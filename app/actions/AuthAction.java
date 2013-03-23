package actions;

import java.util.LinkedList;
import java.util.List;

import models.SessionModel;
import models.UserModel;
import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import actions.ActionAnnotations.Authed;
import api.exceptions.BaseApiException;
import api.fb.FbApi;
import api.fb.FbJsonResponse;
import contexts.RequestErrorContext;
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
public class AuthAction extends BaseAction<Authed> {
	
	@Override
	protected List<Class<? extends BaseAction<?>>> hook_listDependencies() {
		List<Class<? extends BaseAction<?>>> list = new LinkedList<Class<? extends BaseAction<?>>>();
		list.add(SessionAction.class);
		return list;
	}


	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		//get the session object
		final SessionModel session = SessionContext.get();
		if (session == null) throw new IllegalStateException("Session should have been populated by now");
		
		//if it is a real user and we need to force re-auth or the auth info is invalid, redirect to fb login
		//TODO add ability to force re-authentication: need to worry about getting caught in infinite loop
		if (!SessionModel.Validator.hasValidFbAuthInfo(session)) {
			Logger.debug("Session needs Facebook auth. Redirecting to the login flow");
			return FbAuthWebController.fblogin(null, null, ctx.request().path());
		}
		
		//get the FbApi object, which must be valid by now
		FbApi fbApi = SessionContext.fbApi();
		if (fbApi == null) throw new IllegalStateException("FbApi should have been populated by now");

		//ensure that a user is referenced by the session
		if (!SessionModel.Validator.hasValidUserPk(session)) {
			Logger.debug("Session needs a user reference. Fetching Facebook ID and looking up user object");
			
			//start by getting the user's Facebook ID from the Facebook API
			try {
				FbJsonResponse fbJson = fbApi.me().get();
				
				String fbId = fbJson.fbId();
				String firstName = fbJson.firstName();
				String lastName = fbJson.lastName();
				String email = fbJson.email();
				
				//get the user associated with this Facebook ID, or create one
				UserModel user = UserModel.Selector.getByFbId(fbId);
				if (user == null) {
					user = UserModel.Factory.createNewUserAndSave(fbId, firstName, lastName, email);
				}
				
				//add this user ID to the session object
				SessionModel.Updater.setUserPkAndUpdate(session, user.pk);
			}
			catch (BaseApiException e) {
				RequestErrorContext.setFbConnectionError(true);
				//TODO need to do anything else to handle this error?
			}
		}
		
		//TODO find a way to enforce a permission access level
		
		//all checks passed, return the delegate
		return delegate.call(ctx);
	}

}
