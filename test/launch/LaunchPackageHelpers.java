package launch;

import org.junit.Assert;

class LaunchPackageHelpers {
	
	/** Helps test that a minimum set of system environment variables are actually defined */
	static void helpTestExpectedEnvironmentVariables(String[] expectedEnvKeys) {
		for (String envKey : expectedEnvKeys) {
			Assert.assertNotNull(
				"Expected environment variable was not found: " + envKey,
				System.getenv(envKey)
			);
		}
	}

}
