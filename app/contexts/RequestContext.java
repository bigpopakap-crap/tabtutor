package contexts;

import play.mvc.Http.Context;
import play.mvc.Http.Request;
import controllers.routes;

/**
 * Gets info about the current request
 * 
 * @author bigpopakap
 * @since 2013-04-06
 *
 */
public class RequestContext extends BaseContext {
	
	/** Gets the current request object */
	public static Request get() {
		return Context.current().request();
	}
	
	/** Gets the url requested */
	public static String url() {
		return Context.current().request().path();
	}
	
	/** Gets the login url that will redirect back to this page
	 *  (as opposed to the url of the Facebook login dialogue, this is the one that initiates it) */
	public static String loginUrl() {
		return routes.FbAuthWebController.fblogin(null, null, url()).url();
	}
	
	/** Gets the login url for a test user */
	public static String devtoolsLoginUrl(String pk) {
		return routes.DevtoolsLoginWebController.login(pk, url()).url();
	}

}
