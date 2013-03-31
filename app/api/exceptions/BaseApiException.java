package api.exceptions;

/**
 * Base exception that all exceptions related to 3rd party API calls should extend
 * 
 * @author bigpopakap
 * @since 2013-03-08
 *
 */
public abstract class BaseApiException extends Exception {

	private static final long serialVersionUID = 5491031286228518832L;
	
	public BaseApiException() {
		super();
	}
	
	public BaseApiException(Throwable cause) {
		super(cause);
	}

}
