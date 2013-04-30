package controllers.exceptions;

import play.mvc.Result;

/**
 * This is the base exception class for exeptions that should be surfaced to the
 * client as an error response of some sort. These will be handled at the very
 * top level controller
 * 
 * Throwing this exception should interrupt the processing of the current request,
 * and the result() method will generate the result that should be returned
 * to the client
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public abstract class BaseExposedException extends RuntimeException {

	private static final long serialVersionUID = -935805780499802623L;
	
	//do *NOT* support a no-argument constructor in order to force classes to specify a cause
	
	/** Only define this constructor so all subclasses are forced to specify the cause */
	public BaseExposedException(Throwable cause) {
		super(cause);
	}
	
	/** Returns the result representing this exception that should be sent to the client */
	public abstract Result result();

}
