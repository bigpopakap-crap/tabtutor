package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * This class will route all pages that the user will see as they navigate through the site,
 * or parent all classes that do so for specific related workflows
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-17
 *
 */
public class BaseWebController extends Controller {
	
	/** Redirect to the favicon */
	public static Result favicon() {
		return redirect(routes.Assets.at("/public/images/favicon.png"));
	}
	
	/** Show the landing page */
	public static Result landing() {
		return ok(views.html.landing.render());
	}

}