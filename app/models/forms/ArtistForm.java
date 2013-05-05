package models.forms;

import helpers.Message;

import java.util.Map;

import controllers.routes;

import juiforms.JuiForm;
import juiforms.JuiFormInput;
import juiforms.JuiFormInputConstraint;
import juiforms.JuiFormInputType;
import models.ArtistModel;
import play.api.templates.Html;
import types.HttpMethodType;

/**
 * JUI form for creating a new artist
 * 
 * @author bigpopakap
 * @since 2013-04-26
 *
 */
public class ArtistForm extends JuiForm<ArtistModel> {
	
	private static final String NAME_INPUT_NAME = "name";

	public ArtistForm() {
		super(new JuiFormInput[] {
			//artist name
			new JuiFormInput(
				JuiFormInputType.TEXT, NAME_INPUT_NAME, Message.formInput_artistName_label,
				Message.formInput_artistName_placeholder, Message.formInput_artistName_helpText, true,
				new JuiFormInputConstraint[] {
					JuiFormInputConstraint.REQUIRED
				}
			)
		});
	}

	@Override
	protected ArtistModel bind(Map<String, String> data) {
		return ArtistModel.createAndSave(data.get(NAME_INPUT_NAME));
	}

	//TODO convert these hardcoded strings to messages
	@Override
	public Html render() {
		return super.render(
			"Create new artist",
			"spring breeeeaaakk",
			HttpMethodType.POST,
			controllers.web.routes.ArtistsWebController.create().url()
		);
	}

}
