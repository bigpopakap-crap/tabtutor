package api.fb;

import java.util.Map;

import play.libs.WS.Response;
import types.HttpMethodType;
import api.BaseApiJsonResponse;

/**
 * Wraps a JSON response from Facebook and provides some helper methods to access
 * the data, as well as metadata about when the object was created
 * 
 * @author bigpopakap
 * @since 2013-03-02
 *
 */
public class FbJsonResponse extends BaseApiJsonResponse<FbErrorResponseException> {
	
	private final String accessToken; //the access token used for the API call
	
	FbJsonResponse(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params, Response resp, String accessToken) {
		super(method, urlDomain, urlPath, params, resp);
		
		if (accessToken == null) throw new IllegalArgumentException("AccessToken cannot be null");
		this.accessToken = accessToken;
	}
	
	/** Gets the access token used in the request that produced this result */
	public String getToken() {
		return accessToken;
	}
	
	/* *********************************************************
	 *  BEGIN METHODS TO QUERY ERRORS IN RESPONSES
	 ********************************************************* */
	
	/** Returns true if this response represents an error */
	@Override
	public boolean isError() {
		return !getJson().path("error").isMissingNode();
	}
	
	@Override
	public FbErrorResponseException getExceptionNoIsErrorCheck() {
		return new FbErrorResponseException(this);
	}
	
	/** Gets the error code. Throws an exception if this is called and it
	 *  is not an error response */
	public int getErrorCode() {
		//TODO implement this
		if (!isError()) throw new IllegalStateException("This is not an error response");
		throw new UnsupportedOperationException("This is not implemented yet");
	}
	
	/** Gets the error message. Throws an exception if this is called and it is
	 * 	not an error response */
	public String getErrorMessage() {
		//TODO implement this
		if (!isError()) throw new IllegalStateException("This is not an error response");
		throw new UnsupportedOperationException("This is not implemented yet");
	}
	
	/** Gets the error type. Throws an exception if this is called and it is
	 * 	not an error response */
	public String getErrorType() {
		//TODO implement this
		if (!isError()) throw new IllegalStateException("This is not an error response");
		throw new UnsupportedOperationException("This is not implemented yet");
	}
	
	/* *********************************************************
	 *  BEGIN METHODS TO EXTRACT INFO FROM VARIOUS API CALL RESPONSES
	 ********************************************************* */
	
	/** Get the Facebook ID if this response came from the /me path */
	public String fbId() {
		return find("id", FbApi.PATH_ME).asText();
	}
	
	/** Get the username if this response came from the /me path */
	public String username() {
		return find("username", FbApi.PATH_ME).asText();
	}
	
	/** Get the first name if this response came from the /me path */
	public String firstName() {
		return find("first_name", FbApi.PATH_ME).asText();
	}
	
	/** Get the first name if this response came from the /me path */
	public String lastName() {
		return find("last_name", FbApi.PATH_ME).asText();
	}
	
	/** Get the first name if this response came from the /me path */
	public String email() {
		return find("email", FbApi.PATH_ME).asText();
	}
	
} //end response class