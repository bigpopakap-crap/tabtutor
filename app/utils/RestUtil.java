package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import utils.EscapingUtil.Escaper;

/**
 * Utils for dealing with query strings and stuff
 * 
 * @author bigpopakap
 * @since 2013-04-19
 *
 */
public final class RestUtil {
	
	private RestUtil() {} //prevent instantiation
	
	/** Converts a map of key-value pairs to a query string without the leading "?" */
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
	
	/** 
	 * The same as {@link #mapToQueryString(Map)}, but the map is represented as
	 * pairs of strings
	 * 
	 * Example: mapToQueryStrign("key1", "val1", "key1", "val1")
	 * 
	 * @throws IllegalArgumentException if there are not an even number of arguments
	 */
	public static String mapToQueryString(String... params) throws IllegalArgumentException {
		if (params.length % 2 != 0) throw new IllegalArgumentException("There must be an even number of params");
		
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < params.length; i += 2) {
			map.put(params[i], params[i + 1]);
		}
		
		return mapToQueryString(map);
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
			return new TreeMap<>();
		}
		
		Map<String, String> map = new HashMap<>();
		
		//TODO make this more efficient by using regex
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
	
	/** Returns a map that just picks the first element of the array for each value */
	public static Map<String, String> arrayMapToMap(Map<String, String[]> arrayMap) {
		Map<String, String> params = new HashMap<String, String>();
		
		for(Entry<String, String[]> entry : arrayMap.entrySet()) {
			String key = entry.getKey();
			String[] value = entry.getValue();
			
			if (value != null && value.length > 0) {
				params.put(key, value[0]);
			}
		}
		
		return params;
	}

}
