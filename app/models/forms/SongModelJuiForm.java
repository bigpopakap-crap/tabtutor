package models.forms;

import java.util.Map;

import juiforms.JuiForm;
import juiforms.JuiFormInput;
import juiforms.JuiFormInputConstraint;
import juiforms.JuiFormInputType;
import models.ArtistModel;
import models.SongModel;
import play.api.templates.Html;
import types.HttpMethodType;
import utils.MessagesEnum;
import controllers.routes;

public class SongModelJuiForm extends JuiForm<SongModel> {
	
	private static final String TITLE_INPUT_NAME = "title";
	
	public SongModelJuiForm() {
		super(new JuiFormInput[] {
			new JuiFormInput(
				JuiFormInputType.TEXT, TITLE_INPUT_NAME, MessagesEnum.formInput_songTitle_label,
				MessagesEnum.formInput_songTitle_placeholder, MessagesEnum.formInput_songTitle_helpText, true,
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
	public Html render() {
		return super.render("Add a song", "Yuuuuup", HttpMethodType.POST, routes.SongsWebController.create().url());
	}
	
}