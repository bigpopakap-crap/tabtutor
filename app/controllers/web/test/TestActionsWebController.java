package controllers.web.test;

import play.mvc.Result;
import actions.base.ActionAnnotations.Authed;
import actions.base.ActionAnnotations.Sessioned;

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
	
	@Authed
	public static Result authAction() {
		return ok("yay");
	}
	
}
