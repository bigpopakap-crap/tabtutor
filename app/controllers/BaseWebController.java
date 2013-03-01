package controllers;

import models.SessionModel;
import play.mvc.Controller;
import play.mvc.Result;
import actions.SessionAction.Sessioned;

import common.Globals;


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
	public static Result landing() {
		SessionModel session = SessionModel.Selector.getById(session(Globals.SESSION_ID_COOKIE_KEY));
		return ok(views.html.landing.render(session));
	}

}