package models.forms;

import java.util.Map;

import juiforms.JuiForm;
import juiforms.JuiFormInput;
import juiforms.JuiFormInputConstraint;
import juiforms.JuiFormInputType;
import models.UserModel;
import play.api.templates.Html;
import types.HttpMethodType;
import controllers.routes;

/**
 * Form for the devtools page that can create test users
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class DevtoolsUserJuiForm extends JuiForm<UserModel> {
	
	//TODO make convert these hardcoded strings to messages

	public DevtoolsUserJuiForm() {
		super(new JuiFormInput[] {
			new JuiFormInput(JuiFormInputType.TEXT, "username", "Username", "jonsmellypants24", "Enter the username", new JuiFormInputConstraint[] {
				JuiFormInputConstraint.REQUIRED
			}),
			new JuiFormInput(JuiFormInputType.EMAIL, "email", "Email", "jonsmellypants@gmail.com", "Enter the email", new JuiFormInputConstraint[] {
				JuiFormInputConstraint.REQUIRED
			})
		});
	}

	@Override
	protected UserModel bind(Map<String, String> data) {
		throw new UnsupportedOperationException("This form isn't used to bind the data, it's handled in the controller itself");
	}
	
	public Html render() {
		return super.render("Create a new user", "Bitchin'", HttpMethodType.GET, routes.DevtoolsLoginWebController.create("", "").url());
	}

}
