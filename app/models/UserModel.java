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
	
	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "isTestUser") public boolean isTestUser;
	@Column(name = "fbId") public String fbId;
	@Column(name = "fbIsAuthed") public boolean fbIsAuthed;
	@Column(name = "firstName") public String firstName;
	@Column(name = "lastName") public String lastName;
	@Transient @Formula(select = "firstName || ' ' || lastName") public String fullName;
	@Column(name = "email") public String email;
	@Column(name = "registerTime") public Date registerTime;
	@Column(name = "lastAccessTime") public Date lastAccessTime;
	@Column(name = "lastLoginTime") public Date lastLoginTime;
	@Column(name = "secondToLastLoginTime") public Date secondToLastLoginTime;
	@Transient @Formula(select = "secondToLastLoginTime IS NULL") public boolean isFirstLogin;
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getFbId() { return fbId; }
	public boolean getFbIsAuthed() { return fbIsAuthed; }
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public String getFullName() { return fullName; }
	public String getEmail() { return email; }
	public Date getLastLoginTime() { return (Date) lastLoginTime.clone(); } //defensive copy
	public Date getsecondToLastLoginTime() { return (Date) secondToLastLoginTime.clone(); } //defensive copy
	public boolean isFirstLogin() { return isFirstLogin; }
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, UserModel> FINDER = new Finder<UUID, UserModel>(
		UUID.class, UserModel.class
	);
	
	/* **************************************************************************
	 *  BEGIN HOOKS
	 ************************************************************************** */
	
	/* **************************************************************************
	 *  BEGIN CONSTRUCTORS/SAVERS
	 ************************************************************************** */
	
	/**
	 * Creates a user with the given information
	 * Saves it to the DB
	 */
	public UserModel(String fbId, String firstName, String lastName, String email) {
		Date now = DbTypesUtil.now();
		
		this.pk = UUID.randomUUID();
		this.isTestUser = false;
		this.fbId = fbId;
		this.fbIsAuthed = true;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.registerTime = now;
		this.lastAccessTime = now;
		this.lastLoginTime = now;
		this.secondToLastLoginTime = null;
		
		//save this object
		doSaveAndRetry();
	}
	
	/* **************************************************************************
	 *  BEGIN SELECTORS
	 ************************************************************************** */
	
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
	
	/* **************************************************************************
	 * BEGIN  UPDATERS
	 ************************************************************************** */
	
	/** Sets the user last access time to the current time */
	public void setLastAccessTimeAndUpdate() {
		lastAccessTime = DbTypesUtil.now();
		doUpdateAndRetry();
		Logger.debug("User " + pk + " last access time updated to " + lastAccessTime);
	}
	
	/** Sets the user login time to the current time */
	public void setLoginTimeAndUpdate() {
		secondToLastLoginTime = lastLoginTime;
		lastLoginTime = DbTypesUtil.now();
		doUpdateAndRetry();
		Logger.debug("User " + pk + " login time updated to " + lastLoginTime);
	}
	
	/* **************************************************************************
	 *  BEGIN VALIDATORS
	 ************************************************************************** */
		
	/** Determines if a User exists with the given ID */
	public static boolean isValidExistingId(UUID id) {
		return getById(id) != null;
	}
	
	/** Determines if a User exists with the given fbId */
	public static boolean isValidExistingFbId(String fbId) {
		return getByFbId(fbId) != null;
	}
		
}
