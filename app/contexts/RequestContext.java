package contexts;

import java.util.Map;

import play.data.DynamicForm;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import types.HttpMethodType;
import utils.RestUtil;
import contexts.base.BaseContext;
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
	public static synchronized Request request() {
		return Context.current().request();
	}
	
	/** Gets the method type of this request */
	public static synchronized HttpMethodType method() {
		//will throw IllegalArgumentException if it's not a value of the enum
		return HttpMethodType.valueOf(request().method());
	}
	
	/** Determines if this request is through the API or not */
	public static synchronized boolean isApi() {
		//TODO actually implement this
		return false;
	}
	
	/** Determines if this request is through the web interface or not */
	public static synchronized boolean isWeb() {
		return !isApi();
	}
	
	/** Gets the url requested */
	public static synchronized String url() {
		return Context.current().request().path();
	}
	
	/** Gets the query params map, ignoring any arrays of values (just takes the first) */
	public static synchronized Map<String, String> paramsMap() {
		//TODO cache the result of this method
		HttpMethodType method = method();
		switch (method) {
			case GET:
				return RestUtil.arrayMapToMap(request().queryString());
			case POST:
				DynamicForm form = DynamicForm.form().bindFromRequest(request());
				return form.data();
			default:
				throw new IllegalStateException("Uhandled method type: " + method);
		}
	}
	
	/** Gets the query params as a string (with no leading ?) */
	public static synchronized String paramsString() {
		return RestUtil.mapToQueryString(paramsMap());
	}
	
	/** Gets the login url that will redirect back to this page
	 *  (as opposed to the url of the Facebook login dialogue, this is the one that initiates it) */
	public static synchronized String loginUrl() {
		return routes.AuthWebController.fblogin(null, null, url()).url();
	}
	
	/** Gets the logout url that will redirect back to this page */
	public static synchronized String logoutUrl() {
		return routes.AuthWebController.logout(url()).url();
	}
	
	/** Gets the login url for a test user */
	public static synchronized String devtoolsLoginUrl(String pk) {
		return routes.DevtoolsLoginWebController.login(pk, url()).url();
	}

}
