package controllers;

import oops.InternalServerOops;
import oops.NotFoundOops;
import play.mvc.Result;

/**
 * Test pages for throwing exposed exceptions
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public class TestErrorPageExceptionWebController extends TestWebController {
	
	public static Result internalServerErrorPage() {
		throw new InternalServerOops(new RuntimeException());
	}
	
	public static Result notFoundPage() {
		throw new NotFoundOops(new RuntimeException());
	}

}
