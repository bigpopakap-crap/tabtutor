package exeptions;

/**
 * Base exception that all exceptions related to 3rd party API calls should extend
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-08
 *
 */
public abstract class BaseApiException extends Exception {

	private static final long serialVersionUID = 5491031286228518832L;
	
	/** Default constructor */
	public BaseApiException() {
		super();
	}

}
