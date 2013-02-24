package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.Formula;

/**
* This ebean maps to the User table, and represents user metadata
* 
* @author bigpopakap@gmail.com
* @since 2013-02-23
*
*/
@Entity
public class User extends Model {

	private static final long serialVersionUID = 5854422586239724109L;
	
	public UUID pk;
	public String username;
	public String fbid;
	public boolean fbIsAuthed;
	public String firstName;
	public String lastName;
	@Formula(select = "select firstName || ' ' || lastName as fullName") public String fullName;
	public String email;
	public Date registerTime;
	public Date lastLoginTime;
	
}
