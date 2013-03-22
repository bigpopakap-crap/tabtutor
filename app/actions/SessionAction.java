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
import contexts.RequestActionContext;
import contexts.SessionContext;

/**
 * This Action will add a session cookie to the browser, and create
 * any session-related database entries
 * 
 * This is the only class that should directly interact with the session cookie.
 * All other classes should refer directly to the AppContext.Session object. With
 * the exception of this class, the term "session" refers to the AppContext.Session,
 * not the browser cookie
 * 
 * @author bigpopakap
 * @since 2013-02-24
 *
 */
public class SessionAction extends Action.Simple {
	
	/**
	 * Annotation for applying SessionedAction
	 * 
	 * @author bigpopakap
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
		RequestActionContext.put(this.getClass());
		
		SessionModel session = SessionContext.get(); //use this method because it is cached
		if (session == null) {
			//there is no session ID set, so create it and add it to the cookie
			SessionModel newSession = SessionModel.Factory.createAndSave();
			ctx.session().put(SessionModel.SESSION_ID_COOKIE_KEY, newSession.GETTER.pk().toString());
			Logger.info("Put session " + newSession.GETTER.pk() + " in cookie for IP " + ctx.request().remoteAddress());
		}
		
		return delegate.call(ctx);
	}
	
}