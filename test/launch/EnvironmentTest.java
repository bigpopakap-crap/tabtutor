package launch;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import base.BaseFuncTest;

import common.AppCtx;

public class EnvironmentTest extends BaseFuncTest {
	
	/** Test that the environment variables necessary for Heroku are present */
	@Test
	public void testExpectedHerokuEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables(new String [] { "WTF_HTTP_PORT" });
	}
	
	/** Test that the environment variables necessary for Play are present */
	@Test
	public void testExpectedPlayEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables(new String [] { "WTF_PLAY_MODE", "WTF_CRYPTO_SECRET" });
	}
	
	/** Test that the environment variables necessary for the app are present */
	@Test
	public void testExpectedAppEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables(new String [] { "WTF_APP_TITLE", "WTF_MODE", "WTF_SYSTEM_TIMEZONE_CODE" });
	}
	
	/** Tests that the AppCtx class has no null values */
	@Test
	public void testAppCtxVars() {
		running(fakeApplication(), new Runnable() {
			@Override
			public void run() {
				//in the fake application, assert each environment var has no nulls
				for (AppCtx.Var envVar : AppCtx.Var.values()) {
					Assert.assertNotNull("AppCtx key has null key: " + envVar.name(), envVar.key());
					Assert.assertNotNull("AppCtx key has null value: " + envVar.name(), envVar.val());
				}
			}
		});
	}
	
	/** Tests that the mode returned by AppCtx matches the boolean state-querier methods */
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
	
	/** Tests that the environment Timezone code is valid */
	@Test
	public void testAppCtxTimezoneCode() {
		List<String> availableIds = Arrays.asList(TimeZone.getAvailableIDs());
		Assert.assertTrue(availableIds.contains(AppCtx.Var.SYSTEM_TIMEZONE_CODE.val()));
	}
	
	//TODO write a static analysis test to make sure nobody references the Play class directly
	//TODO write a static analysis test to make sure nobody references System.getenv(..) directly
	//TODO write a static analysis test to make sure nobody uses private AppCtx.Vars in the UI
	//TODO write a static analysis test to make sure nobody is using System.out.println directly, but instead Logger
	
}
