package contexts;

import java.util.Map;

import play.mvc.Http.Context;
import play.mvc.Http.Request;
import utils.RestUtil;
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
	public static Request request() {
		return Context.current().request();
	}
	
	/** Gets the url requested */
	public static String url() {
		return Context.current().request().path();
	}
	
	/** Gets the query params map, ignoring any arrays of values (just takes the first) */
	public static Map<String, String> queryParams() {
		//TODO cache the result of this method
		return RestUtil.arrayMapToMap(request().queryString());
	}
	
	/** Gets the login url that will redirect back to this page
	 *  (as opposed to the url of the Facebook login dialogue, this is the one that initiates it) */
	public static String loginUrl() {
		return routes.FbLoginWebController.fblogin(null, null, url()).url();
	}
	
	/** Gets the logout url that will redirect back to this page */
	public static String logoutUrl() {
		//TODO do this
		return "#";
	}
	
	/** Gets the login url for a test user */
	public static String devtoolsLoginUrl(String pk) {
		return routes.DevtoolsLoginWebController.login(pk, url()).url();
	}

}
