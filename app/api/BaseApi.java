package api;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import play.Logger;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Http.Status;
import types.HttpMethodType;
import utils.EscapingUtil;
import utils.EscapingUtil.Escaper;
import utils.ConcurrentUtil;
import api.exceptions.ApiErrorCodeException;
import api.exceptions.ApiNoResponseException;

/**
 * The base class for classes implementing interactions with 3rd party APIs.
 * Provides some utility methods for making queries
 * 
 * The utility methods for making queries are kept protected in this class to
 * help enforce that any requests made to third party APIs are done so
 * by subclassing this class
 * 
 * @author bigpopakap
 * @since 2013-03-17
 *
 * @param <R> the response type specific to the API class
 */
public abstract class BaseApi<R extends BaseApiResponse<?>> {
	
	//TODO ensure that only subclasses of this class make third-party requests
	
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
	
	/**
	 * Performs a request to the given API path and returns the wrapped JSON response
	 * (appends the access token on behalf of the caller)
	 * 
	 * Uses {@link #rawQuery(HttpMethodType, String, Map)} to make the query, then maps
	 * it to the correct object
	 * 
	 * @param method the HTTP method to use for the request
	 * @param path the full url to query
	 * @param params the parameters to pass. If null or empty, no parameters will be used (except those
	 * 				added by the hook method to add common params)
	 */
	protected final ApiResponseOption<R> query(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params) {
		//default or throw exception on nulls
		if (method == null) method = HttpMethodType.GET;
		if (urlDomain == null) throw new IllegalArgumentException("UrlDomain cannot be null");
		if (urlPath == null) throw new IllegalArgumentException("UrlPath cannot be null");
		
		//generate the parameter map, add common params, and convert them to a query string
		if (params == null) params = new HashMap<String, String>();
		hook_modifyParams(method, urlDomain, urlPath, params);
		
		//create final versions of variables
		final HttpMethodType fMethod = method;
		final String fUrlDomain = urlDomain;
		final String fUrlPath = urlPath;
		final Map<String, String> fParams = params;
		
		//get the response object
		Response resp = null;;
		try {
			resp = rawQuery(method, urlDomain + urlPath, params);
		}
		catch (ApiNoResponseException ex) {
			return new ApiResponseOption<R>(
				new ApiNoResponseException()
			);
		}
		
		//map the response object to a response option
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
	
	/**
	 * Creates a promise to query the given url, with the given HTTP method and the given params
	 * Url must be non-null
	 * 
	 * Makes the call in its own thread, so it is not idly waiting. Don't worry about that!
	 * 
	 * Note that only GET and POST are supported currently
	 * 
	 * @throws ApiNoResponseException if any exception is thrown while getting the promise
	 */
	protected static final Response rawQuery(HttpMethodType method, String url, Map<String, String> params) throws ApiNoResponseException {
		//default or throw exception on nulls
		if (method == null) method = HttpMethodType.GET;
		if (url == null) throw new IllegalArgumentException("Url cannot be null");
		
		//generate the parameter map, and convert them to a query string
		if (params == null) params = new HashMap<String, String>();
		String paramStr = mapToQueryString(params);
		
		//generate the request promise depending on whether we're using POST or GET
		WSRequestHolder reqHolder = WS.url(url);
		Promise<Response> promise = null;
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
		
		try {
			//get the response in its own thread
			final Promise<Response> fPromise = promise;
			final HttpMethodType fMethod = method;
			final String fUrl = url;
			final Map<String, String> fParams = params;
			return ConcurrentUtil.joinThread(new Callable<Response>() {

				@Override
				public Response call() throws Exception {
					Logger.trace("Querying " + fMethod + " " + fUrl + " " + fParams);
					Response resp = fPromise.get();
					Logger.trace("Received response " + resp.getBody());
					return resp;
				}
				
			});
		}
		catch (Exception ex) {
			throw new ApiNoResponseException(ex);
		}
	}
	
	/** Converts a map of key-value pairs to a query string without the leading "?" */
	protected static final String mapToQueryString(Map<String, String> map) {
		StringBuilder str = new StringBuilder();
		for (String key : map.keySet()) {
			str.append(str.length() > 0 ? "&" : "")
			   .append(EscapingUtil.escape(key, Escaper.URL))
			   .append("=")
			   .append(EscapingUtil.escape(map.get(key), Escaper.URL));
		}
		return str.toString();
	}
	
	/** 
	 * The same as {@link #mapToQueryString(Map)}, but the map is represented as
	 * pairs of strings
	 * 
	 * Example: mapToQueryStrign("key1", "val1", "key1", "val1")
	 * 
	 * @throws IllegalArgumentException if there are not an even number of arguments
	 */
	protected static final String mapToQueryString(String... params) throws IllegalArgumentException {
		if (params.length % 2 != 0) throw new IllegalArgumentException("There must be an even number of params");
		
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < params.length; i += 2) {
			map.put(params[i], params[i + 1]);
		}
		
		return mapToQueryString(map);
	}
	
	/** Converts a query string to a map of key-value pairs
	 *  The input can be a full URL, or just the params, and may start with a ? or not
	 *  
	 *  DOES NOT do url-decoding
	 *  
	 *  Note that keys are overwritten as they are found, so if there are multiple
	 *  of the same key in the query string, the first value is the one that will be kept
	 *  
	 *  */
	protected static final Map<String, String> queryStringToMap(String params) {
		if (params == null || params.isEmpty()) {
			return new TreeMap<String, String>();
		}
		
		Map<String, String> map = new HashMap<String, String>();
		
		//TODO make this more efficient by using regex
		//start after the question mark, or at the beginning of the string
		int startIndex = params.indexOf('?') + 1;
		if (startIndex >= params.length()) {
			//there is not more of the string remaining
			return map;
		}
		
		//split the string by ampersands
		String[] pairs = params.substring(startIndex).split("&");
		for (String pair : pairs) {
			String[] keyValue = pair.split("=");
			map.put(keyValue[0], keyValue[1]);
		}
		
		return map;
	}
	
}
