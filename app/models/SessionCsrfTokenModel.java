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
* This Ebean maps to the SessionCsrfToken table, mapping session IDs to active CSRF tokens
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
	
	@Column(name = "sessionPk") @Id public UUID sessionPk;
	@Column(name = "csrfToken") public UUID csrfToken;
	@Column(name = "createTime") public Date createTime;
	@Column(name = "expireTime") public Date expireTime;
	@Formula(select = "FALSE") public boolean isExpired; //TODO actually implement this
	
	public static final Finder<UUID, SessionCsrfTokenModel> FINDER = new Finder<UUID, SessionCsrfTokenModel>(
		UUID.class, SessionCsrfTokenModel.class
	);

}
