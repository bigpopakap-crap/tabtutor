package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import actions.FbAuthAction.FbAuthed;
import actions.SessionAction.Sessioned;


/**
 * This class will route all pages that the user will see as they navigate through the site,
 * or parent all classes that do so for specific related workflows
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-17
 *
 */
@Sessioned //this is important to make sure all web requests enforce the creation of a session
public class BaseWebController extends Controller {
	
	/** Show the landing page */
	@FbAuthed //TODO remove this. this is only here to test that Action
	public static Result landing() {
		return ok(views.html.landing.render());
	}

}