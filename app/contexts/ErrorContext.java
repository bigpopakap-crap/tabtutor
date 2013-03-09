package contexts;

import play.mvc.Http.Context;

public abstract class ErrorContext {
	
	private static final String FB_CONNECT_ERROR_CONTEXT_KEY = "fbConnectErrorContextKey";
	
	/** Returns true if there was an error accessing Facebook for this session context */
	public static boolean fbConnectError() {
		//any value set is considered true
		return Context.current().args.get(FB_CONNECT_ERROR_CONTEXT_KEY) != null;
	}
	public static void setFbConnectError(boolean b) {
		Context.current().args.put(FB_CONNECT_ERROR_CONTEXT_KEY, b ? true : null);
	}

}
