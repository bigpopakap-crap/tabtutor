package utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utils for Java reflection
 * 
 * @author bigpopakap
 * @since 2013-03-31
 *
 */
public class ReflectUtil {
	
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
	
	/**
	 * Gets the *first* field of the class with the given annotation
	 * @return the field, or null if no field is found
	 */
	public static List<Field> getFieldsWithAnnotation(Class<?> c, Class<? extends Annotation> ann) {
		List<Field> fields = new LinkedList<>();
		
		for (Field field : c.getFields()) {
			if (field.getAnnotation(ann) != null) {
				fields.add(field);
			}
		}
		
		return fields;
	}

}
