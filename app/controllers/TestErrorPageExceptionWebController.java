package controllers;

import play.mvc.Result;
import controllers.exceptions.InternalServerExposedException;
import controllers.exceptions.NotFoundExposedException;

/**
 * Test pages for throwing exposed exceptions
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public class TestErrorPageExceptionWebController extends TestWebController {
	
	public static Result internalServerErrorPage() {
		throw new InternalServerExposedException(new RuntimeException());
	}
	
	public static Result notFoundPage() {
		throw new NotFoundExposedException(new RuntimeException());
	}

}
