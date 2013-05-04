package controllers.web;

import oops.NotFoundOops;
import play.mvc.Result;
import utils.StringUtil;
import contexts.RequestContext;
import controllers.web.base.BaseWebController;

/**
 * This class will route all pages that the user will see as they navigate through the site,
 * with minimal computation
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public class SimpleWebController extends BaseWebController {

	/** Show the landing page */
	public static Result landing() {
		return ok(views.html.pages.landing.render());
	}
	
	/** Show the error page for when no other page was found */
	public static Result pageNotFound(String path) {
		//do nothing with the path, just throw a page not found error page exception
		throw new NotFoundOops(null);
	}
	
	/** Redirect to the path without the trailing slash */
	public static Result untrail(String path) {
		String params = RequestContext.paramsString();
		return movedPermanently("/" + path + (StringUtil.isNullOrEmpty(params) ? "" : "?" + params));
	}
	
}
