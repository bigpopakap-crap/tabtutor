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
* This Ebean maps to the User table, and represents user metadata
* 
* @author bigpopakap@gmail.com
* @since 2013-02-23
*
*/
@Entity
@Table(name = "User")
public class UserModel extends BaseModel {

	private static final long serialVersionUID = 5854422586239724109L;
	
	@Column(name = "pk") @Id private UUID pk;
	@Column(name = "username") private String username;
	@Column(name = "fbId") private String fbId;
	@Column(name = "fbIsAuthed") private boolean fbIsAuthed;
	@Column(name = "firstName") private String firstName;
	@Column(name = "lastName") private String lastName;
	@Transient @Formula(select = "firstName || ' ' || lastName") private String fullName;
	@Column(name = "email") private String email;
	@Column(name = "registerTime") private Date registerTime;
	@Column(name = "lastLoginTime") private Date lastLoginTime;
	@Column(name = "secondToLastLoginTime") private Date secondToLastLoginTime;
	@Transient @Formula(select = "secondToLastLoginTime IS NULL") private boolean isFirstLogin;
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, UserModel> FINDER = new Finder<UUID, UserModel>(
		UUID.class, UserModel.class
	);
	
	public static class Factory extends BaseFactory {}
	
	public class Getter extends BaseGetter {
		
		public UUID pk() { return UUID.fromString(pk.toString()); } //defensive copy
		public String username() { return username; }
		public String fbId() { return fbId; }
		public boolean fbIsAuthed() { return fbIsAuthed; }
		public String firstName() { return firstName; }
		public String lastName() { return lastName; }
		public String fullName() { return fullName; }
		public String email() { return email; }
		public Date lastLoginTime() { return (Date) lastLoginTime.clone(); } //defensive copy
		public Date secondToLastLoginTime() { return (Date) secondToLastLoginTime.clone(); } //defensive copy
		public boolean isFirstLogin() { return isFirstLogin; }
		
	}
	
	public static class Selector extends BaseSelector {}
	
	public static class Updater extends BaseUpdater {}
	
	public static class Validator extends BaseValidator {}
	
}
