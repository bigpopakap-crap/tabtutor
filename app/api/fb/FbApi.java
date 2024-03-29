package api.fb;

import java.util.HashMap;
import java.util.Map;

import models.SessionCsrfTokenModel;
import play.libs.WS.Response;
import play.mvc.Http.Request;
import types.HttpMethodType;
import utils.RestUtil;
import api.base.ApiErrorCodeException;
import api.base.ApiNoResponseException;
import api.base.ApiResponseOption;
import api.base.BaseApi;
import contexts.AppContext;

/**
 * This class implements interactions with the Facebook REST API
 * 
 * @author bigpopakap
 * @since 2013-03-02
 *
 */
public class FbApi extends BaseApi<FbJsonResponse> {
	
	private static final int FB_TOKEN_EXPIRY_INVALID = -1;
	
	// String variables representing the API domain and supported paths
	static final String DOMAIN_FB = "https://www.facebook.com";
	static final String DOMAIN_GRAPH_API = "https://graph.facebook.com";
	static final String PATH_LOGIN_REDIRECT = "/dialog/oauth";
	static final String PATH_OAUTH_ACCESSTOKEN = "/oauth/access_token";
	static final String PATH_ME = "/me";
	
	// String variables representing keys to use in query params
	private static final String QUERY_KEY_CLIENT_ID = "client_id";
	private static final String QUERY_KEY_CLIENT_SECRET = "client_secret";
	private static final String QUERY_KEY_STATE = "state";
	private static final String QUERY_KEY_HTTP_METHOD = "method";
	private static final String QUERY_KEY_REDIRECT_URI = "redirect_uri";
	private static final String QUERY_KEY_ACCESS_TOKEN = "access_token";
	private static final String QUERY_KEY_ACCESS_TOKEN_EXPIRY = "expires";
	private static final String QUERY_KEY_OAUTH_CODE = "code";
	private static final String QUERY_KEY_SCOPE = "scope";
	
	// Standard values for the above keys, where applicable
	private static final String QUERY_VALUE_SCOPE = "email";
	
	/** The access token for this FbApi object, used to make API calls */
	private final String accessToken;
	private final int tokenExpiry;
	
	/** Creates a new object that will use the given access token */
	public FbApi(String accessToken) {
		this(accessToken, FB_TOKEN_EXPIRY_INVALID);
	}
	
	/** Creates a new object that will use the given access token and specifies the seconds until expires */
	private FbApi(String accessToken, int tokenExpiry) {
		if (accessToken == null) throw new IllegalArgumentException("Access token cannot be null");
		this.accessToken = accessToken;
		this.tokenExpiry = tokenExpiry;
	}
	
	/** Gets the access token being used */
	public String getToken() {
		return accessToken;
	}
	
	/** Determines whether this object is carrying a valid number of seconds
	 *  until the token expires
	 *  
	 *  This should only be the case for an API object returned from
	 *  {@link #accessToken(String)}
	 */
	public boolean hasTokenExpiry() {
		return tokenExpiry != FB_TOKEN_EXPIRY_INVALID;
	}
	
	/** Gets the number of seconds until the token expires */
	public int getTokenExpiry() {
		return tokenExpiry;
	}
	
	/* *****************************************************
	 *  BEGIN HOOKS AND ABSTRACT IMPLEMENTATIONS
	 ***************************************************** */
	
	@Override
	protected void hook_modifyParams(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params) {
		super.hook_modifyParams(method, urlDomain, urlPath, params);
		params.put(QUERY_KEY_HTTP_METHOD, method.name());
		params.put(QUERY_KEY_ACCESS_TOKEN, getToken());
	}
	
	@Override
	protected FbJsonResponse hook_mapResponse(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params, Response resp) {
		return new FbJsonResponse(method, urlDomain, urlPath, params, resp, getToken());
	}
	
	/* *****************************************************
	 *  BEGIN THE PUBLIC API CALL METHODS
	 ***************************************************** */
	
	/** Returns the redirect URI to use for Facebook login. TODO quiet this damn compiler error */
	public static String fbLoginRedirectUri(Request request, String targetUrl) {
		return controllers.web.routes.AuthWebController.fblogin(null, null, targetUrl).absoluteURL(request);
	}
	
	/** Gets the URL to redirect the user for Facebook login */
	public static String fbLoginUrl(Request request, String targetUrl) {
		return new StringBuilder().append(DOMAIN_FB).append(PATH_LOGIN_REDIRECT)
									.append("?").append(RestUtil.mapToQueryString(
											QUERY_KEY_CLIENT_ID, AppContext.Var.FB_APP_ID.val(),
											QUERY_KEY_REDIRECT_URI, fbLoginRedirectUri(request, targetUrl),
											QUERY_KEY_STATE, SessionCsrfTokenModel.create().getCsrfToken().toString(),
											QUERY_KEY_SCOPE, QUERY_VALUE_SCOPE
									))
									.toString();
	}
	
	/**
	 * Gets an access token, and creates an FbApi object with that token
	 * This should be the only way to create an object for which hasTokenExpiry() is true 
	 * @throws ApiNoResponseException if there is an error communicating with Facebook
	 */
	public static FbApi accessToken(final Request request, final String targetUrl, final String code) throws ApiNoResponseException {
		if (code == null) throw new IllegalArgumentException("Code cannot be null");
		
		Map<String, String> params = new HashMap<>();
		params.put(QUERY_KEY_CLIENT_ID, AppContext.Var.FB_APP_ID.val());
		params.put(QUERY_KEY_REDIRECT_URI, fbLoginRedirectUri(request, targetUrl));
		params.put(QUERY_KEY_CLIENT_SECRET, AppContext.Var.FB_APP_SECRET.val());
		params.put(QUERY_KEY_OAUTH_CODE, code);

		Response resp = rawQuery(HttpMethodType.POST, DOMAIN_GRAPH_API + PATH_OAUTH_ACCESSTOKEN, params);
		
		//TODO throw an ApiInitializationError on error during this constructor
		//if the response is an erroneous response
		
		//parse the response for the token and expiry
		Map<String, String> paramMap = RestUtil.queryStringToMap(resp.getBody());
		return new FbApi(paramMap.get(QUERY_KEY_ACCESS_TOKEN), Integer.parseInt(paramMap.get(QUERY_KEY_ACCESS_TOKEN_EXPIRY)));
	}
	
	/** Calls the Facebook /me path and return the result as JSON 
	 * @throws FbErrorResponseException 
	 * @throws ApiErrorCodeException 
	 * @throws ApiNoResponseException */
	public ApiResponseOption<FbJsonResponse> me() {
		return query(HttpMethodType.GET, DOMAIN_GRAPH_API, PATH_ME, null);
	}
	
}