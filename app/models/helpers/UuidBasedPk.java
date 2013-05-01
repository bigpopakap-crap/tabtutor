package models.helpers;

import java.util.UUID;

import javax.persistence.Embeddable;

/**
 * Class to use instead of UUID, since we can control how it prints out
 * Pk stands for Primary Key
 * 
 * @author bigpopakap
 * @since 2013-04-29
 *
 */
@Embeddable
public class UuidBasedPk implements Cloneable {
	
	/** The string that defines the pk */
	private final String pk;
	
	/** Creates a Base 64 pk from the given UUID */
	private UuidBasedPk(UUID pk) {
		this(uuidToString(pk));
	}
	
	/** Uses the given pk */
	private UuidBasedPk(String pk) {
		if (pk == null) throw new IllegalArgumentException("pk cannot be null");
		
		//TODO verify that this  is a valid pk
		//throw new IllegalArgumentException("pk is not a valid pk");
		
		this.pk = pk;
	}
	
	/** Creates a random Pk by creating a random UUID and converting it to base 64 */
	public static UuidBasedPk randomPk() {
		return new UuidBasedPk(UUID.randomUUID());
	}
	
	/** Creates a new Pk from the String representing another Pk */
	public static UuidBasedPk fromString(String pk) {
		return new UuidBasedPk(pk);
	}
	
	public String getPk() {
		return pk;
	}
	
	/* **************************************************************************
	 *  BEGIN INTERFACE OVERRIDES
	 ************************************************************************** */
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		else if (!this.getClass().isInstance(obj)) {
			return false;
		}
		else {
			return getPk().equals(((UuidBasedPk) obj).getPk());
		}
	}
	
	@Override
	public int hashCode() {
		return pk.hashCode();
	}
	
	@Override
	public String toString() {
		return getPk();
	}

	@Override
	public UuidBasedPk clone() {
		return new UuidBasedPk(getPk());
	}
	
	/* **************************************************************************
	 *  BEGIN PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Convert the UUID to the representation desired for primary keys */
	private static String uuidToString(UUID uuid) {
		if (uuid == null) throw new IllegalArgumentException("uuid cannot be null");
		return uuid.toString().replaceAll("-", "");
	}
	
}
