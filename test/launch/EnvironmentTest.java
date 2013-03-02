package launch;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import base.BaseFuncTest;

import common.AppCtx;

/**
 * Test packages for those concerning the environment/app configuration
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-26
 *
 */
public class EnvironmentTest extends BaseFuncTest {
	
	/** Test that the environment variables necessary for Heroku are present */
	@Test
	public void testExpectedHerokuEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables("WTF_HTTP_PORT");
	}
	
	/** Test that the environment variables necessary for Play are present */
	@Test
	public void testExpectedPlayEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables("WTF_PLAY_MODE", "WTF_CRYPTO_SECRET");
	}
	
	/** Test that the environment variables necessary for the app are present */
	@Test
	public void testExpectedAppEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables("WTF_APP_TITLE", "WTF_MODE", "WTF_SYSTEM_TIMEZONE_CODE");
	}
	
	/** Test that the environment variables necessary for the logger are present */
	@Test
	public void testLoggerLevelEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables("WTF_ROOT_LOGGER_LEVEL", "WTF_PLAY_LOGGER_LEVEL", "WTF_APP_LOGGER_LEVEL");
	}
	
	/** Test that the environment variables necessary for the db are present */
	@Test
	public void testDatabaseConnectionEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables("WTF_DB_DRIVER", "WTF_DB_URL", "WTF_DB_USERNAME", "WTF_DB_PASSWORD");
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
	
	/** Tests the special methods in AppCtx.Var that are not implemented by all enum members */
	@Test
	public void testAppCtxVarSpecialMethods() {
		//if any of these throws an exception, that'll make the test fail
		//call these a few times to make sure they work each time
		for (int i = 0; i < 3; i++) {
			TimeZone tz = AppCtx.Var.SYSTEM_TIMEZONE_CODE.valAsTimezone();
			int port = AppCtx.Var.HTTP_PORT.valAsInt();
			Assert.assertNotNull("Timezone object is null" , tz);
			Assert.assertFalse("Port cannot be negative", port < 0); //this is really here just so the port variable is used
		}
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
	
	//TODO write a static analysis test to make sure nobody references the Play class directly
	//TODO write a static analysis test to make sure nobody references System.getenv(..) directly
	//TODO write a static analysis test to make sure nobody uses private AppCtx.Vars in the UI
	//TODO write a static analysis test to make sure nobody is using System.out.println directly, but instead Logger
	
}
