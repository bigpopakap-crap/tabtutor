package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

/**
 * 
 * This ebean maps to the Session table
 * 
 * @author bigpopakap@gmail.com
 * @since 2013/02/10
 *
 */
@Entity
@Table(name="Session")
public class Session extends Model {

	private static final long serialVersionUID = -6111608082703517322L;
	
	@Id public Integer pk;
	public Integer user_pk;
	public Timestamp createdTime;
	public Timestamp lastUpdatedTime;
	
}
