package controllers;

import java.net.URLEncoder;

import play.mvc.Result;

import common.AppCtx;

/**
 * This class handles all API requests related to Facebook authentication
 * 
 * Information about the server-side Facebook login:
 * https://developers.facebook.com/docs/concepts/login/login-architecture/
 * https://developers.facebook.com/docs/howtos/login/server-side-login/
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-17
 *
 */
public class FbAuthWebController extends BaseWebController {
	
	//TODO test the registration/login flow
	//TODO add a redirectUrl parameter so that a user gets back to whatever page they were viewing
	
	/**
	 * TODO handle various cases:
	 * 		- the user de-authorized the app
	 * 		- the auth token from Facebook expired
	 */
	
	/**
	 * Handles the Facebook login. Redirects the user to the Facebook login dialogue,
	 * or exchange the code for an access token
	 * @param code the code returned from Facebook. If null, redirect the user to the login dialogue
	 * @return
	 */
	public static Result fblogin(String code) {
		if (code == null) {
			//the login flow has started, redirect to the Facebook login dialogue
			//TODO use something else for url encoding
			return redirect(
						"https://www.facebook.com/dialog/oauth" +
						"?client_id=" + AppCtx.Var.FB_APP_ID.val() +
						"&redirect_uri=" + URLEncoder.encode(routes.FbAuthWebController.fblogin(null).absoluteURL(request()))
						//TODO add CSRF protection
					);
		}
		else {
			//TODO handle the case that they did not authorize the app
			return null;
		}
	}

}
