package controllers;

import models.AlbumModel;
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
		return ok(views.html.albumList.render(
			AlbumModel.getAll())
		);
	}

}
