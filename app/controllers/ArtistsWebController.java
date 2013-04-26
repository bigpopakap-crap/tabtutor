package controllers;

import juiforms.JuiFormValidationException;
import models.ArtistModel;
import models.forms.ArtistModelJuiForm;
import play.mvc.Result;

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
