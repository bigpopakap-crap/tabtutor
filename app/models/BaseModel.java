package models;

import globals.Globals.DevelopmentSwitch;

import java.util.concurrent.Callable;

import javax.persistence.MappedSuperclass;
import javax.persistence.OptimisticLockException;

import models.exceptions.FailedOperationException;
import play.db.ebean.Model;
import types.SqlOperationType.BasicDmlModifyingType;
import utils.ConcurrentUtil;
import utils.Logger;
import utils.ObjectUtil;
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
	 * Called before an operation retry. This wraps the call to the hook, {@link #hook_preModifyingOperationRetry(BasicDmlModifyingType)}
	 * @param opType the type of the operation
	 */
	private void preModifyingOperationRetry(BasicDmlModifyingType opType) {
		//log the retry, increment the number of retries and call the hook
		Logger.trace("Retrying " + opType.name() + " operation on " + this.getClass().getCanonicalName());
		RequestStatsContext.get().incrModelOperationRetries();
		hook_preModifyingOperationRetry(opType);
	}
	
	/**
	 * Called before the retry of any DML operation.
	 * Note that this is not called before the first try
	 * 
	 * Default implementation is to do nothing
	 * 
	 * This is wrapped by the base model's method, {@link #preModifyingOperationRetry(BasicDmlModifyingType)}
	 * 
	 * @param opType the type of the operation
	 */
	protected void hook_preModifyingOperationRetry(BasicDmlModifyingType opType) {
		//do nothing
	}
	
	/**
	 * Called before an operation retry. This wraps the call to the hook, {@link #hook_preModifyingOperationRetry(BasicDmlModifyingType)}
	 * @param opType the type of the operation
	 */
	private void postModifyingOperation(BasicDmlModifyingType opType, boolean wasSuccessful) {
		//log the operation, increment the number of failures and call the hook
		Logger.trace(opType.name() + " operation on " + this.getClass().getCanonicalName() + (wasSuccessful ? " was successful" : " failed"));
		if (!wasSuccessful) RequestStatsContext.get().incrModelOperationFailures();
		hook_preModifyingOperationRetry(opType);
	}

	/**
	 * Called after every DML operation, whether it succeeded or not
	 * 
	 * This is called after all retries have completed, not before each retry.
	 * For that functionality, see {@link #hook_preModifyingOperationRetry(BasicDmlModifyingType)}
	 * 
	 * Default implementation is to nothing
	 * 
	 * This is wrapped by the base model's method, {@link #postModifyingOperation(BasicDmlModifyingType, boolean)}
	 * 
	 * @param opType the type of the operation
	 * @param wasSuccessful true if the operation succeeded, false if it failed.
	 * 							Failure means that it failed after all retries
	 */
	protected void hook_postModifyingOperation(BasicDmlModifyingType opType, boolean wasSuccessful) {
		//do nothing
	}
	
	/* ***********************************************************************
	 * BEGIN PUBLIC OVERRIDES
	 *********************************************************************** */
	
	/** Default toString that returns the field=value mappings */
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + ":" + ObjectUtil.getFieldMap(this);
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
				Throwable failureCause = null; //holds any cause that should be thrown with the failed exception
				
				for (int i = 1; i <= NUM_OPERATION_RETRIES.get() && !wasSuccessful; i++) {
					try {
						//if this is not the first attempt, call the hook
						if (!isFirstTry) preModifyingOperationRetry(opType);
						isFirstTry = false;
						
						//try the operation
						doOperation.call();
						wasSuccessful = true;
					}
					catch (OptimisticLockException ex) {
						//call the post-op and retry the operation
						Logger.debug(opType + " operation for " + model.getClass().getCanonicalName() + " failed, retrying...");
						failureCause = ex;
					}
				}
				
				//throw exception if not successful
				if (!wasSuccessful) {
					//TODO should the cause be set to null?
					throw new FailedOperationException(model, opType, failureCause);
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
					postModifyingOperation(opType, wasSuccessful);
				}
				catch (Exception ex2) {
					Logger.error("Caught exception in post operation hook for " + model.getClass().getCanonicalName(), ex2);
				}
				
				//wrap the cause exception
				throw new FailedOperationException(model, opType, ex);
			}
			finally {
				//catch exceptions here so callers don't think the operation failed when it didn't
				try {
					//we've done all the retries, check if the operation was successful
					postModifyingOperation(opType, wasSuccessful);
				}
				catch (Exception ex) {
					Logger.error("Caught exception in finally of operation for " + model.getClass().getCanonicalName(), ex);
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
