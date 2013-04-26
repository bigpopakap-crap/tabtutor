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
import utils.MessagesEnum;
import contexts.SessionContext;
import controllers.routes;

/**
 * Form for the devtools page that can create test users
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class DevtoolsUserJuiForm extends JuiForm<UserModel> {
	
	private static final String USERNAME_INPUT_NAME = "username";
	private static final String EMAIL_INPUT_NAME = "email";

	public DevtoolsUserJuiForm() {
		super(new JuiFormInput[] {
			new JuiFormInput(
				JuiFormInputType.TEXT, USERNAME_INPUT_NAME, MessagesEnum.formInput_username_label,
				MessagesEnum.formInput_username_placeholder, MessagesEnum.formInput_username_helpText, true,
				new JuiFormInputConstraint[] {
					JuiFormInputConstraint.REQUIRED,
					JuiFormInputConstraint.UNIQUE(new CustomValidator<String, Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return !UserModel.isValidExistingUsername(value());
						}
					})
				}
			),
			new JuiFormInput(
				JuiFormInputType.EMAIL, EMAIL_INPUT_NAME, MessagesEnum.formInput_email_label,
				MessagesEnum.formInput_email_placeholder, MessagesEnum.formInput_email_helpText, true,
				new JuiFormInputConstraint[] {
					JuiFormInputConstraint.REQUIRED
				}
			)
		});
	}
	
	@Override
	protected void hook_preRenderBind(Map<String, String> defaultValues) {
		if (SessionContext.hasUser()) {
			defaultValues.put(EMAIL_INPUT_NAME, SessionContext.user().getEmail());
		}
	}

	@Override
	protected UserModel bind(Map<String, String> data) {
		return UserModel.createAndSave(null, data.get(USERNAME_INPUT_NAME), data.get(EMAIL_INPUT_NAME), true, SessionContext.user());
	}
	
	//TODO convert these hardcoded strings to messages
	public Html render() {
		return super.render("Create a new user", "Bitchin'", HttpMethodType.POST, routes.DevtoolsLoginWebController.create().url());
	}

}
