package actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import models.SessionModel;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;

import common.Globals;

import doer.SessionDoer;

/**
 * This Action will add a session cookie to the browser, and create
 * any session-related database entries
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-24
 *
 */
public class SessionAction extends Action.Simple {
	
	/**
	 * Annotation for applying SessionedAction
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	@With(SessionAction.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Sessioned {}

	/** Implements the action */
	@Override
	public Result call(Context ctx) throws Throwable {
		Logger.debug("Calling into " + this.getClass().getName());
		
		if (!ctx.session().containsKey(Globals.SESSION_ID_COOKIE_KEY)
				|| !SessionDoer.isValidExistingSessionId(ctx.session().get(Globals.SESSION_ID_COOKIE_KEY))) {
			//there is no session ID set, so create it and add it to the cookie
			SessionModel newSession = SessionDoer.createNewSession();
			ctx.session().put(Globals.SESSION_ID_COOKIE_KEY, newSession.pk.toString());
			Logger.info("Put session " + newSession.pk + " in cookie for IP " + ctx.request().remoteAddress());
		}
		
		return delegate.call(ctx);
	}
	
}