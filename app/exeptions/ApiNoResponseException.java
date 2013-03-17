package exeptions;

/**
 * General API exception for when the service does not respond (i.e. the request
 * times out)
 * 
 * @author bigpopakap
 * @since 2013-03-08
 *
 */
public class ApiNoResponseException extends BaseApiException {
	
	/** Default constructor */
	public ApiNoResponseException() {
		super();
	}

}
