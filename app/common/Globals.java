package common;

/**
 * This class holds all static final context that should be accessible by the whole app.
 * This differs from AppCtx because these are not meant to be used as environment vars
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-23
 *
 */
public abstract class Globals {
	
	/** The key to use in the cookie for the session ID */
	public static final String SESSION_ID_COOKIE_KEY = "wtfspk";
	
}
