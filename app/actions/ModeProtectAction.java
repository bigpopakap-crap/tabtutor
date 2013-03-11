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
import exeptions.BaseExposedException;

/**
 * This action will catch throw an exception if the app is not running in the
 * mode specified in the parameter
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public class ModeProtectAction {
	
	/**
	 * Annotation for applying ModeProtectAction
	 * 
	 * @author bigpopakap
	 * @since 2013-03-06
	 *
	 */
	@With(ModeProtectedActionImpl.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ModeProtected {
		AppContext.Mode allowedMode() default AppContext.Mode.DEVELOPMENT;
	}
	
	public static class ModeProtectedActionImpl extends Action<ModeProtected> {
		
		/** Implements the action */
		@Override
		public Result call(Context ctx) throws Throwable {
			Logger.debug("Calling into " + this.getClass() + " only allowing mode " + configuration.allowedMode());
			if (AppContext.Mode.get() != configuration.allowedMode()) {
				//TODO figure out which default error to show to the user
				throw BaseExposedException.Factory.notFound();
			}
			else {
				return delegate.call(ctx);
			}
		}
		
	}
	
}
