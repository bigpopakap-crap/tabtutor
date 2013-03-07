package controllers;

/**
 * This class handles routes that will only be accessible to tests
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-04
 *
 */
public class TestWebController extends BaseWebController {
	
	//TODO figure out how to expose these paths only when running tests or in DEV mode
	//TODO write a test to make sure that if an exception is thrown during routing, the database transaction is rolled back

}
