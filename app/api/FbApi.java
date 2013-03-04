package api;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;

import common.QueryParamsUtil;

/**
 * This class implements interactions with the Facebook REST API
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-02
 *
 */
public class FbApi {
	
	/** The key to use to store the session model object in the session context */
	public static final String FBAPI_OBJ_CONTEXT_KEY = "fbApiObjectContextKey";
	
	// String variables representing the API domain and supported paths
	private static final String GRAPH_API_DOMAIN = "https://graph.facebook.com";
	private static final String PATH_ME = "/me";
	
	/** The access token for this FbApi object, used to make API calls */
	private final String accessToken;
	
	/** Creates a new object that will use the given access token */
	public FbApi(String accessToken) {
		if (accessToken == null) throw new IllegalArgumentException("Access token cannot be null");
		this.accessToken = accessToken;
	}
	
	/** Gets the access token being used */
	public String getToken() {
		return accessToken;
	}
	
	/**
	 * Wraps a JSON response from Facebook and provides some helper methods to access
	 * the data, as well as metadata about when the object was created
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-03-02
	 *
	 */
	public static class FbJsonResponse {
		
		private final JsonNode json; //the actual JSON response
		private final String accessToken; //the access token used for the API call
		private final boolean usedPost; //true if the method used for the query was POST, false if GET
		private final String apiPath; //the API path queries to get this response
		private final String apiParams; //the params passed with the query
		private final StackTraceElement[] stackTraceWhenCreated; //the stack trace when the object was created
		
		/**
		 * Creates a new response object
		 * 
		 * @param apiPath the path queried to get this response
		 * @param json the response that came back
		 */
		private FbJsonResponse(String accessToken, boolean usedPost, String apiPath, String apiParams, JsonNode json) {
			if (accessToken == null) throw new IllegalArgumentException("Access token cannot be null");
			if (apiPath == null) throw new IllegalArgumentException("API path cannot be null");
			if (apiParams == null) throw new IllegalArgumentException("Params cannot be null");
			if (json == null) throw new IllegalArgumentException("Json node cannot be null");
			
			this.accessToken = accessToken;
			this.usedPost = usedPost;
			this.apiPath = apiPath;
			this.apiParams = apiParams;
			this.json = json;
			stackTraceWhenCreated = Thread.currentThread().getStackTrace();
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
			return find("id", PATH_ME).asText();
		}
		
	} //end response class
	
	/**
	 * Performs a request to the given API path and returns the wrapped JSON response
	 * (appends the access token on behalf of the caller)
	 * 
	 * @param usePost if true, uses the POST method to query the API
	 * @param path the path to request. If null or empty, no parameters will be used
	 * @param params the parameters to pass. This string cannot start with an ampersand and cannot
	 * 			start with a question mark
	 */
	private Promise<FbJsonResponse> queryApi(final boolean usePost, final String path, Map<String, String> params) {
		//generate the path and params
		String url = GRAPH_API_DOMAIN + path;
		
		//generate the parameter map
		if (params == null) params = new HashMap<String, String>();
		params.put("method", usePost ? "POST" : "GET");
		params.put("access_token", getToken());
		final String paramStr = QueryParamsUtil.mapToQueryString(params);
		
		//generate the request promise depending on whether we're using POST or GET
		WSRequestHolder reqHolder = WS.url(url);
		Promise<Response> promise;
		if (usePost) {
			promise = reqHolder.post(paramStr);
		}
		else {
			for (String key : params.keySet()) {
				reqHolder.setQueryParameter(key, params.get(key));
			}
			promise = reqHolder.get();
		}
		
		//convert the promise to one for a wrapped JSON result
		return promise.map(new Function<Response, FbJsonResponse>() {

			@Override
			public FbJsonResponse apply(Response resp) throws Throwable {
				return new FbJsonResponse(getToken(), usePost, path, paramStr, resp.asJson());
			}
			
		});
	}
	
	/* *****************************************************
	 *  BEGIN THE PUBLIC API CALL METHODS
	 ***************************************************** */
	
	/** Calls the Facebook /me path and return the result as JSON */
	public Promise<FbJsonResponse> me() {
		return queryApi(false, PATH_ME, null);
	}

}