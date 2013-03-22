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
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, SessionCsrfTokenModel> FINDER = new Finder<UUID, SessionCsrfTokenModel>(
		UUID.class, SessionCsrfTokenModel.class
	);
	
	public static class Factory extends BaseFactory {}
	
	public class Getter extends BaseGetter {
		
		public UUID sessionPk() { return UUID.fromString(sessionPk.toString()); } //defensive copy
		public UUID csrfToken() { return UUID.fromString(csrfToken.toString()); } //defensive copy
		public Date createTime() { return (Date) createTime.clone(); } //defensive copy
		public Date expireTime() { return (Date) expireTime.clone(); } //defensive copy
		public boolean isExpired() { return isExpired; }
		
	}
	
	public static class Selector extends BaseSelector {}
	
	public static class Updater extends BaseUpdater {}
	
	public static class Validator extends BaseValidator {}
	
}
