package controllers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;

import common.Globals;

/**
 * This class defines actions and annotations used for authenticating users
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-24
 *
 */
public abstract class SecuredActions {
	
	/**
	 * Annotation for applying SessionedAction
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	@With(SessionedAction.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Sessioned {}
	
	/**
	 * Annotation for applying SessionedAction then FacebookAuthenticatedAction
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	@With({SessionedAction.class, FacebookAuthenticatedAction.class})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FacebookAuthenticated {}
	
	/**
	 * Annotation for applying SessionedAction, FacebookAuthenticatedAction then InternalAuthenticatedAction
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	@With({SessionedAction.class, FacebookAuthenticatedAction.class, InternalAuthenticatedAction.class})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface InternalAuthenticated {}
	
	
	/* ********************************************************************************
	 * 								BEGIN ACTION CLASSES						      *
	 ******************************************************************************** */
	
	/**
	 * This Action will add a session cookie to the browser, and create
	 * any session-related database entries
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	public static class SessionedAction extends Action.Simple {

		@Override
		public Result call(Context ctx) throws Throwable {
			Logger.debug("Calling into SessionedAction");
			
			if (!ctx.session().containsKey(Globals.SESSION_ID_COOKIE_KEY) /* TODO validate the session key exists */) {
				//there is no session ID set, so create the session
				//TODO create the session
				UUID pk = UUID.randomUUID();
				
				//add the ID to the session
				ctx.session().put(Globals.SESSION_ID_COOKIE_KEY, pk.toString());
				Logger.info("Created session " + pk);
			}
			
			return delegate.call(ctx);
		}
		
	}
	
	/**
	 * This Action will log the user in through Facebook, and ensure that the authentication
	 * token has not expired
	 * It is assumed that the session has already been set up
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	public static class FacebookAuthenticatedAction extends Action.Simple {

		@Override
		public Result call(Context ctx) throws Throwable {
			Logger.debug("Calling into FacebookAuthenticatedAction");
			return delegate.call(ctx);
		}
		
	}
	
	/**
	 * This Action will ensure the logged-in user has access to internal resources and pages
	 * It is assumed that a user has already been logged in
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	public static class InternalAuthenticatedAction extends Action.Simple {

		@Override
		public Result call(Context ctx) throws Throwable {
			Logger.debug("Calling into InternalAuthenticatedAction");
			return delegate.call(ctx);
		}
		
	}

}
