package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class PageController extends Controller {
	
	public static Result index() {
		return ok("Index page");
	}

}
