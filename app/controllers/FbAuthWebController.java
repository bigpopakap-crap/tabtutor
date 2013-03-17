package controllers;

import java.util.Map;

import models.SessionModel;
import models.UserModel;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Result;
import utils.EscapingUtil;
import utils.EscapingUtil.Escaper;
import utils.QueryParamsUtil;
import contexts.AppContext;
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
	public static Result fblogin(String code, String state) {
		if (code == null) {
			//the login flow has started, redirect to the Facebook login dialogue
			String redirectUrl = "https://www.facebook.com/dialog/oauth" +
								"?client_id=" + AppContext.Var.FB_APP_ID.val() +
								"&redirect_uri=" + getFbloginUrlEncoded() +
								"&scope=email";
			return redirect(redirectUrl);
		}
		else {
			String tokenUrl = "https://graph.facebook.com/oauth/access_token";
			String tokenParams = "client_id=" + AppContext.Var.FB_APP_ID.val() +
								"&redirect_uri=" + getFbloginUrlEncoded() +
								"&client_secret=" + AppContext.Var.FB_APP_SECRET.val() +
								"&code=" + code;
			
			Response resp = WS.url(tokenUrl).post(tokenParams).get();
			
			//parse the response for the token and expiry
			Map<String, String> paramMap = QueryParamsUtil.queryStringToMap(resp.getBody());
			String token = paramMap.get("access_token");
			int tokenExpiry = Integer.parseInt(paramMap.get("expires"));
			
			//add this information to the session
			SessionModel session = SessionContext.get();
			SessionModel.Updater.setFbAuthInfoAndUpdate(session, token, tokenExpiry);
			
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
	
	/** Gets the absolute url to the fblogin() action, URL-escaped */
	private static String getFbloginUrlEncoded() {
		return EscapingUtil.escape(
			routes.FbAuthWebController.fblogin(null, null).absoluteURL(request()),
			Escaper.URL
		);
	}
	
}
