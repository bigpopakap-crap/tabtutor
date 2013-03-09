package api.fb;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;

import api.BaseApiResponse;

import common.AppContext;
import exeptions.BaseApiException;

/**
 * Wraps a JSON response from Facebook and provides some helper methods to access
 * the data, as well as metadata about when the object was created
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-02
 *
 */
public class FbJsonResponse extends BaseApiResponse {
	
	private JsonNode json; //the actual JSON response
	private String accessToken; //the access token used for the API call
	private boolean usedPost; //true if the method used for the query was POST, false if GET
	private String apiPath; //the API path queries to get this response
	private String apiParams; //the params passed with the query
	private StackTraceElement[] stackTraceWhenCreated; //the stack trace when the object was created
	
	/** Returns a new erroneous response that will throw an exception */
	public FbJsonResponse(BaseApiException error) {
		super(error);
	}
	
	/** TODO doc */
	FbJsonResponse(String accessToken, boolean usedPost, String apiPath, String apiParams, JsonNode json) {
		this(accessToken, usedPost, apiPath, apiParams, json, null);
	}
	
	/**
	 * Creates a new response object
	 * 
	 * @param apiPath the path queried to get this response
	 * @param json the response that came back
	 */
	FbJsonResponse(String accessToken, boolean usedPost, String apiPath, String apiParams, JsonNode json, BaseApiException error) {
		super(error);
		
		if (accessToken == null) throw new IllegalArgumentException("Access token cannot be null");
		if (apiPath == null) throw new IllegalArgumentException("API path cannot be null");
		if (apiParams == null) throw new IllegalArgumentException("Params cannot be null");
		if (json == null) throw new IllegalArgumentException("Json node cannot be null");
		
		this.accessToken = accessToken;
		this.usedPost = usedPost;
		this.apiPath = apiPath;
		this.apiParams = apiParams;
		this.json = json;
		stackTraceWhenCreated = AppContext.Mode.isProduction()
									? new StackTraceElement[0]
									: Thread.currentThread().getStackTrace();
	}
	
	/** Gets the JSON response itself */
	public JsonNode getJson() {
		return json;
	}
	
	/** Gets the access token used in the request that produced this result */
	public String getToken() {
		return accessToken;
	}
	
	/** Gets the method used in the query. True if POST, false if GET */
	public boolean isPost() {
		return usedPost;
	}
	
	/** Gets the Facebook API path called to generate this result */
	public String getPath() {
		return apiPath;
	}
	
	/** Gets the params passed with the query */
	public String getParams() {
		return apiParams;
	}
	
	/** Sugar method for testing whether this response was returned from any
	 *  one of a given set of API paths */
	public boolean is(String... paths) {
		if (paths == null) throw new IllegalArgumentException("Paths cannot be null");
		for (String path : paths) {
			if (getPath().equals(path)) return true;
		}
		return false;
	}
	
	/** Gets the stack trace when this response object was created */
	public StackTraceElement[] getStackTraceWhenCreated() {
		return stackTraceWhenCreated;
	}
	
	/* *********************************************************
	 *  BEGIN METHODS TO QUERY ERRORS IN RESPONSES
	 ********************************************************* */
	
	/** Returns true if this response represents an error */
	public boolean isError() {
		return !json.path("error").isMissingNode();
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