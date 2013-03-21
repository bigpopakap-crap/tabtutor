package controllers;

import play.mvc.Result;

/**
 * This class handles routes that are related to creating and logging in as
 * test users who are not associated with Facebook accounts
 * 
 * @author bigpopakap
 * @since 2013-03-21
 *
 */
public class DevtoolsTestUsersWebController extends DevtoolsWebController {
	
	/** Lists the test users on a page */
	public static Result list() {
		//TODO do this
		return ok();
	}
	
	/** Creates a new test user with the given first and last name */
	public static Result create(String firstName, String lastName) {
		//TODO do this
		return ok();
	}
	
	/** Logs in as the test user with the given ID and redirects to the given url */
	public static Result login(String id, String redirect) {
		//TODO do this
		return ok();
	}

}
