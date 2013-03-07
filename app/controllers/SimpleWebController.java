package controllers;

import play.mvc.Result;

/**
 * This class will route all pages that the user will see as they navigate through the site,
 * with minimal computation
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-06
 *
 */
public class SimpleWebController extends BaseWebController {

	/** Show the landing page */
	public static Result landing() {
		return ok(views.html.landing.render());
	}
	
}
