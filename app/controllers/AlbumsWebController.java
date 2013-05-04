package controllers;

import controllers.base.BaseWebController;
import juiforms.JuiFormValidationException;
import models.AlbumModel;
import models.forms.AlbumModelJuiForm;
import oops.NotFoundOops;
import play.Logger;
import play.mvc.Result;
import utils.EscapingUtil;
import utils.EscapingUtil.Escaper;

/**
 * Controller for all things related so albums: listing, creating, modifying, etc.
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class AlbumsWebController extends BaseWebController {
	
	/** Show the album list page */
	public static Result list() {
		return list(new AlbumModelJuiForm());
	}
	
	/** Show the album detail page */
	public static Result detail(String pk, String title) {
		//check that the title is the correct one for that pk
		AlbumModel album = AlbumModel.getByPk(pk);
		if (album == null) {
			throw new NotFoundOops(null);
		}
		else if (!EscapingUtil.escape(album.getTitle(), Escaper.URL_DESCRIPTIVE_PARAM).equals(title)) { //this takes care of null title
			return redirect(detailUrl(album));
		}
		
		return ok("Detail page for album " + album);
	}
	
	/** Show the album list page after creating the album */
	public static Result create() {
		AlbumModelJuiForm albumModelForm = new AlbumModelJuiForm();
		try {
			albumModelForm.bind();
			return redirect(routes.AlbumsWebController.list());
		}
		catch (JuiFormValidationException ex) {
			return list(albumModelForm);
		}
	}
	
	/* **************************************************************************
	 *  PUBLIC HELPERS
	 ************************************************************************** */
	
	/** Gets the URL of the detail page for this album. This is the best way of getting
	 *  the detail URL because it will populate the correct title */
	public static String detailUrl(AlbumModel album) {
		if (album != null) {
			return routes.AlbumsWebController.detail(
				album.getPk().toString(),
				EscapingUtil.escape(album.getTitle(), Escaper.URL_DESCRIPTIVE_PARAM)
			).url();
		}
		else {
			//don't throw exception, simply return a path that will not work
			Logger.warn("Called with null argument", new RuntimeException("album cannot be null"));
			return routes.SimpleWebController.pageNotFound(null).url();
		}
	}
	
	/* **************************************************************************
	 *  PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Displays the list of songs using the given form object */
	private static Result list(AlbumModelJuiForm albumModelForm) {
		return ok(views.html.albumList.render(
			AlbumModel.getAll(),
			albumModelForm
		));
	}

}