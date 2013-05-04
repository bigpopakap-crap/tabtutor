package actions;

import helpers.OperationReq;
import play.mvc.Http.Context;
import play.mvc.Result;
import actions.base.ActionAnnotations.DevModeProtected;
import actions.base.BaseAction;

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
		OperationReq.IS_DEV_MODE.verifyAndThrow();
		return delegate.call(ctx);
	}
	
}
