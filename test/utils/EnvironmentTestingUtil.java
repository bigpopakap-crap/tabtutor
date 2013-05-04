package utils;

import org.junit.Assert;

/**
 * Provides utils for the launch package tests
 * 
 * @author bigpopakap
 * @since 2013-02-26
 *
 */
public final class EnvironmentTestingUtil {
	
	private EnvironmentTestingUtil() {} //prevent instantiation
	
	/** Helps test that a minimum set of system environment variables are actually defined */
	public static void helpTestExpectedEnvironmentVariables(String... expectedEnvKeys) {
		for (String envKey : expectedEnvKeys) {
			Assert.assertNotNull(
				"Expected environment variable was not found: " + envKey,
				System.getenv(envKey)
			);
		}
	}

}
