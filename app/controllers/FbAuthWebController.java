package controllers;

import models.SessionModel;
import models.UserModel;
import play.mvc.Result;
import api.fb.FbApi;
import contexts.SessionContext;

/**
 * This class handles all API requests related to Facebook authentication
 * 
 * Information about the server-side Facebook login:
 * https://developers.facebook.com/docs/concepts/login/login-architecture/
 * https://developers.facebook.com/docs/howtos/login/server-side-login/
 * 
 * @author bigpopakap
 * @since 2013-02-17
 *
 */
public class FbAuthWebController extends BaseWebController {
	
	//TODO add a redirectUrl parameter so that a user gets back to whatever page they were viewing
	//TODO use a pop-up instead of redirecting the whole browser to Facebook
	//TODO add CSRF protection
	//TODO handle the case that they did not authorize the app
	//TODO handle the case that the user deauthorizes the app
	
	/**
	 * Handles the Facebook login. Redirects the user to the Facebook login dialogue,
	 * or exchange the code for an access token
	 * @param code the code returned from Facebook. If null, redirect the user to the login dialogue
	 * @return
	 */
	public static Result fblogin(final String code, String state) {
		if (code == null) {
			//the login flow has started, redirect to the Facebook login dialogue
			return redirect(FbApi.loginRedirect());
		}
		else {
			//Fetch the access token and expiry time
			FbApi fbApi = FbApi.accessToken(code);
			
			//add this information to the session
			SessionModel session = SessionContext.get();
			SessionModel.Updater.setFbAuthInfoAndUpdate(session, fbApi.getToken(), fbApi.getTokenExpiry());
			
			//if there is an associated user, update the login time
			if (SessionContext.hasUser()) {
				UserModel user = SessionContext.user();
				UserModel.Updater.setLoginTimeAndUpdate(user);
			}
			
			//don't get the associated user, that will be taken care of in SecuredActions
			//redirect to the given redirect url, or to the landing page
			return redirect("/");
		}
	}
	
}
