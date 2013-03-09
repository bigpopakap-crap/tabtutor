package api;

import exeptions.BaseApiException;

public abstract class BaseApiResponse {
	
	//TODO figure out how to force callers to call the throwIfErroneous method before doing anything else
	
	private BaseApiException ex;
	
	public BaseApiResponse(BaseApiException ex) {
		setError(ex);
	}
	
	public void setError(BaseApiException ex) {
		this.ex = ex;
	}
	
	public void throwIfErroneous() throws BaseApiException {
		if (ex != null) throw ex;
	}

}
