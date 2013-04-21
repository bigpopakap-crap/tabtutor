package models;

import globals.Globals.DevelopmentSwitch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.MappedSuperclass;
import javax.persistence.OptimisticLockException;

import models.exceptions.FailedOperationException;
import play.db.ebean.Model;
import types.SqlOperationType.BasicDmlModifyingType;
import utils.ConcurrentUtil;
import utils.DateUtil;
import utils.Logger;
import utils.ReflectUtil;
import contexts.RequestStatsContext;

/**
 * Base class for all models. All models should extend this class
 * 
 * Models must provide an extension of the Factor, Interactor and Validator classes, which will preferably be
 * statically defined in the implementing class
 * 
 * @author bigpopakap
 * @since 2013-02-26
 *
 */
@MappedSuperclass
public abstract class BaseModel extends Model {
	
	private static final long serialVersionUID = 1L;
	private static DevelopmentSwitch<Integer> NUM_OPERATION_RETRIES = new DevelopmentSwitch<>(5);
	
	//TODO static analysis test that nobody calls Ebean.save(), update(), etc. directly
	//TODO static analysis test that nobody reads or modifies columns in a model directly
	
	/* ******************************************
	 *  HOOKS FOR DML OPERATIONS
	 ****************************************** */
	
	/**
	 * Called before the retry of any DML operation (before {@link #hook_preModifyingOperation(BasicDmlModifyingType)})
	 * Note that this is not called before the first try
	 * 
	 * Default implementation is just to log the operation retry
	 * 
	 * @param opType the type of the operation
	 */
	protected void hook_preModifyingOperationRetry(BasicDmlModifyingType opType) {
		//do nothing but log. models can override this if they want to do something
		Logger.trace("Retrying " + opType.name() + " operation on " + this.getClass().getCanonicalName());
		RequestStatsContext.get().incrModelOperationRetries();
	}
	
	/**
	 * Called before any DML operation (after {@link #hook_preModifyingOperationRetry(BasicDmlModifyingType)}, if it's a retry)
	 * 
	 * Default implementation is to update any timestamps and expire times
	 * 
	 * @see CreateTime
	 * @see UpdateTime
	 * @see ExpireTime
	 * 
	 * @param opType the type of the operation
	 */
	protected void hook_preModifyingOperation(BasicDmlModifyingType opType) {
		//try updating the timestamps
		try {
			updateDateFields(opType);
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			//don't do anything, just log the error
			Logger.error("Exception while updating timestamps in " + this.getClass().getCanonicalName(), ex);
		}
	}

	/**
	 * Called after every DML operation, whether it succeeded or not
	 * 
	 * This is called after all retries have completed, not before each retry.
	 * For that functionality, see {@link #hook_preModifyingOperationRetry(BasicDmlModifyingType)}
	 * 
	 * Default implementation is just to log the operation
	 * 
	 * @param opType the type of the operation
	 * @param wasSuccessful true if the operation succeeded, false if it failed.
	 * 							Failure means that it failed after all retries
	 */
	protected void hook_postModifyingOperation(BasicDmlModifyingType opType, boolean wasSuccessful) {
		//do nothing but log. models can override this if they want to do something
		Logger.trace(opType.name() + " operation on " + this.getClass().getCanonicalName() + (wasSuccessful ? " was successful" : " failed"));
		if (!wasSuccessful) RequestStatsContext.get().incrModelOperationFailures();
	}
	
	/* ***********************************************************************
	 * BEGIN PUBLIC OVERRIDES
	 *********************************************************************** */
	
