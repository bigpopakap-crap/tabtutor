package controllers;

import java.lang.reflect.Method;
import java.util.TimeZone;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

import common.AppCtx;

/**
 * This the global controller, that implements hooks in the app's lifecycle
 * 
 * @author bigpopakap@gmail.com
 * @since 2013/02/10
 *
 */
public class GlobalController extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {
		//set the system timezone
		TimeZone tz = AppCtx.Var.SYSTEM_TIMEZONE_CODE.valAsTimezone();
		TimeZone.setDefault(tz); //server
		//TODO set the database timezone as well, and indicate that is was set in the log line below
		Logger.info("Set system timezone to " + tz.getID() + ": " + tz);
		
		//print environment vars and environment context
		for (AppCtx.Var envVar : AppCtx.Var.values()) {
			Logger.info("Environment variable: " + envVar.name() + ":" + envVar.key() + " -> " + envVar.val());
		}
		Logger.info("App context: " + AppCtx.Mode.get());
		
		Logger.info("App is starting");
		super.onStart(app);
	}
	
	@Override
	public void onStop(Application app) {
		Logger.info("App stopping");
		super.onStart(app);
	}
	
	@Override
	public Result onError(RequestHeader req, Throwable t) {
		//TODO add session information to log line
		Logger.error("Error handling " + req + ": " + t + " (IP: " + req.remoteAddress() + ")");
		
		//if in production mode, show some error page
		if (AppCtx.Mode.isProduction()) {
			//TODO
		}
		
		return super.onError(req, t);
	}
	
	@Override
	public Result onHandlerNotFound(RequestHeader req) {
		//TODO add session information to log line
		Logger.warn("Handler not found " + req + " (IP: " + req.remoteAddress() + ")");
		
		//if in production mode, show some error page
		if (AppCtx.Mode.isProduction()) {
			//TODO
		}
		
		return super.onHandlerNotFound(req);
	}
	
	@Override
	public Result onBadRequest(RequestHeader req, String err) {
		//TODO add session information to log line
		Logger.warn("Bad request " + req + " (IP: " + req.remoteAddress() + ")");
		
		//if in production mode, show some error page
		if (AppCtx.Mode.isProduction()) {
			//TODO
		}
		
		return super.onBadRequest(req, err);
	}
	
	@Override
	public Action<?> onRequest(Request req, Method method) {
		//TODO add session information to log line
		Logger.info("Handling " + req + " (IP: " + req.remoteAddress() + ")");
		return super.onRequest(req, method);
	}
	
}
