package controllers;

import play.mvc.Result;
import controllers.exceptions.web.NotFoundErrorPageException;

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
		return ok(views.html.landing.render());
	}
	
	/** Show the error page for when no other page was found */
	public static Result pageNotFound(String path) {
		//do nothing with the path, just throw a page not found error page exception
		throw new NotFoundErrorPageException(null);
	}
	
}
