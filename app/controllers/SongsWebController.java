package controllers;

import juiforms.JuiFormValidationException;
import models.SongModel;
import models.forms.SongModelJuiForm;
import play.mvc.Result;

/**
 * Controller for all things related so songs: listing, creating, modifying, etc.
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class SongsWebController extends BaseWebController {
	
	/** Show the song list page */
	public static Result list() {
		return list(new SongModelJuiForm());
	}
	
	/** Show the song list page after creating the song */
	public static Result create() {
		SongModelJuiForm songModelForm = new SongModelJuiForm();
		try {
			songModelForm.bind();
			return redirect(routes.SongsWebController.list());
		}
		catch (JuiFormValidationException ex) {
			return list(songModelForm);
		}
	}
	
	/* **************************************************************************
	 *  PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Displays the list of songs using the given form object */
	private static Result list(SongModelJuiForm songModelForm) {
		return ok(views.html.songList.render(
			SongModel.getAll(),
			songModelForm
		));
	}
	
}