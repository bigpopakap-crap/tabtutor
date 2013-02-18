package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * This class will route all pages that the user will see as they navigate through the site
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-17
 *
 */
public class PageController extends Controller {
	
	public static Result landing() {
		return ok(views.html.landing.render());
	}

}
