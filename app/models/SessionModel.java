package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import types.SqlOperationType.BasicDmlModifyingType;
import utils.DbTypesUtil;

import com.avaje.ebean.annotation.Formula;

import contexts.BaseContext.ContextKey;
import contexts.SessionContext;

/**
 * This Ebean maps to the Session table, and represents the active sessions
 * 
 * @author bigpopakap
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
	public static final ContextKey SESSION_OBJ_CONTEXT_KEY = ContextKey.register("sessionObjectContextKey");
	
	private static final long serialVersionUID = -6111608082703517322L;
	
	/* **************************************************************************
	 *  BEGIN FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "userPk") public UUID userPk; //TODO how to populate this as the user object reference?
	@Column(name = "fbToken") public String fbToken;
	@Column(name = "fbTokenExpireTime") public Date fbTokenExpireTime;
	@Transient @Formula(select = "NOW() > fbTokenExpireTime") public boolean isFbtokenExpired;
	@Column(name = "startTime") public Date startTime;
	@Column(name = "lastAccessTime") public Date lastAccessTime;
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getPk_String() { return getPk().toString(); }
	public UUID getUserPk() { return UUID.fromString(userPk.toString()); }
	public String getFbToken() { return fbToken; }
	public Date getFbTokenExpireTime() { return (Date) fbTokenExpireTime.clone(); } //defensive copy
	public boolean isFbtokenExpired() { return isFbtokenExpired; }
	public Date getStartTime() { return (Date) startTime.clone(); } //defensive copy
	public Date getLastAccessTime() { return (Date) lastAccessTime.clone(); } //defensive copy
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, SessionModel> FINDER = new Finder<>(
		UUID.class, SessionModel.class
	);
	
	/* **************************************************************************
	 *  BEGIN HOOKS
	 ************************************************************************** */
	
	@Override
	protected void hook_postModifyingOperation(BasicDmlModifyingType opType, boolean wasSuccessful) {
		super.hook_postModifyingOperation(opType, wasSuccessful);
		
		//refresh the app context if the operation was successful
		if (wasSuccessful) SessionContext.refresh();
		
		//TODO add caching here
	}
	
	/* **************************************************************************
	 *  BEGIN CONSTRUCTORS (PRIVATE)
	 ************************************************************************** */
	
	/**
	 * Creates a default new session with a random ID, no associated user,
	 * no Facebook auth information, and the current start and update times.
	 */
	private SessionModel() {
		Date now = DbTypesUtil.now();
		
		this.pk = UUID.randomUUID();
		this.userPk = null;
		this.fbToken = null;
		this.fbTokenExpireTime = null;
		this.startTime = now;
		this.lastAccessTime = now;
	}
	
	/* **************************************************************************
	 *  BEGIN CREATORS (PUBLIC)
	 ************************************************************************** */
	
	/** Creates a new session and saves it to the DB */
	public static SessionModel create() {
		SessionModel session = new SessionModel();
		session.doSaveAndRetry();
		return session;
	}
	
	/* **************************************************************************
	 *  BEGIN SELECTORS
	 ************************************************************************** */
	
	/** Gets a Session by ID, converts the string to a UUID internally */
	public static SessionModel getById(String id) {
		try {
			return getById(id != null ? UUID.fromString(id) : null);
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
	public UserModel getUser() {
		return hasValidUserPk()
				? UserModel.getById(userPk)
				: null;
	}
	
	/* **************************************************************************
	 *  BEGIN UPDATERS
	 ************************************************************************** */
	
	/**
	 * Adds the Facebook auth token and expiry time to the session
	 * @param session the session to alter
	 * @param token the auth token
	 * @param seconds number of seconds until the token expires
	 */
	public void setFbAuthInfoAndUpdate(String token, int seconds) {
		fbToken = token;
		fbTokenExpireTime = DbTypesUtil.add(DbTypesUtil.now(), seconds);
		doUpdateAndRetry();
	}
	
	/** Adds the user pk to the session. Assumes that the given userPk is valid */
	public void setUserPkAndUpdate(UUID userPk) {
		this.userPk = userPk;
		doUpdateAndRetry();
	}
	
	/* **************************************************************************
	 *  BEGIN VALIDATORS
	 ************************************************************************** */
		
	/** Determines if the given ID is valid and it exists in the database */
	public static boolean isValidExistingId(String id) {
		return getById(id) != null;
	}
	
	/**
	 * @param session the session to check
	 * @return true if this session has a Facebook auth token that has not expired
	 */
	public boolean hasValidFbAuthInfo() {
		return fbToken != null && !isFbtokenExpired;
	}
	
	/**
	 * @param session the session to check
	 * @return true if the session has a reference to a user
	 */
	public boolean hasValidUserPk() {
		return userPk != null && UserModel.isValidExistingId(userPk);
	}
		
}
