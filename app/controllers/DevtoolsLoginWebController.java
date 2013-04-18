package controllers;

import juiforms.JuiFormValidationException;
import models.UserModel;
import models.forms.DevtoolsUserJuiForm;
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
		return listUsers(new DevtoolsUserJuiForm());
	}
	
	/** Creates a new test user with the given first and last name */
	public static Result create(String username, String email) {
		//use a form object to validate and create a user from this data
		DevtoolsUserJuiForm devtoolsUserForm = new DevtoolsUserJuiForm();
		try {
			devtoolsUserForm.bind();
			return redirect(routes.DevtoolsLoginWebController.listUsers());
		} catch (JuiFormValidationException ex) {
			return listUsers(devtoolsUserForm);
		}
		
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
	
	/* **************************************************************************
	 *  PRIVATE HELPERS
	 ************************************************************************** */
	
	private static Result listUsers(DevtoolsUserJuiForm devtoolsUserForm) {
		return ok(views.html.devtools_listUsers.render(
			UserModel.getAll(),
			devtoolsUserForm
		));
	}

}