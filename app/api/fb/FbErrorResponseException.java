package api.fb;

import exeptions.BaseApiException;

public class FbErrorResponseException extends BaseApiException {

	private static final long serialVersionUID = 5011709050554598032L;
	
	private final FbJsonResponse fbJson;
	
	public FbErrorResponseException(FbJsonResponse fbJson) {
		super();
		if (fbJson == null) throw new IllegalArgumentException("fbJson cannot be null");
		if (!fbJson.isError()) throw new IllegalArgumentException("fbJson must represent an error");
		this.fbJson = fbJson;
	}
	
	public int getCode() {
		return fbJson.getErrorCode();
	}
	
	public String getMessage() {
		return fbJson.getErrorMessage();
	}
	
	public String getType() {
		return fbJson.getErrorType();
	}

}
