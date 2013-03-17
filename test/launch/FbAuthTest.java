package launch;

import org.junit.Test;

import base.BaseFuncTest;

/**
 * Test package for the Facebook authentication flows
 * 
 * @author bigpopakap
 * @since 2013-02-26
 *
 */
public class FbAuthTest extends BaseFuncTest {
	
	/** Test that the environment variables necessary for Facebook are present */
	@Test
	public void testExpectedFbEnvironmentVariables() {
		LaunchPackageHelpers.helpTestExpectedEnvironmentVariables("WTF_FB_SITE_URL", "WTF_FB_APP_ID", "WTF_FB_APP_SECRET");
	}
	
	//TODO write more tests for Facebook login
	
}
