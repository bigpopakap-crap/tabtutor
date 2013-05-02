package helpers;

/**
 * Singleton Logger for the app
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 */
public abstract class Logger {
	
	//TODO ensure that classes only use this Logger, not play.Logger or anything else
	//TODO test that this logging level enum matches the Javascript logger's levels

	/**
	 * Logging levels
	 * 
	 * @author bigpopakap
	 * @since 2013-04-17
	 *
	 */
	public static enum Level {
		NONE, TRACE, DEBUG, INFO, WARN, ERROR;
	}
	
	/** Logs a message at the given level. Includes a throwable to print the stack trace */
	public static synchronized void log(Level level, String msg, Throwable cause) {
		switch (level) {
			case NONE:
				break; //don't log
			case TRACE:
				if (cause != null) play.Logger.trace(msg, cause);
				else play.Logger.trace(msg);
				break;
			case DEBUG:
				if (cause != null) play.Logger.debug(msg, cause);
				else play.Logger.debug(msg);
				break;
			case INFO:
				if (cause != null) play.Logger.info(msg, cause);
				else play.Logger.info(msg);
				break;
			case WARN:
				if (cause != null) play.Logger.warn(msg, cause);
				else play.Logger.warn(msg);
				break;
			case ERROR:
				if (cause != null) play.Logger.error(msg, cause);
				else play.Logger.error(msg);
				break;
			default:
				//log at info and log an error of unhandled logger level
				if (cause != null) play.Logger.info(msg, cause);
				else play.Logger.info(msg);
				
				play.Logger.error("Unhandled logger level: " + level);
				break;
		}
	}
	
	/** Logs a message at the given level */
	public static synchronized void log(Level level, String msg) {
		log(level, msg, null);
	}
	
	/** Shortcut for logging at Trace level */
	public static synchronized void trace(String msg, Throwable cause) {
		log(Level.TRACE, msg, cause);
	}
	
	/** Shortcut for logging at Trace level */
	public static synchronized void trace(String msg) {
		trace(msg, null);
	}
	
	/** Shortcut for logging at Trace level */
	public static synchronized void debug(String msg, Throwable cause) {
		log(Level.DEBUG, msg, cause);
	}
	
	/** Shortcut for logging at Trace level */
	public static synchronized void debug(String msg) {
		debug(msg, null);
	}
	
	/** Shortcut for logging at Info level */
	public static synchronized void info(String msg, Throwable cause) {
		log(Level.INFO, msg, cause);
	}
	
	/** Shortcut for logging at Info level */
	public static synchronized void info(String msg) {
		info(msg, null);
	}
	
	/** Shortcut for logging at Warn level */
	public static synchronized void warn(String msg, Throwable cause) {
		log(Level.WARN, msg, cause);
	}
	
	/** Shortcut for logging at Warn level */
	public static synchronized void warn(String msg) {
		warn(msg, null);
	}
	
	/** Shortcut for logging at Error level */
	public static synchronized void error(String msg, Throwable cause) {
		log(Level.ERROR, msg, cause);
	}
	
	/** Shortcut for logging at Error level */
	public static synchronized void error(String msg) {
		error(msg, null);
	}
	
}
