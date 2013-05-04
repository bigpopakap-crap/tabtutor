package main;

/**
 * Main class to run the app
 * 
 * @author bigpopakap
 * @since 2013-04-04
 *
 */
public final class MainApp extends BaseMain {
	
	private MainApp() {} //prevent instantiation
	
	/** Run the app */
	public static void main(String[] args) {
		foreman(APP_PROCFILE_NAME, ENV_FILE_NAME);
	}

}
