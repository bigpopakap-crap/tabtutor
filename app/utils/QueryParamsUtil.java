package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import utils.EscapingUtil.Escaper;

/**
 * Utility methods for dealing with query strings
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-03
 *
 */
public abstract class QueryParamsUtil {
	
	/** Converts a map of key-value pairs to a query string */
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
	
	/** Converts a query string to a map of key-value pairs
	 *  The input can be a full URL, or just the params, and may start with a ? or not
	 *  
	 *  DOES NOT do url-decoding
	 *  
	 *  Note that keys are overwritten as they are found, so if there are multiple
	 *  of the same key in the query string, the first value is the one that will be kept
	 *  
	 *  */
	public static Map<String, String> queryStringToMap(String params) {
		if (params == null || params.isEmpty()) {
			return new TreeMap<String, String>();
		}
		
		Map<String, String> map = new HashMap<String, String>();
		
		//start after the question mark, or at the beginning of the string
		int startIndex = params.indexOf('?') + 1;
		if (startIndex >= params.length()) {
			//there is not more of the string remaining
			return map;
		}
		
		//split the string by ampersands
		String[] pairs = params.substring(startIndex).split("&");
		for (String pair : pairs) {
			String[] keyValue = pair.split("=");
			map.put(keyValue[0], keyValue[1]);
		}
		
		return map;
	}

}
