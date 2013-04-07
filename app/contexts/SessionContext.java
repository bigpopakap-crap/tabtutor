package contexts;

import java.util.concurrent.Callable;

import models.SessionModel;
import models.UserModel;
import play.Logger;
import play.i18n.Lang;
import play.mvc.Http.Context;
import api.fb.FbApi;

/**
 * This class holds methods to query the current session for important objects,
 * like the session model itself or the FbApi object
 * 
 * @author bigpopakap
 * @since 2013-03-02
 *
 */
public abstract class SessionContext extends BaseContext {
	
	/** Get the language of the current session context. Useful for templates */
	public static synchronized Lang lang() {
		return Context.current().lang();
	}
	
	/** Get the session model object for the current session */
	public static synchronized SessionModel get() {
		return getOrLoad(SessionModel.SESSION_OBJ_CONTEXT_KEY, SESSION_LOADER);
	}
	
	/** Determines if there is a session */
	public static synchronized boolean has() {
		return get() != null;
	}
	
	/** Get the current logged-in user */
	public static synchronized UserModel user() {
		return getOrLoad(UserModel.USER_OBJ_CONTEXT_KEY, USER_LOADER);
	}
	
	/** Determines if there is a logged-in user */
	public static synchronized boolean hasUser() {
		return user() != null;
	}
	
	/** Get the FbApi object for the the current session */
	public static synchronized FbApi fbApi() {
		return getOrLoad(FbApi.FBAPI_OBJ_CONTEXT_KEY, FBAPI_LOADER);
	}
	
	/** Determines if there is an FbApi object */
	public static synchronized boolean hasFbApi() {
		return fbApi() != null;
	}
	
	/** Refreshes the context to make sure the values are current */
	public static synchronized void refresh() {
		//just delete them from the context, and they will be loaded next time
		refresh(
			SessionModel.SESSION_OBJ_CONTEXT_KEY,
			UserModel.USER_OBJ_CONTEXT_KEY,
			FbApi.FBAPI_OBJ_CONTEXT_KEY
		);
		Logger.debug(SessionContext.class.getCanonicalName() + " refreshed");
	}
	
	/* *****************************************************
	 *  BEGIN CALLABLE HELPERS
	 ***************************************************** */
	
	/** The Callable that can get the Session */
	private static final Callable<SessionModel> SESSION_LOADER = new Callable<SessionModel>() {

		@Override
		public SessionModel call() throws Exception {
			String sessionId = Context.current().session().get(SessionModel.SESSION_ID_COOKIE_KEY);
			if (sessionId != null && SessionModel.isValidExistingId(sessionId)) {
				return SessionModel.getById(sessionId);
			}
			else {
				return null;
			}
		}
		
	};
	
	/** The Callable that can get the User */
	private static final Callable<UserModel> USER_LOADER = new Callable<UserModel>() {

		@Override
		public UserModel call() throws Exception {
			SessionModel session = get();
			if (session != null && session.hasValidUserPk()) {
				return session.getUser();
			}
			else {
				return null;
			}
		}
		
	};
	
	/** The Callable that can get the FbApi */
	private static final Callable<FbApi> FBAPI_LOADER = new Callable<FbApi>() {

		@Override
		public FbApi call() throws Exception {
			SessionModel session = get();
			if (session != null && session.hasValidFbAuthInfo()) {
				return new FbApi(session.getFbToken());
			}
			else {
				return null;
			}
		}
		
	};
	
}