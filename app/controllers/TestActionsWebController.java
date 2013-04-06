package controllers;

import play.mvc.Result;
import actions.ActionAnnotations.AccessTimed;
import actions.ActionAnnotations.Authed;
import actions.ActionAnnotations.Sessioned;
import actions.ActionAnnotations.TriedCaught;

/**
 * Test pages that use various annotations
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public class TestActionsWebController extends TestWebController {
	
	@TriedCaught @AccessTimed @Sessioned
	public static Result sessionAction() {
		return ok("yay");
	}
	
	@TriedCaught @AccessTimed @Sessioned @Authed
	public static Result authAction() {
		return ok("yay");
	}
	
}
