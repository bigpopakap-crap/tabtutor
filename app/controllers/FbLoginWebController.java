package controllers;

import models.SessionModel;
import models.UserModel;
import play.mvc.Result;
import utils.Logger;
import actions.ActionAnnotations.Sessioned;
import api.ApiNoResponseException;
import api.BaseApiException;
import api.fb.FbApi;
import api.fb.FbJsonResponse;
import contexts.RequestErrorContext;
import contexts.SessionContext;
import controllers.exceptions.web.InternalServerErrorPageException;

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
public class FbLoginWebController extends BaseWebController {
	
	//TODO add a redirectUrl parameter so that a user gets back to whatever page they were viewing
	//TODO use a pop-up instead of redirecting the whole browser to Facebook
	//TODO add CSRF protection
	//TODO handle the case that they did not authorize the app
	//TODO handle the case that the user deauthorizes the app
	
	/**
	 * Handles the Facebook login. Redirects the user to the Facebook login dialogue,
	 * or exchange the code for an access token
	 * @param code the code returned from Facebook. If null, redirect the user to the login dialogue
	 * @param state the CSRF token
	 * @param targetUrl the url to redirect after login
	 */
	@Sessioned
	public static Result fblogin(final String code, String state, String targetUrl) {
		if (targetUrl == null) targetUrl = "/";
		
		if (code == null) {
			//the login flow has started, redirect to the Facebook login dialogue
			String redirect = FbApi.fbLoginUrl(request(), targetUrl);
			Logger.debug("Redirecting to " + redirect);
			return redirect(redirect);
		}
		else {
			try {
				//Fetch the access token and expiry time
				FbApi fbApi = FbApi.accessToken(request(), targetUrl, code);
				
				//add this information to the session
				SessionModel session = SessionContext.session();
				session.setFbAuthInfoAndUpdate(fbApi.getToken(), fbApi.getTokenExpiry());
				
				//start by getting the user's Facebook ID from the Facebook API
				try {
					//get the response from Facebook and the important fields
					FbJsonResponse fbJson = fbApi.me().get();
					String fbId = fbJson.fbId();
					String fbUsername = fbJson.username();
					String email = fbJson.email();
					
					//get the user associated with this Facebook ID, or create one
					UserModel user = UserModel.getByFbId(fbId);
					if (user == null) {
						user = UserModel.createAndSave(fbId, fbUsername, email);
					}
					
					//add this user ID to the session object
					SessionContext.establish(user);
				}
				catch (BaseApiException e) {
					RequestErrorContext.setFbConnectionError(true);
					//TODO need to do anything else to handle this error?
				}
				
				//update the user's login time
				//TODO this shouldn't happen if the user is already logged in
				UserModel user = SessionContext.user();
				user.setLoginTimeAndUpdate();
				
				//don't get the associated user, that will be taken care of in SecuredActions
				//redirect to the given redirect url, or to the landing page
				Logger.debug("Redirecting to " + targetUrl);
				return redirect(targetUrl);
			}
			catch (ApiNoResponseException ex) {
				//TODO how should this be handled?
				throw new InternalServerErrorPageException(ex);
			}
		}
	}
	
}
