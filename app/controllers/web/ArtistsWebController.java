package controllers.web;

import juiforms.JuiFormValidationException;
import models.ArtistModel;
import models.forms.ArtistForm;
import oops.NotFoundOops;
import play.Logger;
import play.mvc.Result;
import utils.EscapingUtil;
import utils.EscapingUtil.Escaper;
import controllers.web.base.BaseWebController;

/**
 * Controller for all things related so artists: listing, creating, modifying, etc.
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class ArtistsWebController extends BaseWebController {
	
	/** Show the artist list page */
	public static Result listPage() {
		return list(new ArtistForm());
	}
	
	/** Show the artist detail page */
	public static Result detailPage(String pk, String name) {
		//check that the title is the correct one for that pk
		ArtistModel artist = ArtistModel.getByPk(pk);
		if (artist == null) {
			throw new NotFoundOops(null);
		}
		else if (!EscapingUtil.escape(artist.getName(), Escaper.URL_DESCRIPTIVE_PARAM).equals(name)) { //this takes care of null name
			return redirect(detailUrl(artist));
		}
		
		return ok("Detail page for artist " + artist);
	}
	
	/** Show the artist list page after creating the artist */
	public static Result create() {
		ArtistForm artistModelForm = new ArtistForm();
		try {
			artistModelForm.bind();
			return redirect(controllers.web.routes.ArtistsWebController.listPage());
		}
		catch (JuiFormValidationException ex) {
			return list(artistModelForm);
		}
	}
	
	/* **************************************************************************
	 *  PUBLIC HELPERS
	 ************************************************************************** */
	
	/** Gets the URL of the detail page for this artist. This is the best way of getting
	 *  the detail URL because it will populate the correct name */
	public static String detailUrl(ArtistModel artist) {
		if (artist != null) {
			return controllers.web.routes.ArtistsWebController.detailPage(
				artist.getPk().toString(),
				EscapingUtil.escape(artist.getName(), Escaper.URL_DESCRIPTIVE_PARAM)
			).url();
		}
		else {
			//don't throw exception, simply return a path that will not work
			Logger.warn("Called with null argument", new RuntimeException("artist cannot be null"));
			return controllers.web.routes.SimpleWebController.notFoundPage(null).url();
		}
	}
	
	/* **************************************************************************
	 *  PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Displays the list of songs using the given form object */
	private static Result list(ArtistForm artistModelForm) {
		return ok(views.html.pages.artistList.render(
			ArtistModel.getAll(),
			artistModelForm
		));
	}

}
