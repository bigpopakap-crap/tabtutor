package api;

import exeptions.ApiNoResponseException;
import exeptions.BaseApiException;

/**
 * This class represents an API response option that should be returned from
 * and API classs. It will hold either the response, or an exception to be thrown
 * so that the get() method that is used to get the actual response might throw
 * an exception that should be handled
 * 
 * @author bigpopakap
 * @since 2013-03-10
 *
 * @param <R> the type of the response
 */
public class ApiResponseOption<R extends BaseApiResponse<?>> {
	
	private final R resp;
	private final BaseApiException ex;
	
	/** Creates a response options that will return the given response successfully
	 *  (unless the object is an error response, in which case an exception will be
	 *  thrown for that)
	 *  
	 *  @see {@link BaseApiResponse#isError() }
	 *  @see {@link BaseApiResponse#getExceptionNoIsErrorCheck() }
	 */
	public ApiResponseOption(R resp) {
		this(resp, null);
	}
	
	/** Creates a response option that will throw the given exception */
	public ApiResponseOption(BaseApiException ex) {
		this(null, ex);
	}
	
	/**
	 * Creates the object with either an exception to be thrown or a response
	 * to return successfully
	 * 
	 * Exactly one of them must the non-null, and the other must be null
	 * Will either return the response, or throw the exception when the get()
	 * method is called
	 * 
	 * Note that if the response represents an error response, an exception
	 * will be thrown as well
	 * 
	 * @see #ApiResponseOption(BaseApiResponse)
	 */
	private ApiResponseOption(R resp, BaseApiException ex) {
		//make sure they're not both non-null
		if (resp != null && ex != null) {
			throw new IllegalArgumentException("One of these must be null");
		}
		//if both are null, treat it as a no response exception
		else if (resp == null && ex == null) {
			this.resp = null;
			this.ex = new ApiNoResponseException();
		}
		//check whether the response object is specified and an error
		else if (resp != null && resp.isError()) {
			this.resp = null;
			this.ex = resp.getException();
		}
		//check whether the response object is specified
		else if (resp != null) {
			this.resp = resp;
			this.ex = null;
		}
		//otherwise, just set the exception to the one given
		else {
			this.resp = null; //since resp is null, explicity set it here
			this.ex = ex;
		}
	}
	
	/**
	 * Either gets the response object, or throws an exception if the response
	 * was an error
	 * 
	 * @return the response object if there was no error while getting the response
	 * @throws BaseApiException if there was an error getting the response, or the
	 *   						reponse represents and error
	 */
	public R get() throws BaseApiException {
		if (ex != null) throw ex;
		else return resp;
	}

}
