package utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Class for dealing with exceptions
 * 
 * @author bigpopakap
 * @since 2013-04-29
 *
 */
public abstract class ExceptionUtil {
	
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
