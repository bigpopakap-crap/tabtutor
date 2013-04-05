package starter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import utils.DateUtil;
import base.BaseFuncTest;
import contexts.AppContext;

/**
 * Tests for app timing (time zones, converting to a user's timezone, making sure the database
 * time matches the server time, etc.)
 * 
 * @author bigpopakap
 * @since 2013-02-26
 *
 */
public class TimezoneTest extends BaseFuncTest {
	
	/** Tests that the environment Timezone code is valid */
	@Test
	public void testAppContextTimezoneCode() {
		String tzCode = AppContext.Var.SYSTEM_TIMEZONE_CODE.val();
		Assert.assertNotNull(tzCode);
		
		List<String> availableIds = Arrays.asList(TimeZone.getAvailableIDs());
		Assert.assertTrue(availableIds.contains(tzCode));
	}
	
	/** Tests that the timezone returned has the same ID as returned by AppContext */
	@Test
	public void testAppContextTimezoneMatches() {
		TimeZone tz = AppContext.Var.SYSTEM_TIMEZONE_CODE.valAsTimezone();
		Assert.assertNotNull(tz);
		Assert.assertTrue(tz.getID().equals(AppContext.Var.SYSTEM_TIMEZONE_CODE));
	}
	
	/** Tests that the server time is returned in the environment timezone */
	@Test
	public void testServerTimezone() {
		Date date1 = new Date();
		Date date2 = DateUtil.now();
		TimeZone tz = AppContext.Var.SYSTEM_TIMEZONE_CODE.valAsTimezone();
		//TODO complete this. make sure both dates are in the correct timezone
	}
	
	//TODO test the database timezone is in the environment timezone
	//TODO test the database timezone matches the server timezone

}
