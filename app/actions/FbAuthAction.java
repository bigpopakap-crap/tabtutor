package actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import models.SessionModel;
import play.Logger;
import play.libs.F.Function;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;
import api.FbApi;
import api.FbApi.FbJsonResponse;

import common.AppCtx;

import controllers.FbAuthWebController;

/**
 * This Action will log the user in through Facebook, and ensure that the authentication
 * token has not expired
 * It is assumed that the session has already been set up
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-24
 *
 */
public class FbAuthAction extends Action.Simple {
	
	/**
	 * Annotation for applying FacebookAuthenticatedAction
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	@With({SessionAction.class, FbAuthAction.class})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FbAuthed {}

	/** Implements the action */
	@Override
	public Result call(Context ctx) throws Throwable {
		Logger.debug("Calling into " + this.getClass().getName());
		
		//get the session object
		SessionModel session = AppCtx.Session.get();
		if (session == null) throw new IllegalStateException("Session should have been populated by now");
		
		//if there is no Facebook authToken associated, or it has expired start the Facebook login flow
		//TODO add ability to force re-authentication
		if (!SessionModel.Validator.hasValidFbAuthInfo(session)) {
			Logger.debug("Session needs Facebook auth. Redirecting to the login flow");
			return FbAuthWebController.fblogin(null, null);
		}
		
		//get the FbApi object, which must be valid by now
		FbApi fbApi = AppCtx.Session.fbApi();
		if (fbApi == null) throw new IllegalStateException("FbApi should have been populated by now");

		//ensure that a user is referenced by the session
		if (!SessionModel.Validator.hasValidUserReference(session)) {
			Logger.debug("Session needs a user reference. Fetching Facebook ID and looking up user object");
			
			//get the user's Facebook ID from the Facebook API
			//TODO make sure this works
			fbApi.me().map(new Function<FbJsonResponse, String>() {

				@Override
				public String apply(FbJsonResponse fbJson) throws Throwable {
					// TODO Auto-generated method stub
					String fbId = fbJson.fbId();
					return fbId;
				}
				
			});
			
			//TODO get the user ID associated with this Facebook ID, or create one
			
			//TODO add this user ID to the session object
			
		}
		
		//all checks passed, pass control to the delegate action
		return delegate.call(ctx);
	}
	
}