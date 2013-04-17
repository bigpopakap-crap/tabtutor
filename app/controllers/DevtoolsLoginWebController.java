package controllers;

import models.UserModel;
import play.mvc.Result;
import actions.ActionAnnotations.Sessioned;
import contexts.SessionContext;

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
	public static Result create(String username, String email) {
		//create a user with the given username and email
		//since this is internal, don't really need to do validation here
		UserModel.createAndSave(null, username, email);
		return redirect(routes.DevtoolsLoginWebController.listUsers());
	}
	
	/** Logs in as the test user with the given ID and redirects to the given url */
	//TODO make this use the POST method
	@Sessioned(forceRefresh = true)
	public static Result login(String pk, String targetUrl) {
		//establish the context and then redirect to the homepage
		UserModel user = UserModel.getById(pk);
		if (user != null) {
			SessionContext.establish(user);
		}
		return redirect(targetUrl);
	}

}