package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.Formula;
import common.DbTypesUtil;

/**
 * This Ebean maps to the Session table, and represents the active sessions
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-10
 *
 */
@Entity
@Table(name = "Session")
public class SessionModel extends Model {
	
	//TODO figure out how to clean up old sessions
	
	private static final long serialVersionUID = -6111608082703517322L;
	
	public SessionModel() {
		this(null, null, null);
	}
	public SessionModel(UUID userPk, String fbtoken, Date fbtokenExpireTime) {
		this(UUID.randomUUID(), userPk, fbtoken, fbtokenExpireTime, DbTypesUtil.now(), DbTypesUtil.now());
	}
	public SessionModel(UUID pk, UUID userPk,
						String fbtoken, Date fbtokenExpireTime,
						Date startTime, Date lastAccessTime) {
		this.pk = pk;
		this.userPk = userPk;
		this.fbToken = fbtoken;
		this.fbTokenExpireTime = fbtokenExpireTime;
		this.startTime = startTime;
		this.lastAccessTime = lastAccessTime;
	}
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "userPk") public UUID userPk;
	@Column(name = "fbToken") public String fbToken;
	@Column(name = "fbTokenExpireTime") public Date fbTokenExpireTime;
	@Transient @Formula(select = "NOW() > fbTokenExpireTime") public boolean isFbtokenExpired;
	@Column(name = "startTime") public Date startTime;
	@Column(name = "lastAccessTime") public Date lastAccessTime;
	
	public static final Finder<UUID, SessionModel> FINDER = new Finder<UUID, SessionModel>(
		UUID.class, SessionModel.class
	);
	
}
