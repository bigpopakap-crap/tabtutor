package models;

import javax.persistence.MappedSuperclass;

import play.db.ebean.Model;
import types.SqlCommandType.BasicDmlModifyingType;

/**
 * Base class for all models. All models should extend this class
 * 
 * Models must provide an extension of the Factor, Interactor and Validator classes, which will preferably be
 * statically defined in the implementing class
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-26
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseModel extends Model {
	
	//TODO static analysis test that nobody calls Ebean.save(), update(), etc. directly
	//TODO static analysis test that nobody reads or modifies columns in a model directly
	
	/**
	 * This class should be extended by the implementing model class, providing all
	 * methods to create objects
	 * 
	 * @author bigpopakap@gmail.com
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
	 * @author bigpopakap@gmail.com
	 * @since 2013-03-02
	 *
	 */
	protected abstract class BaseGetter {}
	
	/**
	 * This class should be extended by the implementing model class, providing all
	 * methods to read data from the table
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-26
	 */
	protected static abstract class BaseSelector {}
	
	/**
	 * This class should be extended by the implementing model class, providing all
	 * methods to modify data in the table
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-26
	 */
	protected static abstract class BaseUpdater {}
	
	/**
	 * This class should be extended by the implementing model class, providing methods
	 * to validate the model
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-26
	 */
	protected static abstract class BaseValidator {}
	
	/* ******************************************
	 *  HOOKS FOR DML OPERATIONS
	 ****************************************** */
	
	/** Called after any save() or update() */
	protected void postOp(BasicDmlModifyingType opType) {
		//do nothing. models can override this if they want to do something
	}
	
	
	/* ***********************************************************************
	 *  BEGIN INVALIDATION OF DIRECT METHODS
	 *  this will help stop others from making direct modifications to models
	 *  equivalent protected methods are provided for use by the model subclasses
	 ************************************************************************* */
	
	private static final String BLOCKED_REASON = "This operation has been blocked. " +
												 "Use the methods in each model class to modify the model";
	
	@Override
	public void save() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _save() {
		super.save();
		postOp(BasicDmlModifyingType.INSERT);
	}
	
	@Override
	public void save(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _save(String str) {
		super.save(str);
		postOp(BasicDmlModifyingType.INSERT);
	}
	
	@Override
	public void saveManyToManyAssociations(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _saveManyToManyAssociations(String str) { throw new UnsupportedOperationException(); }
	
	@Override
	public void saveManyToManyAssociations(String str1, String str2) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _saveManyToManyAssociations(String str1, String str2) { throw new UnsupportedOperationException(); }
	
	@Override
	public void update() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _update() {
		super.update();
		postOp(BasicDmlModifyingType.UPDATE);
	}
	
	@Override
	public void update(Object obj) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _update(Object obj) {
		super.update(obj);
		postOp(BasicDmlModifyingType.UPDATE);
	}
	
	@Override
	public void update(Object obj, String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _update(Object obj, String str) {
		super.update(obj, str);
		postOp(BasicDmlModifyingType.UPDATE);
	}
	
	@Override
	public void update(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _update(String str) {
		super.update(str);
		postOp(BasicDmlModifyingType.UPDATE);
	}
	
	@Override
	public void delete() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _delete() {
		super.delete();
		postOp(BasicDmlModifyingType.DELETE);
	}
	
	@Override
	public void delete(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _delete(String str) {
		super.delete(str);
		postOp(BasicDmlModifyingType.DELETE);
	}
	
	@Override
	public void deleteManyToManyAssociations(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _deleteManyToManyAssociations(String str) { throw new UnsupportedOperationException(); }

}
