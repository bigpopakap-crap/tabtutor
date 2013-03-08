package controllers;

import play.mvc.Result;

public class TestErrorPageExceptionController extends TestWebController {
	
	public static Result notFoundPage() {
		throw ErrorPageException.Factory.notFoundPage();
	}
	
	public static Result goBackPage() {
		throw ErrorPageException.Factory.goBackPage("This is the description");
	}
	
	public static Result goToPage() {
		throw ErrorPageException.Factory.goToPage("This is the description", "/", "to go home");
	}

}
