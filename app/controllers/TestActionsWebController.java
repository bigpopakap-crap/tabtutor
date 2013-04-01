package controllers;

import play.mvc.Result;
import actions.ActionAnnotations.Authed;
import actions.ActionAnnotations.TriedCaught;
import actions.ActionAnnotations.Sessioned;

/**
 * Test pages that use various annotations
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public class TestActionsWebController extends TestWebController {
	
	@TriedCaught @Sessioned
	public static Result sessionAction() {
		return ok("yay");
	}
	
	@TriedCaught @Sessioned @Authed
	public static Result authAction() {
		return ok("yay");
	}
	
}
