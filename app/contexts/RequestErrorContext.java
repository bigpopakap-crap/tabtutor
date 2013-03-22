package contexts;

import play.mvc.Http.Context;

/**
 * This class can be used to store any errors and error messages for the duration of the
 * request. Note that all the data stored in this class will be lost at the end of the
 * request handling
 * 
 * One use case is for tracking form errors (not yet supported, but can be added when
 * needed). After validation, errors associated with each field can be stored in this class
 * and retrieved from the template
 * 
 * @author bigpopakap
 * @since 2013-03-10
 *
 */
public abstract class RequestErrorContext extends BaseContext {
	
	private static final String FB_CONNECTION_ERROR_CONTEXT_KEY = "fbConnectionErrorContextKey";
	
	/** Returns true if there was an error accessing Facebook for this session context */
	public static synchronized boolean fbConnectionError() {
		//any value set is considered true
		return Context.current().args.get(FB_CONNECTION_ERROR_CONTEXT_KEY) != null;
	}
	
	/** Sets whether or not there was an FB connection error in the request context */
	public static synchronized void setFbConnectionError(boolean b) {
		Context.current().args.put(FB_CONNECTION_ERROR_CONTEXT_KEY, b ? true : null);
	}
	
	//TODO add ability to get form errors
	
}
