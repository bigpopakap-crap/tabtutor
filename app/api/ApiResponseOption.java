package api;

import exeptions.BaseApiException;

/**
 * This class represents an API response option that should be returned from
 * and API classs. It will hold either the response, or an exception to be thrown
 * so that the get() method that is used to get the actual response might throw
 * an exception that should be handled
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-10
 *
 * @param <R> the type of the response
 */
public class ApiResponseOption<R extends BaseApiResponse<?>> {
	
	private final R resp;
	private final BaseApiException ex;
	
	/** Creates a response option that will throw the given exception */
	public ApiResponseOption(BaseApiException ex) {
		this(null, ex);
	}
	
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
		//make sure exactly one is non-null
		if (ex == null && resp == null) throw new IllegalArgumentException("Both of these cannot be null");
		if (ex != null && resp != null) throw new IllegalArgumentException("One of these must be null");
		
		//check whether the response object is an error, and set fields accordingly
		if (resp != null && resp.isError()) {
			this.ex = resp.getExceptionNoIsErrorCheck();
			this.resp = null;
		}
		else {
			this.ex = ex;
			this.resp = null; //since resp is null, explicity set it here
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
