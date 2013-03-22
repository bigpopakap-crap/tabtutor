package actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import models.SessionModel;
import models.UserModel;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;
import api.exceptions.BaseApiException;
import api.fb.FbApi;
import api.fb.FbJsonResponse;
import contexts.ErrorContext;
import contexts.SessionContext;
import controllers.FbAuthWebController;

/**
 * This Action will log the user in through Facebook, and ensure that the authentication
 * token has not expired
 * It is assumed that the session has already been set up
 * 
 * @author bigpopakap
 * @since 2013-02-24
 *
 */
public class FbAuthAction extends Action.Simple {
	
	/**
	 * Annotation for applying FacebookAuthenticatedAction
	 * 
	 * @author bigpopakap
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
		final SessionModel session = SessionContext.get();
		if (session == null) throw new IllegalStateException("Session should have been populated by now");
		
		//if there is no Facebook authToken associated, or it has expired start the Facebook login flow
		//TODO add ability to force re-authentication
		if (!SessionModel.Validator.hasValidFbAuthInfo(session)) {
			Logger.debug("Session needs Facebook auth. Redirecting to the login flow");
			return FbAuthWebController.fblogin(null, null);
		}
		
		//get the FbApi object, which must be valid by now
		FbApi fbApi = SessionContext.fbApi();
		if (fbApi == null) throw new IllegalStateException("FbApi should have been populated by now");

		//ensure that a user is referenced by the session
		if (!SessionModel.Validator.hasValidUserPk(session)) {
			Logger.debug("Session needs a user reference. Fetching Facebook ID and looking up user object");
			
			//start by getting the user's Facebook ID from the Facebook API
			try {
				FbJsonResponse fbJson = fbApi.me().get().get();
				
				String fbId = fbJson.fbId();
				String firstName = fbJson.firstName();
				String lastName = fbJson.lastName();
				String email = fbJson.email();
				
				//get the user associated with this Facebook ID, or create one
				UserModel user = UserModel.Selector.getByFbId(fbId);
				if (user == null) {
					user = UserModel.Factory.createNewRealUserAndSave(fbId, firstName, lastName, email);
				}
				
				//add this user ID to the session object
				SessionModel.Updater.setUserPkAndUpdate(session, user.pk);
			} catch (BaseApiException e) {
				ErrorContext.setFbConnectionError(true);
				//TODO need to do anything else to handle this error?
			}
		}
		
		//all checks passed, return the delegate
		return delegate.call(ctx);
	}
	
}