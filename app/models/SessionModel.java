package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.Logger;

import com.avaje.ebean.annotation.Formula;
import common.DbTypesUtil;
import common.SessionContext;
import common.SqlCommandType.BasicDmlModifyingType;

/**
 * This Ebean maps to the Session table, and represents the active sessions
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-10
 *
 */
@Entity
@Table(name = "Session")
public class SessionModel extends BaseModel {
	
	//TODO figure out how to clean up old sessions
	
	/** The key to use in the cookie for the session ID */
	public static final String SESSION_ID_COOKIE_KEY = "wtfspk";
	
	/** The key to use to store the session model object in the session context */
	public static final String SESSION_OBJ_CONTEXT_KEY = "sessionObjectContextKey";
	
	private static final long serialVersionUID = -6111608082703517322L;
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "userPk") public UUID userPk; //TODO how to populate this as the user object reference?
	@Column(name = "fbToken") public String fbToken;
	@Column(name = "fbTokenExpireTime") public Date fbTokenExpireTime;
	@Transient @Formula(select = "NOW() > fbTokenExpireTime") public boolean isFbtokenExpired;
	@Column(name = "startTime") public Date startTime;
	@Column(name = "lastAccessTime") public Date lastAccessTime;
	
	@Override
	protected void postOp(BasicDmlModifyingType opType) {
		//refresh the app context
		//TODO add caching here
		super.postOp(opType);
		SessionContext.refresh();
	}
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, SessionModel> FINDER = new Finder<UUID, SessionModel>(
		UUID.class, SessionModel.class
	);
	
	public static class Factory extends BaseFactory {
		
		/**
		 * Creates a default new session with a random ID, no associated user,
		 * no Facebook auth information, and the current start and update times
		 * @return the new model after it is saved
		 */
		public static SessionModel createAndSave() {
			Date now = DbTypesUtil.now();
			return create(UUID.randomUUID(), null, null, null, now, now, true);
		}
		
		private static SessionModel create(UUID pk, UUID userPk,
									String fbtoken, Date fbtokenExpireTime,
									Date startTime, Date lastAccessTime,
									boolean save) {
			//TODO how to make sure this covers all columns?
			SessionModel session = new SessionModel();
			session.pk = pk;
			session.userPk = userPk;
			session.fbToken = fbtoken;
			session.fbTokenExpireTime = fbtokenExpireTime;
			session.startTime = startTime;
			session.lastAccessTime = lastAccessTime;
			
			if (save) {
				session._save();
				Logger.debug("Saved session " + session.pk + " to database");
			}
			
			return session;
		}

	}
	
	public class Getter extends BaseGetter {
		
		public UUID pk() { return UUID.fromString(pk.toString()); } //defensive copy
		public String pk_String() { return pk().toString(); }
		public UUID userPk() { return UUID.fromString(userPk.toString()); }
		public String fbToken() { return fbToken; }
		public Date fbTokenExpireTime() { return (Date) fbTokenExpireTime.clone(); } //defensive copy
		public boolean isFbtokenExpired() { return isFbtokenExpired; }
		public Date startTime() { return (Date) startTime.clone(); } //defensive copy
		public Date lastAccessTime() { return (Date) lastAccessTime.clone(); } //defensive copy
		
	}
	public Getter GETTER = new Getter();
	
	public static class Selector extends BaseSelector {
		
		/** Gets a Session by ID, converts the string to a UUID internally */
		public static SessionModel getById(String id) {
			try {
				return getById(UUID.fromString(id));
			}
			catch (IllegalArgumentException ex) {
				//the string was not a valid UUID
				return null;
			}
		}
		
		/** Gets a Session by ID */
		public static SessionModel getById(UUID id) {
			return id != null ? FINDER.byId(id) : null;
		}
		
		/** Gets the user associated with this session, or null if no user has been associated yet */
		public static UserModel getUser(SessionModel session) {
			return session != null && Validator.hasValidUserPk(session)
						? UserModel.Selector.getById(session.userPk)
						: null;
		}
		
	}
	
	public static class Updater extends BaseUpdater {
		
		/**
		 * Adds the Facebook auth token and expiry time to the session
		 * @param session the session to alter
		 * @param token the auth token
		 * @param seconds number of seconds until the token expires
		 */
		public static void setFbAuthInfoAndUpdate(SessionModel session, String token, int seconds) {
			if (session == null) {
				Logger.debug("setFbAuthInfoAndUpdate called on null session");
				return;
			}
			
			session.fbToken = token;
			session.fbTokenExpireTime = DbTypesUtil.add(DbTypesUtil.now(), seconds);
			session._update();
			Logger.debug("Session " + session.pk + " updated with Facebook token " + session.fbToken);
		}
		
		/** Adds the user pk to the session. Assumes that the given userPk is valid */
		public static void setUserPkAndUpdate(SessionModel session, UUID userPk) {
			if (session == null) {
				Logger.debug("setUserPkAndUpdate called on null session");
				return;
			}
			
			session.userPk = userPk;
			session._update();
			Logger.debug("Session " + session.pk + " updated with User reference " + session.userPk);
		}
		
	}
	
	public static class Validator extends BaseValidator {
		
		/** Determines if the given ID is valid and it exists in the database */
		public static boolean isValidExistingId(String id) {
			return Selector.getById(id) != null;
		}
		
		/**
		 * @param session the session to check
		 * @return true if this session has a Facebook auth token that has not expired
		 */
		public static boolean hasValidFbAuthInfo(SessionModel session) {
			if (session == null) {
				//this is a weird case, but forcing auth may solve it
				Logger.warn("Null passed to hasValidFbAuthInfo method");
				return false;
			}
			else {
				return session.fbToken != null && !session.isFbtokenExpired;
			}
		}
		
		/**
		 * @param session the session to check
		 * @return true if the session has a reference to a user
		 */
		public static boolean hasValidUserPk(SessionModel session) {
			if (session == null) {
				//this is a weird case, but forcing auth may solve it
				Logger.warn("Null passed to hasValidUserReference method");
				return true;
			}
			else {
				return session.userPk != null && UserModel.Validator.isValidExistingId(session.userPk);
			}
		}
		
	}
	
}
