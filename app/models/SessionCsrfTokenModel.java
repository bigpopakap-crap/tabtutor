package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.Formula;

/**
* This ebean maps to the SessionCsrfToken table, mapping session IDs to active CSRF tokens
* 
* @author bigpopakap@gmail.com
* @since 2013-02-23
*
*/
@Entity
@Table(name = "SessionCsrfToken")
public class SessionCsrfTokenModel extends Model {
	
	//TODO figure out how to clean up expired tokens

	private static final long serialVersionUID = 1065279771090088334L;
	
	public UUID sessionPk;
	public UUID csrfToken;
	public Date createTime;
	public Date expireTime;
	@Formula(select = "select FALSE from dual") public boolean isExpired; //TODO actually implement this
	
	public static final Finder<UUID, SessionCsrfTokenModel> FINDER = new Finder<UUID, SessionCsrfTokenModel>(
		UUID.class, SessionCsrfTokenModel.class
	);

}
