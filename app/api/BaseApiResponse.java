package api;

import exeptions.BaseApiException;

/**
 * Base response object that all responses objects from 3rd party API calls should
 * extend. It enforces two methods that determine if the response represents
 * an error, and what exception should be thrown to represent that error
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-08
 * 
 * @param <T> the type of the exception that is associated with an errorneous response
 */
public abstract class BaseApiResponse<T extends BaseApiException> {
	
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
