package api.fb;

import java.util.Map;

import play.libs.F.Promise;
import play.libs.WS.Response;
import types.HttpMethodType;
import api.ApiResponseOption;
import api.BaseApi;
import exeptions.ApiErrorCodeException;
import exeptions.ApiNoResponseException;

/**
 * This class implements interactions with the Facebook REST API
 * 
 * @author bigpopakap
 * @since 2013-03-02
 *
 */
public class FbApi extends BaseApi<FbJsonResponse> {
	
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
	
	/* *****************************************************
	 *  BEGIN HOOKS AND ABSTRACT IMPLEMENTATIONS
	 ***************************************************** */
	
	@Override
	protected void hook_modifyParams(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params) {
		super.hook_modifyParams(method, urlDomain, urlPath, params);
		params.put("method", method.name());
		params.put("access_token", getToken());
	}
	
	@Override
	protected FbJsonResponse hook_mapResponse(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params, Response resp) {
		return new FbJsonResponse(method, urlDomain, urlPath, params, resp, getToken());
	}
	
	/* *****************************************************
	 *  BEGIN THE PUBLIC API CALL METHODS
	 ***************************************************** */
	
	/** Calls the Facebook /me path and return the result as JSON 
	 * @throws FbErrorResponseException 
	 * @throws ApiErrorCodeException 
	 * @throws ApiNoResponseException */
	public Promise<ApiResponseOption<FbJsonResponse>> me() {
		return query(HttpMethodType.GET, GRAPH_API_DOMAIN, PATH_ME, null);
	}

}