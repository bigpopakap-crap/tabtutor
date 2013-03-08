package controllers;

import play.mvc.Result;
import utils.MessagesEnum;

public class TestErrorPageExceptionController extends TestWebController {
	
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
