package common;

import java.util.Date;

/**
 * Defines helpers pertaining to Database types
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-24
 *
 */
public abstract class DbTypesUtil {
	
	/** Sugar for creating a Date representing the current time */
	public static Date now() {
		return new Date();
	}
	
	/** Returns a new Date, which is the given number of seconds after the given Date
	 */
	public static Date add(Date date, int seconds) {
		return new Date(date.getTime() + (seconds * 1000));
	}

}
