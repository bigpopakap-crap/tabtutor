package utils;

/**
 * Utils for dealing with Strings
 * 
 * @author bigpopakap
 * @since 2013-04-07
 *
 */
public abstract class StringUtil {
	
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

}
