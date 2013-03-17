package api;

import java.util.HashMap;
import java.util.Map;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Http.Status;
import types.HttpMethodType;
import utils.QueryParamsUtil;
import api.exceptions.ApiErrorCodeException;
import api.exceptions.ApiNoResponseException;

/**
 * The base class for classes implementing interactions with 3rd party APIs.
 * Provides some utility methods for making queries
 * 
 * @author bigpopakap
 * @since 2013-03-17
 *
 * @param <R> the response type specific to the API class
 */
public abstract class BaseApi<R extends BaseApiResponse<?>> {
	
	/**
	 * Performs a request to the given API path and returns the wrapped JSON response
	 * (appends the access token on behalf of the caller)
	 * 
	 * @param method the HTTP method to use for the request
	 * @param path the full url to query
	 * @param params the parameters to pass. If null or empty, no parameters will be used (except those
	 * 				added by the hook method to add common params)
	 */
	protected final Promise<ApiResponseOption<R>> query(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params) {
		//default or throw exception on nulls
		if (method == null) method = HttpMethodType.GET;
		if (urlDomain == null) throw new IllegalArgumentException("UrlDomain cannot be null");
		if (urlPath == null) throw new IllegalArgumentException("UrlPath cannot be null");
		
		//generate the parameter map, add common params, and convert them to a query string
		if (params == null) params = new HashMap<String, String>();
		hook_modifyParams(method, urlDomain, urlPath, params);
		String paramStr = QueryParamsUtil.mapToQueryString(params);
		
		//generate the request promise depending on whether we're using POST or GET
		WSRequestHolder reqHolder = WS.url(urlDomain + urlPath);
		Promise<Response> promise;
		switch (method) {
			case GET:
				for (String key : params.keySet()) {
					reqHolder.setQueryParameter(key, params.get(key));
				}
				promise = reqHolder.get();
				break;
			case POST:
				promise = reqHolder.post(paramStr);
				break;
			default:
				throw new IllegalStateException("Unhandled HTTP method type: " + method);
		}
		
		//create final versions of variables
		final HttpMethodType fMethod = method;
		final String fUrlDomain = urlDomain;
		final String fUrlPath = urlPath;
		final Map<String, String> fParams = params;
		
		//map the response to a response object
		return promise.map(new Function<Response, ApiResponseOption<R>>() {

			@Override
			public ApiResponseOption<R> apply(Response resp) throws Throwable {
				if (resp == null) {
					return new ApiResponseOption<R>(
						new ApiNoResponseException()
					);
				}
				else if (resp.getStatus() != Status.OK) {
					return new ApiResponseOption<R>(
						new ApiErrorCodeException(resp.getStatus())
					);
				}
				else {
					return new ApiResponseOption<R>(hook_mapResponse(fMethod, fUrlDomain, fUrlPath, fParams, resp));
				}
			}
			
		});
	}
	
	/**
	 * Hook that each API subclass can override that will add common parameters to every
	 * request made with the query() method
	 * 
	 * Subclasses have full control over this map, so be careful with it! Overrwriting
	 * existing parameters will happen silently, so make sure to check for that as wel
	 * 
	 * @see #query(HttpMethodType, String, Map)
	 * @param method the method that is going to be used
	 * @param url the url that will be queried
	 * @param params the parameter map to modify. This is guaranteed to be non-null
	 */
	protected void hook_modifyParams(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params) {
		//do nothing. subclasses can override this
	}
	
	/**
	 * Hook to map a successfull HTTP response to the response type of the API class
	 * Should not return null
	 */
	protected abstract R hook_mapResponse(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params, Response resp);

}
