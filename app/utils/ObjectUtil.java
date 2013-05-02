package utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Utils for dealing with objects
 * 
 * @author bigpopakap
 * @since 2013-03-31
 *
 */
public class ObjectUtil {
	
	/**
	 * Returns a map of field names to values for this object
	 * @param o
	 */
	public static Map<String, String> getFieldMap(Object o) {
		Map<String, String> map = new HashMap<>();
		if (o == null) return map;
		
		//iterate over columns
		for (Field field : o.getClass().getDeclaredFields()) {
			field.setAccessible(true); //this is okay because we're just reading values
			
			try {
				map.put(field.getName(), String.valueOf(field.get(o)));
			}
			catch (Exception ex) {
				//put a default string here
				map.put(field.getName(), "EX!" + ex.getClass().getSimpleName() + "!");
			}
		}
		
		return map;
	}

}
