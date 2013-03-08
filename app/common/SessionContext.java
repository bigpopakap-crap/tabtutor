package common;

import java.util.concurrent.Callable;

import models.SessionModel;
import models.UserModel;
import play.Logger;
import play.mvc.Http.Context;
import api.FbApi;

/**
 * This class holds methods to query the current session for important objects,
 * like the session model itself or the FbApi object
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-02
 *
 */
public abstract class SessionContext {
	
	/** Get the session model object for the current session */
	public static SessionModel get() {
		return getOrLoad(SessionModel.SESSION_OBJ_CONTEXT_KEY, SESSION_LOADER);
	}
	
	/** Get the current logged-in user */
	public static UserModel user() {
		return getOrLoad(UserModel.USER_OBJ_CONTEXT_KEY, USER_LOADER);
	}
	
	/** Determines if there is a logged-in user */
	public static boolean hasUser() {
		return user() != null;
	}
	
	/** Get the FbApi object for the the current session */
	public static FbApi fbApi() {
		return getOrLoad(FbApi.FBAPI_OBJ_CONTEXT_KEY, FBAPI_LOADER);
	}
	
	/** Refreshes the context to make sure the values are current */
	public static void refresh() {
		//just delete them from the context, and they will be loaded next time
		refresh(
			SessionModel.SESSION_OBJ_CONTEXT_KEY,
			UserModel.USER_OBJ_CONTEXT_KEY,
			FbApi.FBAPI_OBJ_CONTEXT_KEY
		);
		Logger.debug(SessionContext.class + " refreshed");
	}
	
	/** Helper to clear set all the values to null for the given keys */
	private static void refresh(String... keys) {
		for (String key : keys) {
			Context.current().args.put(key, null);
		}
	}
	
	/** Helper to either get the value from the context (given the key), or
	 *  load it and save it to the context */
	@SuppressWarnings("unchecked")
	private static <T> T getOrLoad(String contextKey, Callable<T> loader) {
		//try getting the object from the context
		T t = (T) Context.current().args.get(contextKey);
		
		//if not retrieved, load it and store it in the context
		if (t == null) {
			try {
				t = loader.call();
			}
			catch (Exception ex) {
				//set to null so this method just does nothing and returns null
				t = null;
			}
			
			Context.current().args.put(contextKey, t);
		}
		
		//finally return the value
		return t;
	}
	
	/** The Callable that can get the Session */
	private static final Callable<SessionModel> SESSION_LOADER = new Callable<SessionModel>() {

		@Override
		public SessionModel call() throws Exception {
			String sessionId = Context.current().session().get(SessionModel.SESSION_ID_COOKIE_KEY);
			if (sessionId != null && SessionModel.Validator.isValidExistingId(sessionId)) {
				return SessionModel.Selector.getById(sessionId);
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
			if (session != null && SessionModel.Validator.hasValidUserPk(session)) {
				return SessionModel.Selector.getUser(session);
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
			if (session != null && SessionModel.Validator.hasValidFbAuthInfo(session)) {
				return new FbApi(session.GETTER.fbToken());
			}
			else {
				return null;
			}
		}
		
	};
	
}