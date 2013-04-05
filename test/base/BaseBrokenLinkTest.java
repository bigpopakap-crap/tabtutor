package base;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;

import contexts.AppContext;

/**
 * This class is a base class for tests that make sure links are not broken
 * 
 * @author bigpopakap
 * @since 2013-02-26
 *
 */
public class BaseBrokenLinkTest extends BaseFuncTest {
	
	/**
	 * A class that can be overridden and passed in to implement the details of a test run
	 * See {@link #BaseBrokenLinkTest.doTestLinksNotBroken()} as an example of how to use this class
	 * 
	 * @author bigpopakap
	 * @since 2013-02-26
	 *
	 */
	protected static class TestCaseHooks {
		
		/** Initialize the data on the server */
		public void initialize() {
			//do nothing
		}
		
		/** Navigate to a page that should be tested */
		public void navigate() {
			//do nothing
			//TODO go to a default page?
		}
		
		/** Get all the urls on the page that should be tested */
		public List<String> locate() {
			//TODO get all <a href="">, <script src=""> or <link href=""> strings on the page
			return new LinkedList<>();
		}
		
		/** Validate the string */
		public boolean validate(String str) {
			//TODO do navigate to the page and make sure the response exists
			return false;
		}
		
	}

	/**
	 * Helper to test the links. The hooks can be passed in to control the flow of this method
	 * @param hooks
	 */
	protected void doTestLinksNotBroken(TestCaseHooks hooks) {
		final TestCaseHooks nonNullHooks = hooks != null ? hooks : new TestCaseHooks();
		
		running(testServer(AppContext.Var.HTTP_PORT.valAsInt()), new Runnable() {

			@Override
			public void run() {
				nonNullHooks.initialize();
				nonNullHooks.navigate();
				List<String> strings = nonNullHooks.locate();
				for (String str : strings) {
					Assert.assertTrue(nonNullHooks.validate(str));
				}
			}
			
		});

	}

}
