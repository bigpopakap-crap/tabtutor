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
* This Ebean maps to the User table, and represents user metadata
* 
* @author bigpopakap@gmail.com
* @since 2013-02-23
*
*/
@Entity
@Table(name = "User")
public class UserModel extends Model {

	private static final long serialVersionUID = 5854422586239724109L;
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "username") public String username;
	@Column(name = "fbId") public String fbId;
	@Column(name = "fbIsAuthed") public boolean fbIsAuthed;
	@Column(name = "firstName") public String firstName;
	@Column(name = "lastName") public String lastName;
	@Formula(select = "firstName || ' ' || lastName") public String fullName;
	@Column(name = "email") public String email;
	@Column(name = "registerTime") public Date registerTime;
	@Column(name = "lastLoginTime") public Date lastLoginTime;
	
	public static final Finder<UUID, UserModel> FINDER = new Finder<UUID, UserModel>(
		UUID.class, UserModel.class
	);
	
}
