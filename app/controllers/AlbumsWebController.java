package controllers;

import juiforms.JuiFormValidationException;
import models.AlbumModel;
import models.forms.AlbumModelJuiForm;
import play.mvc.Result;

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
