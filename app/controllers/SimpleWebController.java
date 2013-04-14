package controllers;

import models.AlbumModel;
import models.ArtistModel;
import models.SongModel;
import play.mvc.Result;

/**
 * This class will route all pages that the user will see as they navigate through the site,
 * with minimal computation
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public class SimpleWebController extends BaseWebController {

	/** Show the landing page */
	public static Result landing() {
		return ok(views.html.landing.render());
	}
	
	/** Show the song list page */
	public static Result songs() {
		return ok(views.html.songList.render(
			SongModel.getAll())
		);
	}
	
	/** Show the artist list page */
	public static Result artists() {
		return ok(views.html.artistList.render(
			ArtistModel.getAll())
		);
	}
	
	/** Show the album list page */
	public static Result albums() {
		return ok(views.html.albumList.render(
			AlbumModel.getAll())
		);
	}
	
	/** Show the error page for when no other page was found */
	public static Result pageNotFound(String path) {
		//do nothing with the path, just throw a page not found error page exception
		throw new NotFoundErrorPageException(null);
	}
	
}
