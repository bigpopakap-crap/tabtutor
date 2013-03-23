package actions;

import java.util.List;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import actions.ActionAnnotations.ErrorCaught;
import contexts.AppContext;
import controllers.exceptions.BaseExposedException;

/**
 * This action will catch any exceptions in the delegated action, and
 * returns the corresponding result to the user
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public class ErrorCatchAction extends BaseAction<ErrorCaught> {
	
	@Override
	protected List<Class<? extends BaseAction<?>>> hook_listDependencies() {
		return NO_DEPENDENCIES;
	}
	
	@Override
	protected Result callImpl(Context ctx) throws Throwable {
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
				return BaseExposedException.Factory.internalServerError(ex).result();
			}
			else {
				throw ex;
			}
		}
	}

}
