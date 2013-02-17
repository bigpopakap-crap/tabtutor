package common;

import play.Play;

/**
 * This class holds system environment information (the app context), which includes
 * the mode it is running in, and the config/environment variables
 * 
 * The rest of the code should never directly access System.getenv() or the Play class, because
 * all environment variables will be loaded into this classS
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-16
 *
 */
public abstract class AppCtx {

	/**
	 * This enum holds all config/environment vars that the app should access
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-16
	 *
	 */
	public static enum Var {
		
		DOMAIN("WTF_DOMAIN"),
		PORT("WTF_HTTP_PORT"),
		FB_APP_ID("WTF_FB_APP_ID"),
		FB_APP_SECRET("WTF_FB_APP_SECRET", true);
		
		private final boolean isSecuredSecret;	//indicates that this should never be surfaced in the UI
		private final String key;
		private final String val;
		
		private Var(String key) {
			this(key, false);
		}
		
		private Var(String key, boolean isSecret) {
			this.key = key;
			this.val = System.getenv(this.key);
			this.isSecuredSecret = isSecret;
		}
		
		/** Gets the name of the environment variable as it was defined */
		public synchronized String key() {
			return key;
		}
		
		/** Gets the value of the environment variable */
		public synchronized String val() {
			return val;
		}
		
		/** Returns true if this is a guarded secret */
		public boolean isSecuredSecret() {
			return isSecuredSecret;
		}
		
	}
	
	/**
	 * This enum holds the possible app modes, as well as methods to query the current
	 * mode the app is running
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-16
	 *
	 */
	public static enum Mode {
		DEVELOPMENT, STAGING, PRODUCTION;
		
		private static final Mode WTF_MODE = Mode.valueOf(System.getenv("WTF_MODE"));
		
		/** Gets the current mode in which the app is running */
		public static synchronized Mode get() {
			return WTF_MODE;
		}
		
		/** Returns true if the app is in development mode */
		public static synchronized boolean isDevelopment() {
			return get() == DEVELOPMENT;
		}
		
		/** Returns true if the app is in staging mode */
		public static synchronized boolean isStaging() {
			return get() == STAGING;
		}
		
		/** Returns true if the app is in production mode */
		public static synchronized boolean isProduction() {
			return get() == PRODUCTION;
		}
		
		/** An extra method to determine whether the app is currently running tests */
		public static synchronized boolean isRunningTests() {
			return Play.isTest();
		}
		
	}

}