	/** Default toString that returns the field=value mappings */
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + ":" + ReflectUtil.getFieldMap(this);
	}
	
	/* ***********************************************************************
	 * BEGIN PRIVATE HELPERS
	 *********************************************************************** */
	
	/**
	 * Class to run database operations and do all retry logic involved in that
	 * This class is implemented this way so that it can be wrapped into it's own thread
	 * 
	 * @author bigpopakap
	 * @since 2013-03-23
	 *
	 */
	private class OperationCallable implements Callable<Void> {
		
		private final BaseModel model;
		private final BasicDmlModifyingType opType;
		private final Callable<Void> doOperation;
		
		/**
		 * Creates a new instance of this class
		 * @param model the model on which the operation will execute
		 * @param opType the type of the operation
		 * @param doOperation the callable that implements the operation, without retry logic
		 */
		private OperationCallable(BaseModel model, BasicDmlModifyingType opType, Callable<Void> doOperation) {
			if (model == null) throw new IllegalArgumentException("Model cannot be null");
			if (opType == null) throw new IllegalArgumentException("OpType cannot be null");
			if (doOperation == null) throw new IllegalArgumentException("doOperation cannot be null");
			this.model = model;
			this.opType = opType;
			this.doOperation = doOperation;
		}

		@Override
		public Void call() throws Exception {
			boolean isFirstTry = true;
			boolean wasSuccessful = false;
			try {
				for (int i = 1; i <= NUM_OPERATION_RETRIES.get() && !wasSuccessful; i++) {
					try {
						//if this is not the first attempt, call the retry hook
						if (!isFirstTry) hook_preModifyingOperationRetry(opType);
						isFirstTry = false;
						
						//call the pre-op hook
						hook_preModifyingOperation(opType);
						
						//try the operation
						doOperation.call();
						wasSuccessful = true;
					}
					catch (OptimisticLockException ex) {
						//call the post-op and retry the operation
						Logger.debug(opType + " operation for " + this.getClass().getCanonicalName() + " failed, retrying...");
					}
				}
				
				//throw exception if not successful
				if (!wasSuccessful) {
					//TODO should the cause be set to null?
					throw new FailedOperationException(model, opType, null);
				}
			}
			catch (FailedOperationException ex) {
				//relay this exception
				throw ex;
			}
			catch (Exception ex) {
				//any other exception thrown means a failed operation
				if (wasSuccessful) throw new IllegalStateException("wasSuccessful should never be true in this block", ex);
				
				try {
					hook_postModifyingOperation(opType, wasSuccessful);
				}
				catch (Exception ex2) {
					Logger.error("Caught exception in BaseModel post operation hook", ex2);
				}
				
				//wrap the cause exception
				throw new FailedOperationException(model, opType, ex);
			}
			finally {
				//catch exceptions here so callers don't think the operation failed when it didn't
				try {
					//we've done all the retries, check if the operation was successful
					hook_postModifyingOperation(opType, wasSuccessful);
				}
				catch (Exception ex) {
					Logger.error("Caught exception in finally of BaseModel operation ", ex);
				}
			}
			
			//shut up the compiler
			return null;
		}
		
	}
	
	/**
	 * Executes the given DML operation, doing all retry logic
	 * 
	 * @param opType the type of the operation
	 * @param doOperation the callable that actually executes the operation (without any retry logic)
	 * @return this object for convenience
	 * @throws FailedOperationException if the operation fails after all retries
	 */
	private BaseModel doOperationAndRetry(BasicDmlModifyingType opType, Callable<Void> doOperation) throws FailedOperationException {
		try {
			ConcurrentUtil.joinThread(new OperationCallable(this, opType, doOperation));
			return this;
		}
		catch (RuntimeException ex) {
			//just relay that exception
			throw ex;
		}
		catch (Exception ex) {
			//wrap the exception in a RuntimeException
			throw new RuntimeException(ex);
		}
	}
	
	/** TODO */
	private void updateDateFields(BasicDmlModifyingType opType) throws IllegalArgumentException, IllegalAccessException {
		Date now = DateUtil.now();
		
		//update the create times if this is an insert
		if (opType == BasicDmlModifyingType.INSERT) {
			List<Field> createFields = ReflectUtil.getFieldsWithAnnotation(this.getClass(), CreateTime.class);
			for (Field createField : createFields) {
				setDateField(CreateTime.class, createField, now);
			}
		}
		
		//update the updates time if this is an update or insert
		//TODO detect if it's actually being updated, or it's just the same
		if (opType == BasicDmlModifyingType.INSERT || opType == BasicDmlModifyingType.UPDATE) {
			List<Field> updateFields = ReflectUtil.getFieldsWithAnnotation(this.getClass(), UpdateTime.class);
			for (Field updateField : updateFields) {
				setDateField(UpdateTime.class, updateField, now);
			}
		}
		
		//update the expire times according
		if (opType == BasicDmlModifyingType.INSERT || opType == BasicDmlModifyingType.UPDATE) {
			//this if is here to avoid doing reflection unless there's a chance we might have to
			
			List<Field> expireFields = ReflectUtil.getFieldsWithAnnotation(this.getClass(), ExpireTime.class);
			for (Field expireField : expireFields) {
				//this should never be null because it was returned as a field that has an annotation of this class
				ExpireTime config = expireField.getAnnotation(ExpireTime.class);
				if (config == null) throw new IllegalStateException("config should never be null");
				
				//set the field on all inserts, and updates if requested
				if (opType == BasicDmlModifyingType.INSERT || (opType == BasicDmlModifyingType.UPDATE && config.extendOnUpdate())) {
					setDateField(ExpireTime.class, expireField, DateUtil.add(now, config.numSeconds()));
				}
			}
		}
	}

	/** TODO */
	private void setDateField(Class<?> ann, Field field, Date date) throws IllegalArgumentException, IllegalAccessException {
		if (field == null) return; //don't do anything with a null field

		//make sure it is of the correct type before setting it
		if (field.getType() != Date.class) {
			Logger.error(ann.getCanonicalName() + " annotation used on field " +
						 field.getName() + " in " +
						 this.getClass().getCanonicalName() +
						 ", and is not a Date type");
		}
		else {
			field.set(this, date);
		}
	}
	
	/* ***********************************************************************
	 *  BEGIN PROTECTED NESTED CLASSES
	 ************************************************************************* */
	
	/**
	 * To mark a model's field as the one that holds the created time
	 * This will be updated in {@link BaseModel}
	 * 
	 * This should only be associated with {@link Date} fields
	 * 
	 * @author bigpopakap
	 * @since 2013-04-16
	 *
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	protected static @interface CreateTime {

	}
	
	/**
	 * To mark a model's field as the one that holds the updated time
	 * This will be updated in {@link BaseModel}
	 * 
	 * Note that this doesn't cover some cases, like updating the time
	 * when the user logs in, because those cases don't need this column
	 * to be modified on *ever* single update operation
	 * 
	 * This should only be associated with {@link Date} fields
	 * 
	 * @author bigpopakap
	 * @since 2013-04-16
	 *
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	protected static @interface UpdateTime {

		/** If true, does not get updated unless the row actually changes.
		 *  Otherwise will get updated only when the row actually changes */
		public boolean ignoreNoOp() default true;
		
	}
	
	/**
	 * To mark a model's field as the one that holds the expire time
	 * This will be updated in {@link BaseModel}
	 * 
	 * This should only be associated with {@link Date} fields
	 * 
	 * @author bigpopakap
	 * @since 2013-04-16
	 *
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	protected static @interface ExpireTime {
		
		/** The number of seconds from the current time to set the expiration time */
		public int numSeconds();
		
		/** Indicates whether the expire time should be extended on a row update */
		public boolean extendOnUpdate() default false;

	}

	
	/* ***********************************************************************
	 *  BEGIN INVALIDATION OF DIRECT METHODS
	 *  this will help stop others from making direct modifications to models
	 *  equivalent protected methods are provided for use by the model subclasses
	 ************************************************************************* */
	
	private static final String BLOCKED_REASON = "This operation has been blocked. " +
												 "Use the methods in each model class to modify the model";
	
	@Override public void save() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _save() { super.save(); }
	protected BaseModel doSaveAndRetry() {
		return doOperationAndRetry(BasicDmlModifyingType.INSERT, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_save();
				return null;
			}
			
		});
	}
	
	@Override public void save(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _save(String str) { super.save(str); }
	protected BaseModel doSaveAndRetry(final String str) {
		return doOperationAndRetry(BasicDmlModifyingType.INSERT, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_save(str);
				return null;
			}
			
		});
	}
	
	@Override
	public void saveManyToManyAssociations(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	
	@Override
	public void saveManyToManyAssociations(String str1, String str2) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	
	@Override public void update() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _update() { super.update(); }
	protected BaseModel doUpdateAndRetry() {
		return doOperationAndRetry(BasicDmlModifyingType.UPDATE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_update();
				return null;
			}
			
		});
	}
	
	@Override public void update(Object obj) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _update(Object obj) { super.update(obj); }
	protected BaseModel doUpdateAndRetry(final Object obj) {
		return doOperationAndRetry(BasicDmlModifyingType.UPDATE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_update(obj);
				return null;
			}
			
		});
	}
	
	@Override public void update(Object obj, String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _update(Object obj, String str) { super.update(obj, str); }
	protected BaseModel doUpdateAndRetry(final Object obj, final String str) {
		return doOperationAndRetry(BasicDmlModifyingType.UPDATE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_update(obj, str);
				return null;
			}
			
		});
	}
	
	@Override public void update(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _update(String str) { super.update(str); }
	protected BaseModel doUpdateAndRetry(final String str) {
		return doOperationAndRetry(BasicDmlModifyingType.UPDATE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_update(str);
				return null;
			}
			
		});
	}
	
	@Override public void delete() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _delete() { super.delete(); }
	protected BaseModel doDeleteAndRetry() {
		return doOperationAndRetry(BasicDmlModifyingType.DELETE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_delete();
				return null;
			}
			
		});
	}
	
	@Override public void delete(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _delete(String str) { super.delete(str); }
	protected BaseModel doDeleteAndRetry(final String str) {
		return doOperationAndRetry(BasicDmlModifyingType.DELETE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_delete(str);
				return null;
			}
			
		});
	}
	
	@Override
	public void deleteManyToManyAssociations(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	
}
