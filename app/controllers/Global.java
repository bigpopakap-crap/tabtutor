package controllers;

import java.lang.reflect.Method;

import common.AppCtx;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

/**
 * 
 * @author bigpopakap@gmail.com
 * @since 2013/02/10
 *
 */
public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {
		//print environment vars and environment context
		for (AppCtx.Var envVar : AppCtx.Var.values()) {
			Logger.info("Environment variable: " + envVar + ":" + envVar.key() + " -> " + envVar.val());
		}
		Logger.info("App context: " + AppCtx.Mode.get());
		Logger.info("App is starting...");
		
		super.onStart(app);
	}
	
	@Override
	public void onStop(Application app) {
		Logger.info("App stopping");
		super.onStart(app);
	}
	
	@Override
	public Result onError(RequestHeader req, Throwable t) {
		Logger.error("Error handling " + req + ": " + t);
		return super.onError(req, t);
	}
	
	@Override
	public Result onHandlerNotFound(RequestHeader req) {
		Logger.warn("Handler not found " + req);
		return super.onHandlerNotFound(req);
	}
	
	@Override
	public Result onBadRequest(RequestHeader req, String err) {
		Logger.warn("Bad request " + req);
		return super.onBadRequest(req, err);
	}
	
	@Override
	public Action<?> onRequest(Request req, Method method) {
		Logger.info("Handling " + req);
		return super.onRequest(req, method);
	}
	
}
