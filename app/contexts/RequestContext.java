package contexts;

import play.mvc.Http.Context;
import controllers.routes;

/**
 * Gets info about the current request
 * 
 * @author bigpopakap
 * @since 2013-04-06
 *
 */
public class RequestContext extends BaseContext {
	
	/** Gets the url requested */
	public static String url() {
		return Context.current().request().path();
	}
	
	/** Gets the Facebook login url that will redirect back to this page */
	public static String fbloginUrl() {
		return routes.FbAuthWebController.fblogin(null, null, url()).url();
	}

}
