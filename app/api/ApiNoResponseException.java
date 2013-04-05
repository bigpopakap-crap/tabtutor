package api;

/**
 * General API exception for when the service does not respond (i.e. the request
 * times out)
 * 
 * @author bigpopakap
 * @since 2013-03-08
 *
 */
public class ApiNoResponseException extends BaseApiException {
	
	private static final long serialVersionUID = -4920976263519573529L;
	
	ApiNoResponseException() {
		super();
	}
	
	ApiNoResponseException(Throwable cause) {
		super(cause);
	}

}
