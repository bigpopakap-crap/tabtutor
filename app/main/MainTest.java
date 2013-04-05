package main;

/**
 * Main class to run the test suite
 * 
 * @author bigpopakap
 * @since 2013-04-04
 *
 */
public class MainTest extends BaseMain {
	
	/** Runs the tests */
	public static void main(String[] args) throws Exception {
		foreman("Procfile.test", "env.dev");
	}

}
