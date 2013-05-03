package actions;

import oops.NotFoundOops;
import play.mvc.Http.Context;
import play.mvc.Result;
import actions.base.BaseAction;
import actions.base.ActionAnnotations.DevModeProtected;
import contexts.AppContext;

/**
 * This action will catch throw an exception if the app is not running in the
 * mode specified in the parameter
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public class DevModeProtectAction extends BaseAction<DevModeProtected> {
	
	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		if (!AppContext.Mode.isDevelopment()) {
			//TODO figure out which default error to show to the user
			throw new NotFoundOops(null);
		}
		else {
			return delegate.call(ctx);
		}
	}
	
}
