package utils;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

/**
 * Class to use instead of UUID, since it will print in base64 without the dashes
 * Pk stands for Primary Key
 * 
 * @author bigpopakap
 * @since 2013-04-29
 *
 */
public class Pk implements CharSequence, Cloneable {
	
	/** The string that defines the pk */
	private final String pk;
	
	/** Creates a Base 64 pk from the given UUID */
	private Pk(UUID pk) {
		this(uuidToBase64(pk));
	}
	
	/** Uses the given pk */
	private Pk(String pk) {
		if (pk == null) throw new IllegalArgumentException("pk cannot be null");
		this.pk = pk;
	}
	
	/** Creates a random Pk by creating a random UUID and converting it to base 64 */
	public static Pk randomPk() {
		return new Pk(UUID.randomUUID());
	}
	
	/* **************************************************************************
	 *  BEGIN INTERFACE OVERRIDES
	 ************************************************************************** */
	
	@Override
	public String toString() {
		return pk;
	}

	@Override
	public char charAt(int index) {
		return toString().charAt(index);
	}

	@Override
	public int length() {
		return toString().length();
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return toString().subSequence(start, end);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Pk(pk);
	}
	
	/* **************************************************************************
	 *  BEGIN PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Converts UUID to Base64
	 *  From http://stackoverflow.com/questions/772802/storing-uuid-as-base64-string/ */
	private static String uuidToBase64(UUID uuid) {
		if (uuid == null) throw new IllegalArgumentException("uuid cannot be null");
		
	    Base64 base64 = new Base64();
	    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
	    bb.putLong(uuid.getMostSignificantBits());
	    bb.putLong(uuid.getLeastSignificantBits());
	    return String.valueOf(base64.encode(bb.array()));
	}
	
}
