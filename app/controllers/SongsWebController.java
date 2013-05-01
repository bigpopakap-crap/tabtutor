package controllers;

import juiforms.JuiFormValidationException;
import models.SongModel;
import models.forms.SongModelJuiForm;
import play.Logger;
import play.mvc.Result;
import controllers.exceptions.web.NotFoundErrorPageException;

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
	
	/** Redirect to the song detail page with the correct title */
	public static Result detailRedirect(String pk) {
		//ensure that such a song exists and redirect to the url including the title
		SongModel song = SongModel.getByPk(pk);
		if (song == null) {
			throw new NotFoundErrorPageException(null);
		}
		return redirect(detailUrl(song));
	}
	
	/** Show the song detail page */
	public static Result detail(String pk, String title) {
		//check that the title is the correct one for that pk
		SongModel song = SongModel.getByPk(pk);
		if (song == null) {
			throw new NotFoundErrorPageException(null);
		}
		//TODO don't hardcode the replaceAll
		else if (!song.getTitle().replaceAll("\\s", "-").equals(title)) {
			return redirect(detailUrl(song));
		}
		
		return ok("Detail page for song " + song);
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
	 *  PUBLIC HELPERS
	 ************************************************************************** */
	
	/** Gets the URL of the detail page for this song. This is the best way of getting
	 *  the detail URL because it will populate the correct title */
	public static String detailUrl(SongModel song) {
		if (song != null) {
			//TODO don't hardcode the replaceAll
			return routes.SongsWebController.detail(song.getPk().toString(), song.getTitle().replaceAll("\\s", "-")).url();
		}
		else {
			//don't throw exception, simply return a path that will not work
			Logger.warn("Called with null argument", new RuntimeException("song cannot be null"));
			return routes.SimpleWebController.pageNotFound(null).url();
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