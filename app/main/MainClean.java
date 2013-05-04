package main;

/**
 * Class to clean the build
 * 
 * @author bigpopakap
 * @since 2013-04-12
 *
 */
public final class MainClean extends BaseMain {
	
	private MainClean() {} //prevent instantiation
	
	public static void main(String[] args) {
		foreman(CLEAN_PROCFILE_NAME, ENV_FILE_NAME);
	}

}
