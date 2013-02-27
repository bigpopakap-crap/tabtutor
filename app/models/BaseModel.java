package models;

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

}
