package models.forms;

import helpers.Message;

import java.util.Map;

import juiforms.JuiForm;
import juiforms.JuiFormInput;
import juiforms.JuiFormInputConstraint;
import juiforms.JuiFormInputType;
import models.ArtistModel;
import models.SongModel;
import play.api.templates.Html;
import types.HttpMethodType;

/**
 * JUI form for creating a new song
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class SongForm extends JuiForm<SongModel> {
	
	private static final String TITLE_INPUT_NAME = "title";
	
	public SongForm() {
		super(new JuiFormInput[] {
			//song title field
			new JuiFormInput(
				JuiFormInputType.TEXT, TITLE_INPUT_NAME, Message.formInput_songTitle_label,
				Message.formInput_songTitle_placeholder, Message.formInput_songTitle_helpText, true,
				new JuiFormInputConstraint[] {
					JuiFormInputConstraint.REQUIRED
				}
			)
			//TODO add other fields
		});
	}

	//TODO don't hardcode these values
	@Override
	protected SongModel bind(Map<String, String> data) {
		return SongModel.createAndSave(
			data.get(TITLE_INPUT_NAME),
			ArtistModel.createAndSave("Artist " + System.currentTimeMillis()), //TODO don't hardcode this
			null, //TODO don't hardcode this
			0, //TODO data.get("trackNum") != null ? Integer.parseInt(data.get("trackNum")) : null,
			false, //TODO data.get("isLive") != null ? Boolean.parseBoolean(data.get("isLive")) : null,
			null //TODO data.get("youtubeId")
		);
	}
	
	//TODO convert these hardcoded strings to messages
	@Override
	public Html render() {
		return super.render(
			"Add a song",
			"Yuuuuup",
			HttpMethodType.POST,
			controllers.web.routes.SongsWebController.create().url()
		);
	}
	
}