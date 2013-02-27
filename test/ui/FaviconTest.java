package ui;

import static play.test.Helpers.GET;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.routeAndCall;
import junit.framework.Assert;

import org.junit.Test;

import play.mvc.Result;
import base.BaseFuncTest;

/**
 * Tests the Facebook authentication flows
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-26
 *
 */
public class FaviconTest extends BaseFuncTest {
	
	/** Tests that the AppCtx class has no null values */
	@Test
	public void testFaviconExists() {
		//TODO change this to something not deprecated
		Result result = routeAndCall(fakeRequest(GET, "/favicon.ico"));
		Assert.assertNotNull(result);
	}

}