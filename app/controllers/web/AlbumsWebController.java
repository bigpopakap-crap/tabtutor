package controllers.web;

import juiforms.JuiFormValidationException;
import models.AlbumModel;
import models.forms.AlbumForm;
import oops.NotFoundOops;
import play.Logger;
import play.mvc.Result;
import utils.EscapingUtil;
import utils.EscapingUtil.Escaper;
import controllers.web.base.BaseWebController;

/**
 * Controller for all things related so albums: listing, creating, modifying, etc.
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class AlbumsWebController extends BaseWebController {
	
	/** Show the album list page */
	public static Result listPage() {
		return list(new AlbumForm());
	}
	
	/** Show the album detail page */
	public static Result detailPage(String pk, String title) {
		//check that the title is the correct one for that pk
		AlbumModel album = AlbumModel.getByPk(pk);
		if (album == null) {
			throw new NotFoundOops(null);
		}
		else if (!EscapingUtil.escape(album.getTitle(), Escaper.URL_DESCRIPTIVE_PARAM).equals(title)) { //this takes care of null title
			return redirect(detailPageUrl(album));
		}
		
		return ok("Detail page for album " + album);
	}
	
	/** Show the album list page after creating the album */
	public static Result create() {
		AlbumForm albumModelForm = new AlbumForm();
		try {
			albumModelForm.bind();
			return redirect(controllers.web.routes.AlbumsWebController.listPage());
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
	public static String detailPageUrl(AlbumModel album) {
		if (album != null) {
			return controllers.web.routes.AlbumsWebController.detailPage(
				album.getPk().toString(),
				EscapingUtil.escape(album.getTitle(), Escaper.URL_DESCRIPTIVE_PARAM)
			).url();
		}
		else {
			//don't throw exception, simply return a path that will not work
			Logger.warn("Called with null argument", new RuntimeException("album cannot be null"));
			return controllers.web.routes.SimpleWebController.notFoundPage(null).url();
		}
	}
	
	/* **************************************************************************
	 *  PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Displays the list of songs using the given form object */
	private static Result list(AlbumForm albumModelForm) {
		return ok(views.html.pages.albumList.render(
			AlbumModel.getAll(),
			albumModelForm
		));
	}

}
