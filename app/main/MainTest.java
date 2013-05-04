package main;

/**
 * Main class to run the test suite
 * 
 * @author bigpopakap
 * @since 2013-04-04
 *
 */
public final class MainTest extends BaseMain {
	
	private MainTest() {} //prevent instantiation
	
	/** Runs the tests */
	public static void main(String[] args) {
		foreman(TEST_PROCFILE_NAME, ENV_FILE_NAME);
	}

}
