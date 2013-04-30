package controllers.exceptions;

import play.mvc.Result;
import play.mvc.Results;

/**
 * Base exposed exception for returning INTERNAL_SERVER_ERROR HTML error code
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class InternalServerErrorExposedException extends BaseExposedException {
	
	private static final long serialVersionUID = 7039651808723919999L;
	
	/** Optional message to return */
	private String message;

	/** Returns an exposed exception that simply responds with an INTERNAL SERVER ERROR with no body */
	public InternalServerErrorExposedException(Throwable cause) {
		this(cause, null);
	}
	
	/** Returns an exposed exception that simply responds with an INTERNAL SERVER ERROR with the given message */
	public InternalServerErrorExposedException(Throwable cause, String message) {
		super(cause);
		this.message = message;
	}
	
	@Override
	public Result hook_result() {
		return message != null ? Results.notFound(message) : Results.notFound();
	}

}
