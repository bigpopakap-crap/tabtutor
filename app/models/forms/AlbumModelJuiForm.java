package models.forms;

import helpers.Message;

import java.util.Map;

import juiforms.JuiForm;
import juiforms.JuiFormInput;
import juiforms.JuiFormInputConstraint;
import juiforms.JuiFormInputType;
import models.AlbumModel;
import play.api.templates.Html;
import types.HttpMethodType;
import controllers.routes;

/**
 * JUI form for creating a new album
 * 
 * @author bigpopakap
 * @since 2013-04-26
 *
 */
public class AlbumModelJuiForm extends JuiForm<AlbumModel> {
	
	private static final String TITLE_INPUT_NAME = "title";

	public AlbumModelJuiForm() {
		super(new JuiFormInput[] {
			//album title
			new JuiFormInput(
				JuiFormInputType.TEXT, TITLE_INPUT_NAME, Message.formInput_albumTitle_label,
				Message.formInput_albumTitle_placeholder, Message.formInput_albumTitle_helpText, true,
				new JuiFormInputConstraint[] {
					JuiFormInputConstraint.REQUIRED
				}
			)
		});
		//TODO add other fields
	}

	@Override
	protected AlbumModel bind(Map<String, String> data) {
		return AlbumModel.createAndSave(data.get(TITLE_INPUT_NAME));
	}

	//TODO convert these hardcoded strings to messages
	@Override
	public Html render() {
		return super.render(
			"Create an album",
			"tru dat",
			HttpMethodType.POST,
			controllers.web.routes.AlbumsWebController.create().url()
		);
	}

}
