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
import controllers.routes;

public class SongModelJuiForm extends JuiForm<SongModel> {
	
	//TODO make convert these hardcoded strings to messages
	
	public SongModelJuiForm() {
		super(new JuiFormInput[] {
			new JuiFormInput(JuiFormInputType.TEXT, "title", "Title", "Freebird", "The title of the song", true, new JuiFormInputConstraint[] {
				JuiFormInputConstraint.REQUIRED
			})
			//TODO add other fields
		});
	}

	@Override
	protected SongModel bind(Map<String, String> data) {
		return SongModel.createAndSave(
			data.get("title"),
			ArtistModel.createAndSave("Artist " + System.currentTimeMillis()), //TODO don't hardcode this
			null, //TODO don't hardcode this
			0, //TODO data.get("trackNum") != null ? Integer.parseInt(data.get("trackNum")) : null,
			false, //TODO data.get("isLive") != null ? Boolean.parseBoolean(data.get("isLive")) : null,
			data.get("youtubeId")
		);
	}
	
	public Html render() {
		return super.render("Add a song", "Yuuuuup", HttpMethodType.POST, routes.SongsWebController.create().url());
	}
	
}