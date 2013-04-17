package controllers;

import play.mvc.Result;
import utils.Logger;
import actions.ActionAnnotations.ModeProtected;

/**
 * This class handles routes (or parents classes that handle routes) which are used as developer tools
 * It is protected to only be available in DEVELOPMENT mode
 * 
 * @author bigpopakap
 * @since 2013-03-21
 *
 */
@ModeProtected
public class DevtoolsWebController extends BaseWebController {
	
	//TODO make this an admin action so it is also accessible in production by priveleged users
	/** Kill the server */
	public static Result killServer() {
		Logger.error("Killing server because the killserver path was queried");
		System.exit(0);
		return null;
	}

}
