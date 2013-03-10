package api;

import exeptions.BaseApiException;

public class ApiResponseOption<R extends BaseApiResponse<?>> {
	
	private final R resp;
	private final BaseApiException ex;
	
	public ApiResponseOption(BaseApiException ex) {
		this(null, ex);
	}
	
	public ApiResponseOption(R resp) {
		this(resp, null);
	}
	
	private ApiResponseOption(R resp, BaseApiException ex) {
		//make sure exactly one is non-null
		if (ex == null && resp == null) throw new IllegalArgumentException("Both of these cannot be null");
		if (ex != null && resp != null) throw new IllegalArgumentException("One of these must be null");
		
		//check whether the response object is an error, and set fields accordingly
		if (resp != null && resp.isError()) {
			this.ex = resp.getException();
			this.resp = null;
		}
		else {
			this.ex = ex;
			this.resp = null; //since resp is null, explicity set it here
		}
	}
	
	public R get() throws BaseApiException {
		if (ex != null) throw ex;
		else return resp;
	}

}
