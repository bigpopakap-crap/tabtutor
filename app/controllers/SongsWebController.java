package controllers;

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
		return ok(views.html.songList.render(
			SongModel.getAll(),
			new SongModelJuiForm()
		));
	}
	
	/** Show the song list page after creating the song */
	public static Result create() {
		return null;
	}
	
}