package contexts;

import play.mvc.Http.Context;

public abstract class ErrorContext {
	
	private static final String FB_CONNECTION_ERROR_CONTEXT_KEY = "fbConnectionErrorContextKey";
	
	/** Returns true if there was an error accessing Facebook for this session context */
	public static boolean fbConnectionError() {
		//any value set is considered true
		return Context.current().args.get(FB_CONNECTION_ERROR_CONTEXT_KEY) != null;
	}
	public static void setFbConnectionError(boolean b) {
		Context.current().args.put(FB_CONNECTION_ERROR_CONTEXT_KEY, b ? true : null);
	}
	
	//TODO add ability to get form errors
	
}
