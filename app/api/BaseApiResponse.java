package api;

import exeptions.BaseApiException;

public abstract class BaseApiResponse<T extends BaseApiException> {
	
	//TODO figure out how to force callers to call the throwIfErroneous method before doing anything else
	
	public abstract boolean isError();
	public abstract T getException();

}
