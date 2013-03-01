package models;

import javax.persistence.MappedSuperclass;

import play.db.ebean.Model;

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
	
	
	/* ***********************************************************************
	 *  BEGIN INVALIDATION OF DIRECT METHODS
	 *  this will help stop others from making direct modifications to models
	 *  equivalent protected methods are provided for use by the model subclasses
	 ************************************************************************* */
	
	private static final String BLOCKED_REASON = "This operation has been blocked. " +
												 "Use the methods in each model class to modify the model";
	
	@Override
	public void save() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _save() { super.save(); }
	
	@Override
	public void save(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _save(String str) { super.save(str); }
	
	@Override
	public void saveManyToManyAssociations(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _saveManyToManyAssociations(String str) { super.saveManyToManyAssociations(str); }
	
	@Override
	public void saveManyToManyAssociations(String str1, String str2) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _saveManyToManyAssociations(String str1, String str2) { super.saveManyToManyAssociations(str1, str2); }
	
	@Override
	public void update() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _update() { super.update(); }
	
	@Override
	public void update(Object obj) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _update(Object obj) { super.update(obj); }
	
	@Override
	public void update(Object obj, String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _update(Object obj, String str) { super.update(obj, str); }
	
	@Override
	public void update(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _update(String str) { super.update(str); }
	
	@Override
	public void delete() { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _delete() { super.delete(); }
	
	@Override
	public void delete(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _delete(String str) { super.delete(str); }
	
	@Override
	public void deleteManyToManyAssociations(String str) { throw new UnsupportedOperationException(BLOCKED_REASON); }
	protected void _deleteManyToManyAssociations(String str) { super.deleteManyToManyAssociations(str); }

}
