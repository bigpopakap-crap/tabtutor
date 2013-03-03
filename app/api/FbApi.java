package api;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;

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
	public String getAccessToken() {
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
		
		private JsonNode json; //the actual JSON response
		private String apiPath; //the API path queries to get this response
		private StackTraceElement[] stackTraceWhenCreated; //the stack trace when the object was created
		
		/**
		 * Creates a new response object
		 * 
		 * @param apiPath the path queried to get this response
		 * @param json the response that came back
		 */
		private FbJsonResponse(String apiPath, JsonNode json) {
			if (apiPath == null) throw new IllegalArgumentException("API path cannot be null");
			if (json == null) throw new IllegalArgumentException("Json node cannot be null");
			this.apiPath = apiPath;
			this.json = json;
			stackTraceWhenCreated = Thread.currentThread().getStackTrace();
		}
		
		/** Gets the JSON response itself */
		public JsonNode getJson() {
			return json;
		}
		
		/** Gets the Facebook API path called to generate this result */
		public String getApiPath() {
			return apiPath;
		}
		
		/** Sugar method for testing whether this response was returned from any
		 *  one of a given set of API paths */
		public boolean is(String... paths) {
			if (paths == null) throw new IllegalArgumentException("Paths cannot be null");
			for (String path : paths) {
				if (getApiPath().equals(path)) return true;
			}
			return false;
		}
		
		/** Gets the stack trace when this response object was created */
		public StackTraceElement[] getStackTraceWhenCreated() {
			return stackTraceWhenCreated;
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
			else {
				return json.findPath(jsonPath);
			}
		}
		
		/** Get the Facebook ID if this response came from the /me path */
		public String fbId() { return find("id", PATH_ME).asText(); }
		
	}
	
	/** Helper to create a request object for the given API path and given parameters.
	 *  Automatically appends the access token. Note that the params should not start with an ampersand */
	private WSRequestHolder callApi(String path, String getParams) {
		Logger.debug("Calling Facebook API path " + path + " for access token" + getAccessToken());
		return WS.url(GRAPH_API_DOMAIN + path + "?" + getAccessToken() + (getParams != null ? "&" + getParams : ""));
	}
	/** Helper to create a request object with no parameters @see #callApi(String, String) */
	private WSRequestHolder callApi(String path) { return callApi(path, null); }
	
	/** Performs a GET request to the given API path with the given params */
	private Promise<FbJsonResponse> getApi(final String path, String getParams) {
		return callApi(path).get().map(new Function<Response, FbJsonResponse>() {

			@Override
			public FbJsonResponse apply(Response resp) throws Throwable {
				//TODO what if the response is not JSON?
				return new FbJsonResponse(path, resp.asJson());
			}
			
		});
	}
	/** Performs a GET request with no params @see #getApi(String, String) */
	private Promise<FbJsonResponse> getApi(final String path) { return getApi(path, null); }
	
	/** Calls the Facebook /me path and return the result as JSON */
	public Promise<FbJsonResponse> me() { return getApi(PATH_ME); }

}