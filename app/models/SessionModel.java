package models;

import helpers.Logger;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.annotations.CreateTime;
import models.base.BaseModel;
import types.SqlOperationType.BasicDmlModifyingType;
import utils.DateUtil;

import com.avaje.ebean.annotation.Formula;

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
	
	private static final long serialVersionUID = -6111608082703517322L;
	
	/* **************************************************************************
	 *  BEGIN FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "fbToken") public String fbToken;
	@Column(name = "fbTokenExpireTime") public Date fbTokenExpireTime;
	@Column(name = "startTime") @CreateTime public Date startTime;
	@Column(name = "lastAccessTime") public Date lastAccessTime;
	
	@OneToOne @JoinColumn(name = "userPk") public UserModel user;
	@OneToMany(fetch = FetchType.LAZY) @JoinColumn(name = "sessionPk", referencedColumnName = "pk") public Set<SessionCsrfTokenModel> csrfTokens;
	
	@Transient @Formula(select = "(NOW() > fbTokenExpireTime)") public boolean isFbtokenExpired;
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getPk_String() { return getPk().toString(); }
	public UserModel getUser() { return user; }
	public String getFbToken() { return fbToken; }
	public Date getFbTokenExpireTime() { return (Date) fbTokenExpireTime.clone(); } //defensive copy
	public boolean isFbtokenExpired() { return isFbtokenExpired; }
	public Date getStartTime() { return (Date) startTime.clone(); } //defensive copy
	public Date getLastAccessTime() { return (Date) lastAccessTime.clone(); } //defensive copy
	public Set<SessionCsrfTokenModel> getCsrfTokens() { return csrfTokens; }
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, SessionModel> FINDER = new Finder<>(
		UUID.class, SessionModel.class
	);
	
	/* **************************************************************************
	 *  BEGIN HOOKS
	 ************************************************************************** */
	
	@Override
	protected void hook_preModifyingOperation(BasicDmlModifyingType opType, boolean isFirstTry) {
		if (isFirstTry && opType == BasicDmlModifyingType.INSERT) {
			//make sure the access time is not before the start time
			lastAccessTime = startTime;
		}
	}
	
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
		Date now = DateUtil.now();
		
		this.pk = UUID.randomUUID();
		this.user = null;
		this.fbToken = null;
		this.fbTokenExpireTime = null;
		this.startTime = now;
		this.lastAccessTime = now;
	}
	
	/* **************************************************************************
	 *  BEGIN CREATORS (PUBLIC)
	 ************************************************************************** */
	
	/** Creates a new session and saves it to the DB */
	public static SessionModel createAndSave() {
		return (SessionModel) new SessionModel().doSaveAndRetry();
	}
	
	/* **************************************************************************
	 *  BEGIN SELECTORS
	 ************************************************************************** */
	
	/** Gets a Session by ID, converts the string to a Pk internally */
	public static SessionModel getByPk(String pk) {
		try {
			return getByPk(pk != null ? UUID.fromString(pk) : null);
		}
		catch (IllegalArgumentException ex) {
			//the string was not a valid Pk
			return null;
		}
	}
	
	/** Gets a Session by ID */
	public static SessionModel getByPk(UUID pk) {
		return pk != null ? FINDER.byId(pk) : null;
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
		fbTokenExpireTime = DateUtil.add(DateUtil.now(), seconds);
		doUpdateAndRetry();
	}
	
	/** Adds the user pk to the session. Assumes that the given userPk is valid */
	public void setUserAndUpdate(UserModel user) {
		this.user = user;
		doUpdateAndRetry();
	}
	
	/** Sets the session last access time to the current time */
	public void setLastAccessTimeAndUpdate() {
		lastAccessTime = DateUtil.now();
		doUpdateAndRetry();
		Logger.debug(getClass().getCanonicalName() + " " + pk + " last access time updated to " + lastAccessTime);
	}
	
	/* **************************************************************************
	 *  BEGIN VALIDATORS
	 ************************************************************************** */
		
	/** Determines if the given ID is valid and it exists in the database */
	public static boolean isValidExistingId(String id) {
		return getByPk(id) != null;
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
	public boolean hasUser() {
		return user != null;
	}
		
}
