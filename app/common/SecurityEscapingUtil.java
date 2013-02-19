package common;

import java.net.URLEncoder;

/**
 * This class contains utility methods for escaping strings for various environments
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-19
 *
 */
public class SecurityEscapingUtil {
	
	//TODO unit test this class
	
	/**
	 * Escapes the given strings for the given environment
	 * @param str the string to escape. If null, null will be returned
	 * @param escapers the Escapers to apply, in order
	 * @return a String resulting from escaping the input string with each Escaper in order
	 */
	public static String escape(String str, Escaper...escapers) {
		if (str == null) return null;
		for (Escaper e : escapers) {
			str = e.escape(str);
		}
		return str;
	}
	
	/**
	 * This enum holds all the environments for which strings can be escaped. They each
	 * implement an escape() method that does the escaping, but this escape method
	 * is protected so that outside classes cannot call them directly. They must instead use
	 * the escape() method provided in SecurityEscapingUtil to escape strings
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-19
	 *
	 */
	public static enum Escaper {
		NONE {
			@Override
			protected String escape(String str) {
				return str;
			}
		},
		URL {
			@Override
			protected String escape(String str) {
				//TODO use a non-deprecated method of doing this
				return URLEncoder.encode(str);
			}
		},
		HTML_ATTR {},
		HTML_TEXT {},
		JS {},
		SQL {};

		protected String escape(String str) {
			throw new UnsupportedOperationException("The following esaper is yet undefined: " + this.name());
		}
	}

}
