package controllers;

import play.mvc.Result;
import actions.AuthAction.Authed;
import actions.SessionAction.Sessioned;

/**
 * Test pages that use various annotations
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public class TestActionsWebController extends TestWebController {
	
	@Sessioned
	public static Result sessionAction() {
		return ok("yay");
	}
	
	@Sessioned @Authed
	public static Result fbAuthAction() {
		return ok("yay");
	}
	
}
