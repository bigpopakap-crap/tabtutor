package actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;

import common.AppContext;

import exeptions.BaseExposedException;

/**
 * This action will catch any exceptions in the delegated action, and
 * returns the corresponding result to the user
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-06
 *
 */
public class ErrorCatchAction extends Action.Simple {
	
	/**
	 * Annotation for applying ErrorCatchAction
	 * 
	 * @author bigpopakap@gmail.com
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
		try {
			return delegate.call(ctx);
		}
		catch (BaseExposedException ex) {
			return ex.result();
		}
		catch (Exception ex) {
			///TODO allow the action to take a default ErrorException parameter
			if (AppContext.Mode.isProduction()) return BaseExposedException.Factory.internalServerError().result();
			else throw ex;
		}
	}

}
