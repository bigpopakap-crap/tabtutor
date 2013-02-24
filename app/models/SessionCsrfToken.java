package models;

import java.util.Date;

import javax.persistence.Entity;

import com.avaje.ebean.annotation.Formula;

import play.db.ebean.Model;

/**
* This ebean maps to the SessionCsrfToken table, mapping session IDs to active CSRF tokens
* 
* @author bigpopakap@gmail.com
* @since 2013-02-23
*
*/
@Entity
public class SessionCsrfToken extends Model {
	
	//TODO figure out how to clean up expired tokens

	private static final long serialVersionUID = 1065279771090088334L;
	
	public String sessionPk;
	public String csrfToken;
	public Date createTime;
	public Date expireTime;
	@Formula(select = "select FALSE from dual") public boolean isExpired; //TODO actually implement this

}
