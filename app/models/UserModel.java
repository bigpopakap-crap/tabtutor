package models;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import utils.DateUtil;
import utils.Logger;

import com.avaje.ebean.annotation.Formula;


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
	
	private static final long serialVersionUID = 5854422586239724109L;
	
	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "fbId") public String fbId;
	@Column(name = "fbIsAuthed") public boolean fbIsAuthed;
	@Column(name = "username") public String username;
	@Column(name = "email") public String email;
	@Column(name = "registerTime") public Date registerTime;
	@Column(name = "lastAccessTime") public Date lastAccessTime;
	@Column(name = "lastLoginTime") public Date lastLoginTime;
	@Column(name = "secondToLastLoginTime") public Date secondToLastLoginTime;
	
	@OneToMany(fetch = FetchType.LAZY) @JoinColumn(name = "userPk_author", referencedColumnName = "pk") public Set<NotationMetaModel> authoredNotations; //TODO use ordered list?
	
	@Transient @Formula(select = "(firstName || ' ' || lastName)") public String fullName;
	@Transient @Formula(select = "(lastLoginTime IS NULL OR secondToLastLoginTime IS NULL)") public boolean isFirstLogin;
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getFbId() { return fbId; }
	public boolean getFbIsAuthed() { return fbIsAuthed; }
	public String getUsername() { return username; }
	public String getFullName() { return fullName; }
	public String getEmail() { return email; }
	public Date getRegisterTime() { return registerTime; }
	public Date getLastAccessTime() { return lastAccessTime; }
	public Date getLastLoginTime() { return (Date) lastLoginTime.clone(); } //defensive copy
	public Date getSecondToLastLoginTime() { return secondToLastLoginTime != null ? (Date) secondToLastLoginTime.clone() : null; } //defensive copy
	public boolean isFirstLogin() { return isFirstLogin; }
	public Set<NotationMetaModel> getAuthoredNotations() { return authoredNotations; }
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, UserModel> FINDER = new Finder<>(
		UUID.class, UserModel.class
	);
	
	/* **************************************************************************
	 *  BEGIN HOOKS
	 ************************************************************************** */
	
	/* **************************************************************************
	 *  BEGIN CONSTRUCTORS (PRIVATE)
	 ************************************************************************** */
	
	/**
	 * Creates a user with the given information
	 * Username will be some default value
	 */
	private UserModel(String fbId, String username, String email) {
		Date now = DateUtil.now();
		
		this.pk = UUID.randomUUID();
		this.fbId = fbId;
		this.fbIsAuthed = (fbId != null);
		this.username = username;
		this.email = email;
		this.registerTime = now;
		this.lastAccessTime = now;
		this.lastLoginTime = now;
		this.secondToLastLoginTime = null;
	}
	
	/* **************************************************************************
	 *  BEGIN CREATORS (PUBLIC)
	 ************************************************************************** */
	
	/** Creates a new user and saves it to the DB */
	public static UserModel createAndSave(String fbId, String username, String email) {
		return (UserModel) new UserModel(fbId, username, email).doSaveAndRetry();
	}
	
	/* **************************************************************************
	 *  BEGIN SELECTORS
	 ************************************************************************** */
	
	/** Gets all users */
	public static List<UserModel> getAll() {
		return FINDER.all();
	}
	
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
		lastAccessTime = DateUtil.now();
		doUpdateAndRetry();
		Logger.debug(getClass().getCanonicalName() + " " + pk + " last access time updated to " + lastAccessTime);
	}
	
	/** Sets the user login time to the current time */
	public void setLoginTimeAndUpdate() {
		secondToLastLoginTime = lastLoginTime;
		lastLoginTime = DateUtil.now();
		doUpdateAndRetry();
		Logger.debug(getClass().getCanonicalName() + " " + pk + " login time updated to " + lastLoginTime);
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
