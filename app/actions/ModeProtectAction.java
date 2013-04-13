package actions;

import java.lang.annotation.Annotation;

import play.mvc.Http.Context;
import play.mvc.Result;
import actions.ActionAnnotations.ModeProtected;
import contexts.AppContext;
import contexts.AppContext.Mode;
import controllers.exceptions.NotFoundExposedException;

/**
 * This action will catch throw an exception if the app is not running in the
 * mode specified in the parameter
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public class ModeProtectAction extends BaseAction<ModeProtected> {
	
	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		if (AppContext.Mode.get() != configuration.allowedMode()) {
			//TODO figure out which default error to show to the user
			throw new NotFoundExposedException(null);
		}
		else {
			return delegate.call(ctx);
		}
	}

	@Override
	protected ModeProtected createConfiguration() {
		return new ModeProtected() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return this.getClass();
			}
			
			@Override
			public Mode allowedMode() {
				return AppContext.Mode.DEVELOPMENT;
			}

		};
	}
	
}
