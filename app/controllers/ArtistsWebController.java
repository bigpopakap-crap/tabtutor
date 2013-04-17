package controllers;

import models.ArtistModel;
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
		return ok(views.html.artistList.render(
			ArtistModel.getAll())
		);
	}

}
