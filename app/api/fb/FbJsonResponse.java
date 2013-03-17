package api.fb;

import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;

import play.libs.WS.Response;
import types.HttpMethodType;
import api.BaseApiResponse;

/**
 * Wraps a JSON response from Facebook and provides some helper methods to access
 * the data, as well as metadata about when the object was created
 * 
 * @author bigpopakap
 * @since 2013-03-02
 *
 */
public class FbJsonResponse extends BaseApiResponse<FbErrorResponseException> {
	
	private final String accessToken; //the access token used for the API call
	private final JsonNode json; //the actual JSON response
	
	public FbJsonResponse(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params, Response resp, String accessToken) {
		super(method, urlDomain, urlPath, params, resp);
		
		if (accessToken == null) throw new IllegalArgumentException("AccessToken cannot be null");
		
		this.json = resp.asJson();
		this.accessToken = accessToken;
	}
	
	/** Gets the access token used in the request that produced this result */
	public String getToken() {
		return accessToken;
	}
	
	/** Gets the JSON response itself */
	public JsonNode getJson() {
		return json;
	}
	
	/** Sugar method for testing whether this response was returned from any
	 *  one of a given set of API paths */
	public boolean is(String... paths) {
		if (paths == null) throw new IllegalArgumentException("Paths cannot be null");
		for (String path : paths) {
			if (getUrlPath().equals(path)) return true;
		}
		return false;
	}
	
	/* *********************************************************
	 *  BEGIN METHODS TO QUERY ERRORS IN RESPONSES
	 ********************************************************* */
	
	/** Returns true if this response represents an error */
	@Override
	public boolean isError() {
		return !json.path("error").isMissingNode();
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
	
	/** Helper to find a value from the JSON if it originated from one of the
	 *  given API paths, or throw an error if it did not come from one of those paths */
	private JsonNode find(String jsonPath, String... acceptableUrlPaths) {
		if (!is(acceptableUrlPaths)) {
			throw new UnsupportedOperationException("Getting this field is not supported for these API url paths");
		}
		else if (isError()) {
			//don't throw an error here, just return no data
			//callers *should* be checking for errors before this anyways
			return JsonNodeFactory.instance.nullNode();
		}
		else {
			return json.path(jsonPath);
		}
	}
	
	/** Get the Facebook ID if this response came from the /me path */
	public String fbId() {
		return find("id", FbApi.PATH_ME).asText();
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