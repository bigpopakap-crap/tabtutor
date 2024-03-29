package models.forms;

import helpers.Message;

import java.util.Map;

import juiforms.CustomValidator;
import juiforms.JuiForm;
import juiforms.JuiFormInput;
import juiforms.JuiFormInputConstraint;
import juiforms.JuiFormInputType;
import models.UserModel;
import play.api.templates.Html;
import types.HttpMethodType;
import contexts.SessionContext;

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
				JuiFormInputType.TEXT, USERNAME_INPUT_NAME, Message.formInput_username_label,
				Message.formInput_username_placeholder, Message.formInput_username_helpText, true,
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
				JuiFormInputType.EMAIL, EMAIL_INPUT_NAME, Message.formInput_email_label,
				Message.formInput_email_placeholder, Message.formInput_email_helpText, true,
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
		return super.render(
			"Create a new user",
			"Bitchin'",
			HttpMethodType.POST,
			controllers.web.devtools.routes.DevtoolsLoginWebController.create().url()
		);
	}

}
