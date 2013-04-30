package controllers.exceptions;

import play.mvc.Result;
import play.mvc.Results;

/**
 * Base exposed exception for returning NOT_FOUND HTML error code
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class NotFoundExposedException extends BaseExposedException {
	
	private static final long serialVersionUID = 6140273326879487189L;
	
	/** Optional message to return */
	private String message;

	/** Returns an exposed exception that simply responds with a NOT FOUND error with no body */
	public NotFoundExposedException(Throwable cause) {
		this(cause, null);
	}
	
	/** Returns an exposed exception that simply responds with a NOT FOUND error with the given message */
	public NotFoundExposedException(Throwable cause, String message) {
		super(cause);
		this.message = message;
	}
	
	@Override
	public Result hook_result() {
		return message != null ? Results.notFound(message) : Results.notFound();
	}

}
