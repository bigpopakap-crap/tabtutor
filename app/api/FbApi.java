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
	
	private static final String GRAPH_API_DOMAIN = "https://graph.facebook.com";
	private static final String PATH_ME = "/me";
	
	private final String accessToken;
	
	public FbApi(String accessToken) {
		if (accessToken == null) throw new IllegalArgumentException("Access token cannot be null");
		this.accessToken = accessToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * Wraps a JSON response from Facebook and provides some helper methods around that
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-03-02
	 *
	 */
	public static class FbJsonResponse {
		
		private JsonNode json;
		private String apiPath;
		private StackTraceElement[] stackTraceWhenCreated;
		
		private FbJsonResponse(String apiPath, JsonNode json) {
			if (apiPath == null) throw new IllegalArgumentException("API path cannot be null");
			if (json == null) throw new IllegalArgumentException("Json node cannot be null");
			this.apiPath = apiPath;
			this.json = json;
			stackTraceWhenCreated = Thread.currentThread().getStackTrace();
		}
		
		/** Gets the JSON object itself */
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
		
		private JsonNode find(String jsonPath, String... acceptableUrlPaths) {
			if (!is(acceptableUrlPaths)) {
				throw new UnsupportedOperationException("Getting this field is not supported for these API url paths");
			}
			else {
				return json.findPath(jsonPath);
			}
		}
		
		public String fbId() { return find("id", PATH_ME).asText(); }
		
	}
	
	private WSRequestHolder callApi(String path, String getParams) {
		Logger.debug("Calling Facebook API path " + path + " for access token" + getAccessToken());
		return WS.url(GRAPH_API_DOMAIN + path + "?" + getAccessToken() + (getParams != null ? getParams : ""));
	}
	private WSRequestHolder callApi(String path) { return callApi(path, null); }
	
	private Promise<FbJsonResponse> getApi(final String path, String getParams) {
		return callApi(path).get().map(new Function<Response, FbJsonResponse>() {

			@Override
			public FbJsonResponse apply(Response resp) throws Throwable {
				return new FbJsonResponse(path, resp.asJson());
			}
			
		});
	}
	private Promise<FbJsonResponse> getApi(final String path) { return getApi(path, null); }
	
	/** Calls the Facebook /me path and return the result as JSON */
	public Promise<FbJsonResponse> me() { return getApi(PATH_ME); }

}