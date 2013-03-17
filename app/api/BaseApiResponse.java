package api;

import java.util.Map;

import play.libs.WS.Response;
import types.HttpMethodType;
import contexts.AppContext;
import exeptions.BaseApiException;

/**
 * Base response object that all responses objects from 3rd party API calls should
 * extend. It enforces two methods that determine if the response represents
 * an error, and what exception should be thrown to represent that error
 * 
 * @author bigpopakap
 * @since 2013-03-08
 * 
 * @param <T> the type of the exception that is associated with an erroneous response
 */
public abstract class BaseApiResponse<T extends BaseApiException> {
	
	private final HttpMethodType method; //true if the method used for the query was POST, false if GET
	private final String urlDomain; //the base domain of the url queried, NOT including the trailing slash
	private final String urlPath; //the path of the url queried to get this response
	private final Map<String, String> params; //the params passed with the query
	private final Response response; //the raw response
	private final StackTraceElement[] stackTraceWhenCreated; //the stack trace when the object was created
	
	protected BaseApiResponse(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params, Response response) {
		//make sure none of them are null, except response which might be null
		if (method == null) throw new IllegalArgumentException("Method cannot be null");
		if (urlDomain == null) throw new IllegalArgumentException("UrlDomain cannot be null");
		if (urlPath == null) throw new IllegalArgumentException("UrlPath cannot be null");
		if (params == null) throw new IllegalArgumentException("Params cannot be null");
		
		this.method = method;
		this.urlDomain = urlDomain;
		this.urlPath = urlPath;
		this.params = params;
		this.response = response;
		
		//create the stack trace unless we are in production mode
		stackTraceWhenCreated = AppContext.Mode.isProduction()
									? new StackTraceElement[0]
									: Thread.currentThread().getStackTrace();
	}
	
	/** Gets the method used in the request to get this response */
	public HttpMethodType getMethod() {
		return method;
	}
	
	/** Gets the url domain queried in the request to get this response */
	public String getUrlDomain() {
		return urlDomain;
	}
	
	/** Gets the url path queried in the request to get this response */
	public String getUrlPath() {
		return urlPath;
	}
	
	/** Gets the params used in the request to get this response */
	public Map<String, String> getParams() {
		//TODO make a defensive copy?
		return params;
	}
	
	/** Gets the raw response */
	public Response getRawResponse() {
		return response;
	}
	
	/** Gets the stack trace when this object was created */
	public StackTraceElement[] getStackTraceWhenCreated() {
		return stackTraceWhenCreated;
	}
	
	/** Determines if this response represents an error */
	public abstract boolean isError();
	
	/** Gets the error representing this exception, if this does indeed represent
	 *  an exception
	 *  @throw IllegalStateException if this object does not represent an error
	 */
	public final T getException() {
		if (!isError()) throw new IllegalStateException("Cannot call this method on a non-error");
		else return getExceptionNoIsErrorCheck();
	}
	
	/** Gets the error representing this exception, if this does indeed represent
	 *  an exception
	 * 
	 *  This method does not need to check whether this object actually does
	 *  represent an error response
	 */
	protected abstract T getExceptionNoIsErrorCheck();

}
