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
import contexts.AppContext;
import contexts.RequestActionContext;
import controllers.exceptions.BaseExposedException;

/**
 * This action will catch any exceptions in the delegated action, and
 * returns the corresponding result to the user
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public class ErrorCatchAction extends Action.Simple {
	
	/**
	 * Annotation for applying ErrorCatchAction
	 * 
	 * @author bigpopakap
	 * @since 2013-03-06
	 *
	 */
	@With(ErrorCatchAction.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ErrorCaught {}

	/** Implements the action */
	@Override
	public Result call(Context ctx) throws Throwable {
		Logger.debug("Calling into " + this.getClass());
		RequestActionContext.put(this.getClass());
		
		try {
			return delegate.call(ctx);
		}
		catch (BaseExposedException ex) {
			Logger.error("Exposed exception caught in " + this.getClass(), ex);
			return ex.result();
		}
		catch (Exception ex) {
			Logger.error("Exception caught in " + this.getClass(), ex);
			//TODO figure out which default error to display to the user
			if (AppContext.Mode.isProduction()) {
				return BaseExposedException.Factory.internalServerError().result();
			}
			else {
				throw ex;
			}
		}
	}

}
