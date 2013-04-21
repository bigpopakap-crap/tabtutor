package controllers;

import play.mvc.Result;
import utils.MessagesEnum;
import controllers.exceptions.web.GoBackErrorPageException;
import controllers.exceptions.web.GoHomeErrorPageException;
import controllers.exceptions.web.GoToErrorPageException;
import controllers.exceptions.web.InternalServerErrorPageException;
import controllers.exceptions.web.NotFoundErrorPageException;

/**
 * Test pages for throwing exposed exceptions
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public class TestErrorPageExceptionWebController extends TestWebController {
	
	public static Result internalServerErrorPage() {
		throw new InternalServerErrorPageException(new RuntimeException());
	}
	
	public static Result notFoundPage() {
		throw new NotFoundErrorPageException(new RuntimeException());
	}
	
	public static Result goBackPage() {
		throw new GoBackErrorPageException(
			new RuntimeException(),
			MessagesEnum.errorPage_sampleDescription.get()
		);
	}
	
	public static Result goToPage() {
		throw new GoToErrorPageException(
			new RuntimeException(),
			"/",
			MessagesEnum.errorPage_toGoHome.get(),
			MessagesEnum.errorPage_sampleDescription.get()
		);
	}
	
	public static Result goHomePage() {
		throw new GoHomeErrorPageException(
			new RuntimeException(),
			MessagesEnum.errorPage_sampleDescription.get()
		);
	}

}
