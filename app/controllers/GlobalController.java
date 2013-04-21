package controllers;

import java.util.TimeZone;

import play.Application;
import play.GlobalSettings;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import utils.Logger;
import contexts.AppContext;

/**
 * This the global controller, that implements hooks in the app's lifecycle
 * 
 * @author bigpopakap
 * @since 2013/02/10
 *
 */
public class GlobalController extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {
		//set the system timezone
		TimeZone tz = AppContext.Var.SYSTEM_TIMEZONE_CODE.valAsTimezone();
		TimeZone.setDefault(tz); //server
		//TODO set the database timezone as well, and indicate that is was set in the log line below
		Logger.info("Set system timezone to " + tz.getID() + ": " + tz);
		
		//print environment vars and environment context
		for (AppContext.Var envVar : AppContext.Var.values()) {
			Logger.info("Environment variable: " + envVar.name() + " (" + envVar.key() + ") -> " + envVar.val());
		}
		Logger.info("App context: " + AppContext.Mode.get());
		
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
		Logger.error("Error handling " + req + ": " + t + " (IP: " + req.remoteAddress() + ")");
		return super.onError(req, t);
	}
	
	@Override
	public Result onHandlerNotFound(RequestHeader req) {
		Logger.warn("Handler not found " + req + " (IP: " + req.remoteAddress() + ")");
		return super.onHandlerNotFound(req);
	}
	
	@Override
	public Result onBadRequest(RequestHeader req, String err) {
		Logger.warn("Bad request " + req + " (IP: " + req.remoteAddress() + ")");
		return super.onBadRequest(req, err);
	}
	
}
