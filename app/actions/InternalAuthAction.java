package actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;

/**
 * This Action will ensure the logged-in user has access to internal resources and pages
 * It is assumed that a user has already been logged in
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-24
 *
 */
public class InternalAuthAction extends Action.Simple {
	
	/**
	 * Annotation for applying SessionedAction, FacebookAuthenticatedAction then InternalAuthenticatedAction
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-02-24
	 *
	 */
	@With({SessionAction.class, FbAuthAction.class, InternalAuthAction.class})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface InternalAuthed {}

	/** Implements the action */
	@Override
	public Result call(Context ctx) throws Throwable {
		Logger.debug("Calling into " + this.getClass().getName());
		//TODO implement this
		return delegate.call(ctx);
	}
	
}
