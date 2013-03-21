package models;

import java.util.concurrent.Callable;

import javax.persistence.MappedSuperclass;
import javax.persistence.OptimisticLockException;

import models.exceptions.FailedOperationException;
import play.db.ebean.Model;
import types.SqlOperationType.BasicDmlModifyingType;

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
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseModel extends Model {
	
	private static int NUM_DML_RETRIES = 5;
	
	//TODO static analysis test that nobody calls Ebean.save(), update(), etc. directly
	//TODO static analysis test that nobody reads or modifies columns in a model directly
	
	/**
	 * This class should be extended by the implementing model class, providing all
	 * methods to create objects
	 * 
	 * @author bigpopakap
	 * @since 2013-02-26
	 */
	protected static abstract class BaseFactory {}
	
	/**
	 * This class should be extended by the implementing model class, providing all
	 * methods to access fields of an object
	 * 
	 * Fields should be declared private and only accessible through methods in this class
	 * This is done to prevent outside classes from modifying model objects
	 * 
	 * Classes should be careful to make defensive copies of returned objects. Except for other
	 * BaseModel objects, which are not immutable, but have limited interaction with outside classes
	 * 
	 * Note that Play generates getters and setters at runtime for classes to conform to the
	 * JavaBean structure that is expected by frameworks like the Ebean ORM. This is ok because
	 * our code cannot reference these methods. So using this getter class is still an effective
	 * way to limit outside access
	 * 
	 * @author bigpopakap
	 * @since 2013-03-02
	 *
	 */
	protected abstract class BaseGetter {}
	
	/**
	 * This class should be extended by the implementing model class, providing all
	 * methods to read data from the table
	 * 
	 * @author bigpopakap
	 * @since 2013-02-26
	 */
	protected static abstract class BaseSelector {}
	
	/**
	 * This class should be extended by the implementing model class, providing all
	 * methods to modify data in the table
	 * 
	 * @author bigpopakap
	 * @since 2013-02-26
	 */
	protected static abstract class BaseUpdater {}
	
	/**
	 * This class should be extended by the implementing model class, providing methods
	 * to validate the model
	 * 
	 * @author bigpopakap
	 * @since 2013-02-26
	 */
	protected static abstract class BaseValidator {}
	
	/* ******************************************
	 *  HOOKS FOR DML OPERATIONS
	 ****************************************** */
	
	/** Called after any DML operation that fails with an OptimisticLockException */
	protected void hook_postFailedModifyingOperation(BasicDmlModifyingType opType) {
		//do nothing. models can override this if they want to do something
	}
	
	/** Called after any DML operation that succeeds */
	protected void hook_postSuccessfulModifyingOperation(BasicDmlModifyingType opType) {
		//do nothing. models can override this if they want to do something
	}
	
	/* ***********************************************************************
	 * BEGIN PRIVATE HELPERS
	 *********************************************************************** */
	
	private void doOperationAndRetry(BasicDmlModifyingType opType, Callable<Void> doOperation) {
		for (int i = 1; i <= NUM_DML_RETRIES; i++) {
			try {
				doOperation.call();
				
				//if the operation was successful, call the post-op and stop retrying
				hook_postSuccessfulModifyingOperation(opType);
				break;
			}
			catch (OptimisticLockException ex) {
				//call the post-op and retry the operation
				hook_postFailedModifyingOperation(opType);
			}
			catch (Exception ex) {
				//TODO should this be thrown like this?
				throw new FailedOperationException(this, opType, ex);
			}
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
	protected void doSaveAndRetry() {
		doOperationAndRetry(BasicDmlModifyingType.INSERT, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_save();
				return null;
			}
			
		});
	}
	
	@Override public void save(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _save(String str) { super.save(str); }
	protected void doSaveAndRetry(final String str) {
		doOperationAndRetry(BasicDmlModifyingType.INSERT, new Callable<Void>() {

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
	protected void doUpdateAndRetry() {
		doOperationAndRetry(BasicDmlModifyingType.UPDATE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_update();
				return null;
			}
			
		});
	}
	
	@Override public void update(Object obj) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _update(Object obj) { super.update(obj); }
	protected void doUpdateAndRetry(final Object obj) {
		doOperationAndRetry(BasicDmlModifyingType.UPDATE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_update(obj);
				return null;
			}
			
		});
	}
	
	@Override public void update(Object obj, String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _update(Object obj, String str) { super.update(obj, str); }
	protected void doUpdateAndRetry(final Object obj, final String str) {
		doOperationAndRetry(BasicDmlModifyingType.UPDATE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_update(obj, str);
				return null;
			}
			
		});
	}
	
	@Override public void update(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _update(String str) { super.update(str); }
	protected void doUpdateAndRetry(final String str) {
		doOperationAndRetry(BasicDmlModifyingType.UPDATE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_update(str);
				return null;
			}
			
		});
	}
	
	@Override public void delete() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _delete() { super.delete(); }
	protected void doDeleteAndRetry() {
		doOperationAndRetry(BasicDmlModifyingType.DELETE, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				_delete();
				return null;
			}
			
		});
	}
	
	@Override public void delete(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	private void _delete(String str) { super.delete(str); }
	protected void doDeleteAndRetry(final String str) {
		doOperationAndRetry(BasicDmlModifyingType.DELETE, new Callable<Void>() {

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
