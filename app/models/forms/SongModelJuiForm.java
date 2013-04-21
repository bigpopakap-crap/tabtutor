package models.forms;

import java.util.Map;

import juiforms.JuiForm;
import juiforms.JuiFormInput;
import juiforms.JuiFormInputConstraint;
import juiforms.JuiFormInputType;
import models.SongModel;
import play.api.templates.Html;
import types.HttpMethodType;

public class SongModelJuiForm extends JuiForm<SongModel> {
	
	//TODO make convert these hardcoded strings to messages
	
	public SongModelJuiForm() {
		super(new JuiFormInput[] {
			new JuiFormInput(JuiFormInputType.TEXT, "title", "Title", "Freebird", "The title of the song", new JuiFormInputConstraint[] {
				JuiFormInputConstraint.REQUIRED
			})
			//TODO add other fields
		});
	}

	@Override
	protected SongModel bind(Map<String, String> data) {
		return SongModel.createAndSave(
			data.get("title"),
			null, //TODO don't hardcode this
			null, //TODO don't hardcode this
			Integer.parseInt(data.get("trackNum")),
			Boolean.parseBoolean(data.get("isLive")),
			data.get("youtubeId")
		);
	}
	
	public Html render() {
		return super.render("Add a song", "Yuuuuup", HttpMethodType.POST, "/"); //TODO change the action URL
	}
	
}