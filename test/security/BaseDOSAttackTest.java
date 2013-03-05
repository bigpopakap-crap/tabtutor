package security;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import java.util.LinkedList;
import java.util.List;

import common.AppContext;

/**
 * Base class to execute DOS attacks on the server
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-26
 *
 */
public class BaseDOSAttackTest extends BaseSecurityTest {
	
	/**
	 * Class for providing hooks to control the test execution
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
		
		/** List URLS to ping and test */
		public List<String> list() {
			return new LinkedList<String>();
		}
		
	}
	
	/** Helper to ping the server and make sure it stops responding at some point */
	public void doTestDosAttack(TestCaseHooks hooks) {
		final TestCaseHooks nonNullHooks = hooks != null ? hooks : new TestCaseHooks();
		
		running(testServer(AppContext.Var.HTTP_PORT.valAsInt()), new Runnable() {

			@Override
			public void run() {
				nonNullHooks.initialize();
				List<String> strings = nonNullHooks.list();
				for (String str : strings) {
					//TODO ping the server a bunch of times and figure out when it should stop responding
				}
			}
			
		});
	}

}
