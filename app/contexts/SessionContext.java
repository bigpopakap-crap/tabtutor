package contexts;

import java.util.concurrent.Callable;

import models.SessionModel;
import models.UserModel;
import play.i18n.Lang;
import play.mvc.Http.Context;
import utils.Logger;
import utils.Universe;
import utils.Universe.UniverseElement;
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
	
	private static final UniverseElement<String> SESSION_OBJ_CONTEXT_KEY = CONTEXT_KEY_UNIVERSE.register("sessionObjectContextKey");
	private static final UniverseElement<String> USER_OBJ_CONTEXT_KEY = CONTEXT_KEY_UNIVERSE.register("userObjectContextKey");
	private static final UniverseElement<String> FBAPI_OBJ_CONTEXT_KEY = CONTEXT_KEY_UNIVERSE.register("fbApiObjectContextKey");
	
	private static final Universe<String> SESSION_COOKIE_KEY_UNIVERSE = new Universe<>();
	private static final UniverseElement<String> SESSION_ID_COOKIE_KEY = SESSION_COOKIE_KEY_UNIVERSE.register("wtfspk"); // The key to use in the cookie for the session ID
	
	/** Creates a new session and establishes a new cookie.
	 * This is the only place in the app that should deal with the session cookie */
	public static synchronized void init(Context ctx) {
		SessionModel newSession = SessionModel.createAndSave();
		ctx.session().put(SESSION_COOKIE_KEY_UNIVERSE.extract(SESSION_ID_COOKIE_KEY), newSession.getPk_String());
		Logger.info("Put session " + newSession.getPk() + " in cookie for IP " + ctx.request().remoteAddress());
	}
	
	/** Establishes the session context as the given user */
	public static synchronized void establish(UserModel user) {
		if (!hasSession()) init(Context.current());
		session().setUserAndUpdate(user);
	}
	
	/** Unestablishes the context for no user. Basically, log out */
	public static synchronized void unestablish() {
		establish(null);
	}
	
	/** Get the language of the current session context. Useful for templates */
	public static synchronized Lang lang() {
		return Context.current().lang();
	}
	
	/** Get the session model object for the current session */
	public static synchronized SessionModel session() {
		return getOrLoad(SESSION_OBJ_CONTEXT_KEY, SESSION_LOADER);
	}
	
	/** Determines if there is a session */
	public static synchronized boolean hasSession() {
		return session() != null;
	}
	
	/** Get the current logged-in user */
	public static synchronized UserModel user() {
		return getOrLoad(USER_OBJ_CONTEXT_KEY, USER_LOADER);
	}
	
	/** Determines if there is a logged-in user */
	public static synchronized boolean hasUser() {
		return user() != null;
	}
	
	/** Get the FbApi object for the the current session */
	public static synchronized FbApi fbApi() {
		return getOrLoad(FBAPI_OBJ_CONTEXT_KEY, FBAPI_LOADER);
	}
	
	/** Determines if there is an FbApi object */
	public static synchronized boolean hasFbApi() {
		return fbApi() != null;
	}
	
	/** Refreshes the context to make sure the values are current */
	public static synchronized void refresh() {
		//just delete them from the context, and they will be loaded next time
		refresh(
			SESSION_OBJ_CONTEXT_KEY,
			USER_OBJ_CONTEXT_KEY,
			FBAPI_OBJ_CONTEXT_KEY
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
			String sessionId = Context.current().session().get(SESSION_COOKIE_KEY_UNIVERSE.extract(SESSION_ID_COOKIE_KEY));
			if (sessionId != null && SessionModel.isValidExistingId(sessionId)) {
				return SessionModel.getByPk(sessionId);
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
			SessionModel session = session();
			if (session != null && session.hasUser()) {
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
			SessionModel session = session();
			if (session != null && session.hasValidFbAuthInfo()) {
				return new FbApi(session.getFbToken());
			}
			else {
				return null;
			}
		}
		
	};

}