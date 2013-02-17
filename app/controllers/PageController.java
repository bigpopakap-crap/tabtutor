package controllers;

import common.AppCtx;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class PageController extends Controller {
	
	public static Result landing() {
		return ok(landing.render(AppCtx.Var.FB_APP_ID.val(), AppCtx.Var.DOMAIN.val()));
	}

}
