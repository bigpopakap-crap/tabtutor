package api.fb;

import exeptions.BaseApiException;

/**
 * Exception to be thrown representing an error response from Facebook
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-10
 *
 */
public class FbErrorResponseException extends BaseApiException {
	
	//TODO test that error response exceptions work

	private static final long serialVersionUID = 5011709050554598032L;
	
	private final FbJsonResponse fbJson;
	
	/**
	 * Creates the exception from the given response
	 * @param fbJson must be non-null and must represent an error
	 */
	public FbErrorResponseException(FbJsonResponse fbJson) {
		super();
		if (fbJson == null) throw new IllegalArgumentException("fbJson cannot be null");
		if (!fbJson.isError()) throw new IllegalArgumentException("fbJson must represent an error");
		this.fbJson = fbJson;
	}
	
	/** Gets the error code from Facebook */
	public int getCode() {
		return fbJson.getErrorCode();
	}
	
	/** Gets the message from Facebook */
	public String getMessage() {
		return fbJson.getErrorMessage();
	}
	
	/** Gets the type of the error from Facebook */
	public String getType() {
		return fbJson.getErrorType();
	}
	
	/** Gets the raw response object that was returned */
	public FbJsonResponse getResponse() {
		return fbJson;
	}

}
