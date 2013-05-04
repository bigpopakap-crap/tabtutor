package utils;

import helpers.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Class for dealing with exceptions
 * 
 * @author bigpopakap
 * @since 2013-04-29
 *
 */
public final class ExceptionUtil {
	
	private ExceptionUtil() {} //prevent instantiation
	
	/**
	 * Gets the stack trace of the given exception as a String, in the
	 * same format as {@link Throwable#printStackTrace()}
	 */
	public static String printStackTrace(Throwable t) {
		if (t == null) return "";
		
		StringWriter stringWriter = null;
		PrintWriter printWriter = null;
		try {
			stringWriter = new StringWriter();
			printWriter = new PrintWriter(stringWriter);
			t.printStackTrace(printWriter);
			return stringWriter.toString();
		}
		finally {
			try {
				if (printWriter != null) printWriter.close();
				if (stringWriter != null) stringWriter.close();
			}
			catch (Exception ex) {
				Logger.warn("Exception caught in finally", ex);
			}
		}
	}

}
