package launch;

import org.junit.Assert;

/**
 * Provides utils for the launch package tests
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-26
 *
 */
class LaunchPackageHelpers {
	
	/** Helps test that a minimum set of system environment variables are actually defined */
	static void helpTestExpectedEnvironmentVariables(String... expectedEnvKeys) {
		for (String envKey : expectedEnvKeys) {
			Assert.assertNotNull(
				"Expected environment variable was not found: " + envKey,
				System.getenv(envKey)
			);
		}
	}

}
