package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.Formula;

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
		//TODO actually implement the expireTime
		this(UUID.randomUUID(), userPk, fbtoken, fbtokenExpireTime, new Date(), new Date(), new Date());
	}
	public SessionModel(UUID pk, UUID userPk, String fbtoken, Date fbtokenExpireTime,
						Date startTime, Date updateTime, Date expireTime) {
		this.pk = pk;
		this.userPk = userPk;
		this.fbToken = fbtoken;
		this.fbTokenExpireTime = fbtokenExpireTime;
		this.startTime = startTime;
		this.updateTime = updateTime;
		this.expireTime = expireTime;
	}
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "userPk") public UUID userPk;
	@Column(name = "fbToken") public String fbToken;
	@Column(name = "fbTokenExpireTime") public Date fbTokenExpireTime;
	@Formula(select = "FALSE") public boolean isFbtokenExpired; //TODO actually do this
	@Column(name = "startTime") public Date startTime;
	@Column(name = "updateTime") public Date updateTime;
	@Column(name = "expireTime") public Date expireTime;
	
	public static final Finder<UUID, SessionModel> FINDER = new Finder<UUID, SessionModel>(
		UUID.class, SessionModel.class
	);
	
}
