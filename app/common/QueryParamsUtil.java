package common;

import java.util.Map;

import common.EscapingUtil.Escaper;

/**
 * Utility methods for dealing with query strings
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-03
 *
 */
public abstract class QueryParamsUtil {
	
	/**
	 * Converts a map of key-value pairs to a query string
	 */
	public static String mapToQueryString(Map<String, String> map) {
		StringBuilder str = new StringBuilder();
		for (String key : map.keySet()) {
			str.append(str.length() > 0 ? "&" : "")
			   .append(EscapingUtil.escape(key, Escaper.URL))
			   .append("=")
			   .append(EscapingUtil.escape(map.get(key), Escaper.URL));
		}
		return str.toString();
	}

}
