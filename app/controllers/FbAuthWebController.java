package controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.SessionModel;
import play.libs.F.Function;
import play.libs.WS;
import play.mvc.Result;

import common.AppCtx;
import common.EscapingUtil;
import common.EscapingUtil.Escaper;

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
	//TODO add CSRF protection
	//TODO handle the case that they did not authorize the app
	
	//TODO move as much logic to an Action class as possible
	//TODO handle the case that the user deauthorizes the app
	
	private static final Pattern FB_TOKEN_PATTERN = Pattern.compile("^access_token=(.+)&");
	private static final Pattern FB_TOKEN_EXPIRY_PATTERN = Pattern.compile("&expires=(\\d+)$");
	
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
								"?client_id=" + AppCtx.Var.FB_APP_ID.val() +
								"&redirect_uri=" + getFbloginUrlEncoded() +
								"&state=" + ""; //TODO csrf generate
			return redirect(redirectUrl);
		}
		else {
			//TODO csrf validate
			
			final String tokenUrl = "https://graph.facebook.com/oauth/access_token";
			final String tokenParams = "client_id=" + AppCtx.Var.FB_APP_ID.val() +
								"&redirect_uri=" + getFbloginUrlEncoded() +
								"&client_secret=" + AppCtx.Var.FB_APP_SECRET.val() +
								"&code=" + code;
			return async(
				WS.url(tokenUrl).post(tokenParams).map(
					new Function<WS.Response, Result>() {
						@Override
						public Result apply(WS.Response resp) {
							//parse the response for the token and expiry
							String token = parseToken(resp);
							int tokenExpiry = parseTokenExpiry(resp);
							
							//add this information to the session
							SessionModel session = AppCtx.Session.get();
							SessionModel.Updater.setFbAuthInfoAndUpdate(session, token, tokenExpiry);
							
							//don't get the associated user, that will be taken care of in SecuredActions
							//redirect to the given redirect url, or to the landing page
							return redirect("/");
						}
					}
				)
			);
		}
	}
	
	/** Gets the absolute url to the fblogin() action, URL-escaped */
	private static String getFbloginUrlEncoded() {
		return EscapingUtil.escape(
					routes.FbAuthWebController.fblogin(null, null).absoluteURL(request()),
					Escaper.URL
				);
	}
	
	/** Parses for the access token returned from Facebook, or returns null */
	private static String parseToken(WS.Response resp) {
		try {
			Matcher tokenMatcher = FB_TOKEN_PATTERN.matcher(resp.getBody());
			return (tokenMatcher.find()) ? tokenMatcher.group(1) : null;
		}
		catch (Exception ex) {
			return null;
		}
	}
	
	/** Parses for the access token expiry time returned from Facebook as an int number of seconds left, or returns null */
	private static int parseTokenExpiry(WS.Response resp) {
		try {
			Matcher tokenExpiryMatcher = FB_TOKEN_EXPIRY_PATTERN.matcher(resp.getBody());
			String tokenExpiry = (tokenExpiryMatcher.find()) ? tokenExpiryMatcher.group(1) : null;
			return Integer.parseInt(tokenExpiry);
		}
		catch (Exception ex) {
			return 0;
		}
	}
	
}
