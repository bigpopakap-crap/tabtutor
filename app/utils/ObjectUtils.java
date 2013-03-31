package utils;

import java.lang.reflect.Field;

/**
 * Utils for dealing with objects
 * 
 * @author bigpopakap
 * @since 2013-03-31
 *
 */
public class ObjectUtils {
	
	public static String getFieldsToString(Object o) {
		if (o == null) return "null";
		
		StringBuilder str = new StringBuilder();
		str.append(o.getClass()).append(":[\n");
		
		//iterate over columns
		for (Field field : o.getClass().getDeclaredFields()) {
			field.setAccessible(true); //this is okay because we're just reading values
			
			String value = null;
			boolean valueIsException = false;
			try {
				value = field.get(o).toString();
			}
			catch (Exception ex) {
				value = ex.getClass().getName();
				valueIsException = true;
			}
			
			str.append("\t")
				.append(field.getName())
				.append(valueIsException ? " threw " : " = ")
				.append(value)
				.append("\n");
		}
		
		str.append("]");
		return str.toString();
	}

}
