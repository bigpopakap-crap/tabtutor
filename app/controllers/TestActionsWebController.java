package controllers;

import actions.FbAuthAction.FbAuthed;
import actions.SessionAction.Sessioned;
import play.mvc.Result;

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
	
	@FbAuthed
	public static Result fbAuthAction() {
		return ok("yay");
	}
	
}
