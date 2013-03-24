package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.Logger;
import utils.DbTypesUtil;

import com.avaje.ebean.annotation.Formula;

import contexts.BaseContext.ContextKey;

/**
* This Ebean maps to the User table, and represents user metadata
* 
* @author bigpopakap
* @since 2013-02-23
*
*/
@Entity
@Table(name = "User")
public class UserModel extends BaseModel {
	
	public static final ContextKey USER_OBJ_CONTEXT_KEY = ContextKey.register("userObjectContextKey");

	private static final long serialVersionUID = 5854422586239724109L;
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "fbId") public String fbId;
	@Column(name = "fbIsAuthed") public boolean fbIsAuthed;
	@Column(name = "firstName") public String firstName;
	@Column(name = "lastName") public String lastName;
	@Column(name = "email") public String email;
	@Column(name = "registerTime") public Date registerTime;
	@Column(name = "lastAccessTime") public Date lastAccessTime;
	@Column(name = "lastLoginTime") public Date lastLoginTime;
	@Column(name = "secondToLastLoginTime") public Date secondToLastLoginTime;
	
	@Transient @Formula(select = "firstName || ' ' || lastName") public String fullName;
	@Transient @Formula(select = "secondToLastLoginTime IS NULL") public boolean isFirstLogin;
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, UserModel> FINDER = new Finder<UUID, UserModel>(
		UUID.class, UserModel.class
	);
	
	public static class Factory extends BaseFactory {
		
		/**
		 * Creates a new user object with the given Facebook auth information
		 * @param isTestUser is this a test user who is not connected to Facebook
		 * @param fbId the Facebook ID of the user
		 * @param firstName
		 * @param lastName
		 * @param email
		 * @return
		 */
		public static UserModel createNewUserAndSave(String fbId, String firstName, String lastName, String email) {
			Date now = DbTypesUtil.now();
			return create(UUID.randomUUID(), fbId, true, firstName, lastName, email, now, now, now, null, true);
		}
		
		/** This should be the only method that touches the fields */
		private static UserModel create(UUID pk, String fbId, boolean fbIsAuthed,
										String firstName, String lastName, String email,
										Date registerTime, Date lastLoginTime, Date lastAccessTime,
										Date secondToLastLoginTime, boolean save) {
			UserModel user = new UserModel();
			user.pk = pk;
			user.fbId = fbId;
			user.fbIsAuthed = fbIsAuthed;
			user.firstName = firstName;
			user.lastName = lastName;
			user.email = email;
			user.registerTime = registerTime;
			user.lastAccessTime = lastAccessTime;
			user.lastLoginTime = lastLoginTime;
			user.secondToLastLoginTime = secondToLastLoginTime;
			
			if (save) {
				user.doSaveAndRetry();
				Logger.debug("Saved user " + user.pk + " to database");
			}
			
			return user;
		}
		
	}
	
	public class Getter extends BaseGetter {
		
		public UUID pk() { return UUID.fromString(pk.toString()); } //defensive copy
		public String fbId() { return fbId; }
		public boolean fbIsAuthed() { return fbIsAuthed; }
		public String firstName() { return firstName; }
		public String lastName() { return lastName; }
		public String fullName() { return fullName; }
		public String email() { return email; }
		public Date lastLoginTime() { return (Date) lastLoginTime.clone(); } //defensive copy
		public Date secondToLastLoginTime() { return (Date) secondToLastLoginTime.clone(); } //defensive copy
		public boolean isFirstLogin() { return isFirstLogin; }
		
	}
	
	public static class Selector extends BaseSelector {
		
		/** Gets a Session by ID, converts the string to a UUID internally */
		public static UserModel getById(String id) {
			try {
				return getById(id != null ? UUID.fromString(id) : null);
			}
			catch (IllegalArgumentException ex) {
				//the string was not a valid UUID
				return null;
			}
		}
		
		/** Gets a Session by ID */
		public static UserModel getById(UUID id) {
			return id != null ? FINDER.byId(id) : null;
		}

		/** Gets a Session by fbId */
		public static UserModel getByFbId(String fbId) {
			return fbId != null ? FINDER.where().eq("fbId", fbId).findUnique() : null;
		}
		
	}
	
	public static class Updater extends BaseUpdater {
		
		/** Sets the user last access time to the current time */
		public static void setLastAccessTimeAndUpdate(UserModel user) {
			if (user == null) {
				Logger.debug("setLastAccessTimeAndUpdate called on null user");
				return;
			}
			
			user.lastAccessTime = DbTypesUtil.now();
			user.doUpdateAndRetry();
			Logger.debug("User " + user.pk + " last access time updated to " + user.lastAccessTime);
		}
		
		/** Sets the user login time to the current time */
		public static void setLoginTimeAndUpdate(UserModel user) {
			if (user == null) {
				Logger.debug("setLoginTimeAndUpdate called on null user");
				return;
			}
			
			user.secondToLastLoginTime = user.lastLoginTime;
			user.lastLoginTime = DbTypesUtil.now();
			user.doUpdateAndRetry();
			Logger.debug("User " + user.pk + " login time updated to " + user.lastLoginTime);
		}
		
	}
	
	public static class Validator extends BaseValidator {
		
		/** Determines if a User exists with the given ID */
		public static boolean isValidExistingId(UUID id) {
			return Selector.getById(id) != null;
		}
		
		/** Determines if a User exists with the given fbId */
		public static boolean isValidExistingFbId(String fbId) {
			return Selector.getByFbId(fbId) != null;
		}
		
	}
	
}
