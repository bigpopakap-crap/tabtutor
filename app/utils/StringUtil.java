package utils;

/**
 * Utils for dealing with Strings
 * 
 * @author bigpopakap
 * @since 2013-04-07
 *
 */
public final class StringUtil {
	
	private StringUtil() {} //prevent instantiation
	
	/** Shortcut for (str == null || str.isEmpty()) */
	public static boolean isNullOrEmpty(String str) {
		return (str == null || str.isEmpty());
	}
	
	/** Determines if the string is only whitespace. Must not be null */
	public static boolean isOnlyWhitespace(String str) {
		if (str == null) throw new IllegalArgumentException("str cannot be null");
		return str.trim().isEmpty();
	}
	
	/** Determines if this string represents an Integer */
	public static boolean isInteger(String str) {
		if (str == null) return false;
		try {
			Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException ex) {
			return false;
		}
	}
	
	/** Reverses a string */
	public static String reverse(String str) {
		return str == null ? null : new StringBuilder(str).reverse().toString();
	}

}
