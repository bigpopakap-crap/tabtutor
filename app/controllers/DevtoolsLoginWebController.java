package controllers;

import models.UserModel;
import play.mvc.Result;
import actions.ActionAnnotations.Sessioned;

/**
 * This class handles routes that are related to creating and logging in as
 * test users who are not associated with Facebook accounts
 * 
 * @author bigpopakap
 * @since 2013-03-21
 *
 */
public class DevtoolsLoginWebController extends DevtoolsWebController {
	
	/** Lists the test users on a page */
	public static Result listUsers() {
		return ok(views.html.devtools_listUsers.render(UserModel.getAll()));
	}
	
	/** Creates a new test user with the given first and last name */
	@Sessioned(forceRefresh = true)
	public static Result create(String firstName, String lastName) {
		//TODO do this
		return ok();
	}
	
	/** Logs in as the test user with the given ID and redirects to the given url */
	//TODO make this use the POST method
	@Sessioned(forceRefresh = true)
	public static Result login(String pk, String targetUrl) {
		//TODO do this
		return ok();
	}

}