package controllers;

import play.libs.F.Function;
import play.libs.WS;
import play.mvc.Result;

import common.AppCtx;
import common.SecurityEscapingUtil;
import common.SecurityEscapingUtil.Escaper;

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
	//TODO use a pop-up instead of redirecting the whole browser to Facebook
	
	//TODO move as much logic to an Action class as possible
	//TODO change url to /login/fb? or /fb/login?
	
	//TODO add "fbTokenExpiresTime" column to DB? Add session timeout?

	/*
	 * TODO add this flow to a top-level place where we can deal with:
	 * 		- re-authenticating timed-out sessions
	 * 		- refreshing expired fb tokens
	 * 		- authenticating for pages that need it
	 * 		- re-authenticating for pages that want to force it
	 */
	
	//TODO handle the case that the user deauthorizes the app
	
	/**
	 * Handles the Facebook login. Redirects the user to the Facebook login dialogue,
	 * or exchange the code for an access token
	 * @param code the code returned from Facebook. If null, redirect the user to the login dialogue
	 * @return
	 */
	public static Result fblogin(String code) {
		System.out.println("\n\n\n\n\n" + getFbloginUrlEncoded() + "\n\n\n\n"); //TODO
		if (code == null) {
			//the login flow has started, redirect to the Facebook login dialogue
			String redirectUrl = "https://www.facebook.com/dialog/oauth" +
								"?client_id=" + AppCtx.Var.FB_APP_ID.val() +
								"&redirect_uri=" + getFbloginUrlEncoded();
								//TODO add CSRF protection
			return redirect(redirectUrl);
		}
		else {
			//TODO handle the case that they did not authorize the app
			final String tokenUrl = "https://graph.facebook.com/oauth/access_token" +
								"?client_id=" + AppCtx.Var.FB_APP_ID.val() +
								"&redirect_uri=" + getFbloginUrlEncoded() +
								"&client_secret=" + AppCtx.Var.FB_APP_SECRET.val() +
								"&code=" + code;
			return async(
				WS.url(tokenUrl).get().map(
					new Function<WS.Response, Result>() {
						@Override
						public Result apply(WS.Response resp) {
							return ok("url: " + tokenUrl + "\n\nresponse: " + resp.getBody());
						}
					}
				)
			);
		}
	}
	
	/** Gets the absolute url to the fblogin() action, URL-escaped */
	private static String getFbloginUrlEncoded() {
		return SecurityEscapingUtil.escape(
					routes.FbAuthWebController.fblogin(null).absoluteURL(request()),
					Escaper.URL
				);
	}
	
}
