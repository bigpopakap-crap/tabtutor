package api.base;

/**
 * General API exception for when the service does responds with an
 * error code and possible a message
 * 
 * @author bigpopakap
 * @since 2013-03-08
 *
 */
public class ApiErrorCodeException extends BaseApiException {
	
	private static final long serialVersionUID = -2953732413793485446L;
	
	private int code;
	private String body;
	
	/**
	 * @param code the error code returned
	 */
	ApiErrorCodeException(int code) {
		this(code, null);
	}
	
	/**
	 * @param code the error code returned
	 * @param message the body of the response, or null if there was no body
	 */
	ApiErrorCodeException(int code, String body) {
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
