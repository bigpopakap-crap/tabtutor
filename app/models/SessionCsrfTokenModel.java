package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.avaje.ebean.annotation.Formula;

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
	
	//TODO figure out how to clean up expired tokens

	private static final long serialVersionUID = 1065279771090088334L;
	
	@Column(name = "sessionPk") @Id public UUID sessionPk;
	@Column(name = "csrfToken") public UUID csrfToken;
	@Column(name = "createTime") public Date createTime;
	@Column(name = "expireTime") public Date expireTime;
	
	@Transient @Formula(select = "NOW() > expireTime") public boolean isExpired;
	
	public UUID getSessionPk() { return UUID.fromString(sessionPk.toString()); } //defensive copy
	public UUID getCsrfToken() { return UUID.fromString(csrfToken.toString()); } //defensive copy
	public Date getCreateTime() { return (Date) createTime.clone(); } //defensive copy
	public Date getExpireTime() { return (Date) expireTime.clone(); } //defensive copy
	public boolean isExpired() { return isExpired; }
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, SessionCsrfTokenModel> FINDER = new Finder<>(
		UUID.class, SessionCsrfTokenModel.class
	);
	
}
