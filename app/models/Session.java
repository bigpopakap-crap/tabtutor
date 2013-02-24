package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.Formula;

/**
 * This ebean maps to the Session table, and represents the active sessions
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-10
 *
 */
@Entity
public class Session extends Model {
	
	//TODO figure out how to clean up old sessions

	private static final long serialVersionUID = -6111608082703517322L;
	
	public String pk;
	public String userPk;
	public String fbtoken;
	public Date fbtokenExpireTime;
	@Formula(select = "select FALSE from dual") public boolean isFbtokenExpired; //TODO actually do this
	public Date startTime;
	public Date updateTime;
	
}
