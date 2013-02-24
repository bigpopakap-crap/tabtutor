package common;

/**
 * This class holds all static final context that should be accessible by the whole app.
 * This differs from AppCtx because these are not meant to be used as environment vars
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-23
 *
 */
public class Globals {
	
	/** Any variables or methods defined in a global namespace in Javascript
	 	should be declared under this variable */
	public static final String JS_APP_NAMESPACE = "window.wtf_namespace";

}
