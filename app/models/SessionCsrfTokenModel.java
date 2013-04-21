package models;

import globals.Globals.DevelopmentSwitch;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import utils.DateUtil;

import com.avaje.ebean.annotation.Formula;

import contexts.SessionContext;

/**
* This Ebean maps to the SessionCsrfToken table, mapping session IDs to active CSRF tokens
* 
* @author bigpopakap
* @since 2013-02-23
*
*/
@Entity
@Table(name = "SessionCsrfToken")
public class SessionCsrfTokenModel extends BaseModel {
	
	public static final DevelopmentSwitch<Integer> CSRF_TOKEN_LIFETIME_SECONDS = new DevelopmentSwitch<>(5 * 60);
	
	//TODO figure out how to clean up expired tokens

	private static final long serialVersionUID = 1065279771090088334L;
	
	@Column(name = "csrfToken") @Id public UUID csrfToken;
	@Column(name = "createTime") public Date createTime;
	@Column(name = "expireTime") public Date expireTime;
	
	@ManyToOne @JoinColumn(name = "sessionPk", referencedColumnName = "pk") public SessionModel session;
	
	@Transient @Formula(select = "(NOW() > expireTime)") public boolean isExpired;
	
	public SessionModel getSession() { return session; }
	public UUID getCsrfToken() { return UUID.fromString(csrfToken.toString()); } //defensive copy
	public Date getCreateTime() { return (Date) createTime.clone(); } //defensive copy
	public Date getExpireTime() { return (Date) expireTime.clone(); } //defensive copy
	public boolean isExpired() { return isExpired; }
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, SessionCsrfTokenModel> FINDER = new Finder<>(
		UUID.class, SessionCsrfTokenModel.class
	);
	
	/* **************************************************************************
	 *  BEGIN CONSTRUCTORS (PRIVATE)
	 ************************************************************************** */
	
	/**
	 * Creates a user with the given information
	 * Username will be some default value
	 */
	private SessionCsrfTokenModel(SessionModel session) {
		Date now = DateUtil.now();
		
		this.session = session;
		this.csrfToken = UUID.randomUUID();
		this.createTime = now;
		this.expireTime = DateUtil.add(now, CSRF_TOKEN_LIFETIME_SECONDS.get());
	}
	
	/* **************************************************************************
	 *  BEGIN CREATORS (PUBLIC)
	 ************************************************************************** */
	
	/** Creates a new token for this session */
	public static SessionCsrfTokenModel create() {
		if (!SessionContext.hasSession()) {
			throw new IllegalStateException("CSRF token should only ever be created when there is a session");
		}
		else {
			return (SessionCsrfTokenModel) new SessionCsrfTokenModel(SessionContext.session()).doSaveAndRetry();
		}
	}
	
	/* **************************************************************************
	 *  BEGIN VALIDATORS
	 ************************************************************************** */
		
	/** Determines if a User exists with the given ID */
	public static boolean isValidToken(String token) {
		if (!SessionContext.hasSession()) {
			throw new IllegalStateException("Cannot validate CSRF token when there is no session");
		}
		
		//check the database for the token and make sure it matches this session
		SessionCsrfTokenModel csrfToken = FINDER.where().eq("csrfToken", token).findUnique();
		return csrfToken != null && SessionContext.session().equals(csrfToken.getSession());
	}
	
}
