package launch;

import org.junit.Assert;
import org.junit.Test;

import static play.test.Helpers.*; 

import base.BaseFuncTest;

import common.AppCtx;

public class EnvironmentTest extends BaseFuncTest {
	
	/** This is the list of expected environment variables */
	private static final String[] EXPECTED_ENV_KEYS = new String[] {
		"WTF_PLAY_MODE", "WTF_MODE", "WTF_DOMAIN",
		"WTF_HTTP_PORT", "WTF_CRYPTO_SECRET",
		"WTF_FB_APP_ID", "WTF_FB_APP_SECRET"
	};
	
	/**
	 * Tests that a minimum set of system environment variables are actually defined
	 */
	@Test
	public void testExpectedEnvironmentVariables() {
		for (String envKey : EXPECTED_ENV_KEYS) {
			Assert.assertNotNull(
				"Expected environment variable was not found: " + envKey,
				System.getenv(envKey)
			);
		}
	}
	
	/**
	 * Tests that the AppCtx class has no null values
	 */
	@Test
	public void testAppCtxVars() {
		running(fakeApplication(), new Runnable() {
			@Override
			public void run() {
				//in the fake application, assert each environment var has no nulls
				for (AppCtx.Var envVar : AppCtx.Var.values()) {
					Assert.assertNotNull("AppCtx key has null key: ", envVar.key());
					Assert.assertNotNull("AppCtx key has null value: " + envVar, envVar.val());
				}
			}
		});
	}
	
	/**
	 * Tests that the environment returned by 
	 */
	@Test
	public void testAppCtxMode() {
		running(fakeApplication(), new Runnable() {
			@Override
			public void run() {
				//ensure that the mode is non-null
				Assert.assertNotNull("App mode cannot be null", AppCtx.Mode.get());
				
				//in the fake application, assert that the context matches
				switch (AppCtx.Mode.get()) {
					case DEVELOPMENT:	
						helpTestAppCtxEnvMatches(AppCtx.Mode.isDevelopment());
						break;
					case STAGING: 
						helpTestAppCtxEnvMatches(AppCtx.Mode.isStaging());
						break;
					case PRODUCTION:
						helpTestAppCtxEnvMatches(AppCtx.Mode.isProduction());
						break;
				}
			}
		});
	}
	
	private void helpTestAppCtxEnvMatches(boolean shouldBeTrue) {
		Assert.assertTrue("App mode is " + AppCtx.Mode.get() + " but boolean doesn't match", shouldBeTrue);
	}
	
	//TODO write a static analysis test to make sure nobody references the Play class directly
	//TODO write a static analysis test to make sure nobody references System.getenv(..) directly
	//TODO write a static analysis test to make sure nobody uses private AppCtx.Vars in the UI
	
}
