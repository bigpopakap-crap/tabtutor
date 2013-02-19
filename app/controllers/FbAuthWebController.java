package controllers;

import java.net.URLEncoder;

import play.libs.F.Function;
import play.libs.WS;
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
			String redirectUrl = "https://www.facebook.com/dialog/oauth" +
								"?client_id=" + AppCtx.Var.FB_APP_ID.val() +
								"&redirect_uri=" + getFbloginUrlEncoded();
								//TODO add CSRF protection
			return redirect(redirectUrl);
		}
		else {
			//TODO handle the case that they did not authorize the app
			String tokenUrl = "https://graph.facebook.com/oauth/access_token" +
								"?client_id=" + AppCtx.Var.FB_APP_ID.val() +
								"&redirect_uri=" + getDomainUrlEncoded() +
								"&client_secret=" + AppCtx.Var.FB_APP_SECRET.val() +
								"&code=" + code;
			System.out.println("\n\n\nredir: " + tokenUrl + "\n\n\n");
			return async(
				WS.url(tokenUrl).post("").map(
					new Function<WS.Response, Result>() {
						@Override
						public Result apply(WS.Response resp) {
							System.out.println("\n\n\n" + resp.getBody() + "\n\n\n");
							return null;
						}
					}
				)
			);
		}
	}
	
	private static String getDomainUrlEncoded() {
		return urlEncode(AppCtx.Var.DOMAIN.val());
	}
	
	private static String getFbloginUrlEncoded() {
		return urlEncode(routes.FbAuthWebController.fblogin(null).absoluteURL(request()));
	}
	
	private static String urlEncode(String url) {
		//TODO use something else for url encoding
		return URLEncoder.encode(url);
	}

}
