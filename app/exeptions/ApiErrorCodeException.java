package exeptions;

/**
 * General API exception for when the service does responds with an
 * error code and possible a message
 * 
 * @author bigpopakap
 * @since 2013-03-08
 *
 */
public class ApiErrorCodeException extends BaseApiException {
	
	private int code;
	private String body;
	
	/**
	 * @param code the error code returned
	 */
	public ApiErrorCodeException(int code) {
		this(code, null);
	}
	
	/**
	 * @param code the error code returned
	 * @param message the body of the response, or null if there was no body
	 */
	public ApiErrorCodeException(int code, String body) {
		super();
		this.code = code;
		this.body = body;
	}
	
	public int getCode() {
		return code;
	}
	
	public boolean hasBody() {
		return body != null;
	}
	
	public String getBody() {
		return body;
	}

}
