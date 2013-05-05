package controllers.web;

import juiforms.JuiFormValidationException;
import models.SongModel;
import models.forms.SongForm;
import oops.NotFoundOops;
import play.Logger;
import play.mvc.Result;
import utils.EscapingUtil;
import utils.EscapingUtil.Escaper;
import controllers.web.base.BaseWebController;

/**
 * Controller for all things related so songs: listing, creating, modifying, etc.
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class SongsWebController extends BaseWebController {
	
	/** Show the song list page */
	public static Result listPage() {
		return list(new SongForm());
	}
	
	/** Show the song detail page */
	public static Result detailPage(String pk, String title) {
		//check that the title is the correct one for that pk
		SongModel song = SongModel.getByPk(pk);
		if (song == null) {
			throw new NotFoundOops(null);
		}
		else if (!EscapingUtil.escape(song.getTitle(), Escaper.URL_DESCRIPTIVE_PARAM).equals(title)) { //this takes care of null title
			return redirect(detailUrl(song));
		}
		
		return ok("Detail page for song " + song);
	}
	
	/** Show the edit page */
	public static Result editPage(String pk, String title) {
		return null; //TODO
	}
	
	/** Show the song list page after creating the song */
	public static Result create() {
		SongForm songModelForm = new SongForm();
		try {
			songModelForm.bind();
			return redirect(controllers.web.routes.SongsWebController.listPage());
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
			return controllers.web.routes.SongsWebController.detailPage(
				song.getPk().toString(),
				EscapingUtil.escape(song.getTitle(), Escaper.URL_DESCRIPTIVE_PARAM)
			).url();
		}
		else {
			//don't throw exception, simply return a path that will not work
			Logger.warn("Called with null argument", new RuntimeException("song cannot be null"));
			return controllers.web.routes.SimpleWebController.notFoundPage(null).url();
		}
	}
	
	/* **************************************************************************
	 *  PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Displays the list of songs using the given form object */
	private static Result list(SongForm songModelForm) {
		return ok(views.html.pages.songList.render(
			SongModel.getAll(),
			songModelForm
		));
	}
	
}