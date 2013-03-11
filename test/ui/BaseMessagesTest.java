package ui;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;

import base.BaseFuncTest;
import contexts.AppContext;

/**
 * Base test for ensuring that non-dynamic strings use the messages framework, and can
 * therefore be translated easily
 * 
 * This workSs by providing some protected helper methods to do the test, and each
 * test can override hooks to navigate to pages, and select which strings should be tested
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-26
 *
 */
public abstract class BaseMessagesTest extends BaseFuncTest {
	
	/**
	 * A class that can be overridden and passed in to implement the details of a test run
	 * See {@link #BasicMessagesTest.doTestHardcodedStrings()} as an example of how to use this class
	 * 
	 * @author bigpopakap@gmail.com
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
		
		/** Get all the Strings on the page that should be tested */
		public List<String> locate() {
			return new LinkedList<String>();
		}
		
		/** Validate the string */
		public boolean validate(String str) {
			//TODO do some actual validation
			return false;
		}
		
	}

	/**
	 * Helper to test the strings. The hooks can be passed in to control the flow of this method
	 * @param hooks
	 */
	protected void doTestHardcodedStrings(TestCaseHooks hooks) {
		final TestCaseHooks nonNullHooks = hooks != null ? hooks : new TestCaseHooks();
		
		running(testServer(AppContext.Var.HTTP_PORT.valAsInt()), new Runnable() {

			@Override
			public void run() {
				//TODO set the language to the debug language
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
