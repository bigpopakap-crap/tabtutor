package controllers;

import juiforms.JuiFormValidationException;
import models.ArtistModel;
import models.forms.ArtistModelJuiForm;
import play.Logger;
import play.mvc.Result;
import controllers.exceptions.web.NotFoundErrorPageException;

/**
 * Controller for all things related so artists: listing, creating, modifying, etc.
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public class ArtistsWebController extends BaseWebController {
	
	/** Show the artist list page */
	public static Result list() {
		return list(new ArtistModelJuiForm());
	}
	
	/** Show the artist detail page */
	public static Result detail(String pk, String name) {
		//check that the title is the correct one for that pk
		ArtistModel artist = ArtistModel.getByPk(pk);
		if (artist == null) {
			throw new NotFoundErrorPageException(null);
		}
		//TODO don't hardcode the replaceAll
		else if (!artist.getName().replaceAll("\\s", "-").equals(name)) { //this takes care of null title
			return redirect(detailUrl(artist));
		}
		
		return ok("Detail page for artist " + artist);
	}
	
	/** Show the artist list page after creating the artist */
	public static Result create() {
		ArtistModelJuiForm artistModelForm = new ArtistModelJuiForm();
		try {
			artistModelForm.bind();
			return redirect(routes.ArtistsWebController.list());
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
			//TODO don't hardcode the replaceAll
			return routes.ArtistsWebController.detail(artist.getPk().toString(), artist.getName().replaceAll("\\s", "-")).url();
		}
		else {
			//don't throw exception, simply return a path that will not work
			Logger.warn("Called with null argument", new RuntimeException("artist cannot be null"));
			return routes.SimpleWebController.pageNotFound(null).url();
		}
	}
	
	/* **************************************************************************
	 *  PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Displays the list of songs using the given form object */
	private static Result list(ArtistModelJuiForm artistModelForm) {
		return ok(views.html.artistList.render(
			ArtistModel.getAll(),
			artistModelForm
		));
	}

}
