package doer;

import java.util.UUID;

import play.Logger;

import models.SessionModel;

import com.avaje.ebean.Ebean;

/**
 * Performs all actions concerning Sessions and related data. Except for simple select queries that do not
 * modify data, this class should be the only one to interact with the database for things concerning sessions
 * 
 * Includes CSRF token management on the SessionCsrfToken table
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-24
 *
 */
public abstract class SessionDoer extends BaseDoer {
	
	/** Returns true if the given string represents a valid session ID that exists */
	public static boolean isValidExistingSessionId(String id) {
		if (id == null) return false;
		try {
			UUID pk = UUID.fromString(id);
			SessionModel session = SessionModel.FINDER.byId(pk);
			return session != null;
		}
		catch (IllegalArgumentException ex) {
			return false;
		}
	}
	
	/**
	 * Creates a new Session and all data associated with it
	 * @return the SessionModel that was just created
	 */
	public static SessionModel createNewSession() {
		SessionModel newSession = new SessionModel();
		Ebean.save(newSession);
		return newSession;
	}
	
	/**
	 * @param session the session to check
	 * @return true if this session needs a Facebook auth token, either because
	 * it has not been done yet, or because the token has expired
	 */
	public static boolean needsFacebookAuth(SessionModel session) {
		if (session == null) {
			//this is a weird case, but forcing auth may solve it
			Logger.warn("Null session passed to needsFacebookAuth method");
			return true;
		}
		else {
			return session.fbToken == null || session.isFbtokenExpired;
		}
	}
	
	public static boolean needsUserReference(SessionModel session) {
		if (session == null) {
			//this is a weird case, but forcing auth may solve it
			Logger.warn("Null session passed to needsUserReference method");
			return true;
		}
		else {
			return session.userPk == null;
		}
	}

}
