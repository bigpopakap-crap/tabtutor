package controllers;

import play.mvc.Result;
import utils.MessagesEnum;

/**
 * Test pages for throwing exposed exceptions
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public class TestErrorPageExceptionWebController extends TestWebController {
	
	public static Result internalServerErrorPage() {
		throw ErrorPageException.Factory.internalServerErrorPage(new RuntimeException());
	}
	
	public static Result notFoundPage() {
		throw ErrorPageException.Factory.notFoundPage(new RuntimeException());
	}
	
	public static Result goBackPage() {
		throw ErrorPageException.Factory.goBackPage(
				new RuntimeException(),
				MessagesEnum.errorPage_sampleDescription.get()
			);
	}
	
	public static Result goToPage() {
		throw ErrorPageException.Factory.goToPage(
				new RuntimeException(),
				MessagesEnum.errorPage_sampleDescription.get(), 
				"/",
				MessagesEnum.errorPage_toGoHome.get()
			);
	}

}
