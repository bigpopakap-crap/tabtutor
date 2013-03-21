package controllers;

import actions.ModeProtectAction.ModeProtected;

/**
 * This class handles routes (or parents classes that handle routes) which are used by tests for various purposes
 * It is protected to only be available in DEVELOPMENT mode
 * 
 * @author bigpopakap
 * @since 2013-03-04
 *
 */
@ModeProtected //so there routes are only accessible in DEVELOPMENT mode
public class TestWebController extends BaseWebController {
	
	//TODO write a test to make sure that if an exception is thrown during routing, the database transaction is rolled back

}
