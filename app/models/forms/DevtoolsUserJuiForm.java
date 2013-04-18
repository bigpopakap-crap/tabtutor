package models.forms;

import java.util.Map;

import juiforms.CustomValidator;
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
				JuiFormInputConstraint.REQUIRED,
				JuiFormInputConstraint.UNIQUE(new CustomValidator<String, Boolean>() {
					
					@Override
					public Boolean call() throws Exception {
						return !UserModel.isValidExistingUsername(value());
					}

				})
			}),
			new JuiFormInput(JuiFormInputType.EMAIL, "email", "Email", "jonsmellypants@gmail.com", "Enter the email", new JuiFormInputConstraint[] {
				JuiFormInputConstraint.REQUIRED
			})
		});
	}

	@Override
	protected UserModel bind(Map<String, String> data) {
		return UserModel.createAndSave(null, data.get("username"), data.get("email"));
	}
	
	public Html render() {
		return super.render("Create a new user", "Bitchin'", HttpMethodType.GET, routes.DevtoolsLoginWebController.create("", "").url());
	}

}
