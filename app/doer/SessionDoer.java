package doer;

import java.util.UUID;

import models.SessionModel;

import com.avaje.ebean.Ebean;

/**
 * Performs all actions concerning Sessions and related data
 * Includes CSRF token management
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-24
 *
 */
public abstract class SessionDoer extends BaseDoer {
	
	/** Returns true if the given string represents a valid session ID that exists */
	public static boolean isValidSessionId(String id) {
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

}
