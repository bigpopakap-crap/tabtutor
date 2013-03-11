package api.fb;

import java.util.HashMap;
import java.util.Map;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Http.Status;
import utils.QueryParamsUtil;
import api.ApiResponseOption;
import exeptions.ApiErrorCodeException;
import exeptions.ApiNoResponseException;

/**
 * This class implements interactions with the Facebook REST API
 * 
 * @author bigpopakap
 * @since 2013-03-02
 *
 */
public class FbApi {
	
	/** The key to use to store the session model object in the session context */
	public static final String FBAPI_OBJ_CONTEXT_KEY = "fbApiObjectContextKey";
	
	// String variables representing the API domain and supported paths
	static final String GRAPH_API_DOMAIN = "https://graph.facebook.com";
	static final String PATH_ME = "/me";
	
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
	 * Performs a request to the given API path and returns the wrapped JSON response
	 * (appends the access token on behalf of the caller)
	 * 
	 * @param usePost if true, uses the POST method to query the API
	 * @param path the path to request. If null or empty, no parameters will be used
	 * @param params the parameters to pass. This string cannot start with an ampersand and cannot
	 * 			start with a question mark
	 */
	private Promise<ApiResponseOption<FbJsonResponse>> queryApi(final boolean usePost, final String path, Map<String, String> params) {
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
		return promise.map(new Function<Response, ApiResponseOption<FbJsonResponse>>() {

			@Override
			public ApiResponseOption<FbJsonResponse> apply(Response resp) throws Throwable {
				if (resp == null) {
					return new ApiResponseOption<FbJsonResponse>(
						new ApiNoResponseException()
					);
				}
				else if (resp.getStatus() != Status.OK) {
					return new ApiResponseOption<FbJsonResponse>(
						new ApiErrorCodeException(resp.getStatus())
					);
				}
				else {
					return new ApiResponseOption<FbJsonResponse>(
						new FbJsonResponse(getToken(), usePost, path, paramStr, resp.asJson())
					);
				}
			}
			
		});
	}
	
	/* *****************************************************
	 *  BEGIN THE PUBLIC API CALL METHODS
	 ***************************************************** */
	
	/** Calls the Facebook /me path and return the result as JSON 
	 * @throws FbErrorResponseException 
	 * @throws ApiErrorCodeException 
	 * @throws ApiNoResponseException */
	public Promise<ApiResponseOption<FbJsonResponse>> me() {
		return queryApi(false, PATH_ME, null);
	}

}