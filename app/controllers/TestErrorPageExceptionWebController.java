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
		throw ErrorPageException.Factory.internalServerErrorPage();
	}
	
	public static Result notFoundPage() {
		throw ErrorPageException.Factory.notFoundPage();
	}
	
	public static Result goBackPage() {
		throw ErrorPageException.Factory.goBackPage(
				MessagesEnum.errorPage_sampleDescription.get()
			);
	}
	
	public static Result goToPage() {
		throw ErrorPageException.Factory.goToPage(
				MessagesEnum.errorPage_sampleDescription.get(), 
				"/",
				MessagesEnum.errorPage_toGoHome.get()
			);
	}

}
