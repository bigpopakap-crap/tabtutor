package actions;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import actions.ActionAnnotations.TriedCaughtFinally;
import contexts.AppContext;
import contexts.SessionContext;
import controllers.exceptions.BaseExposedException;
import controllers.exceptions.InternalServerErrorExposedException;

/**
 * This action will catch any exceptions in the delegated action, and
 * returns the corresponding result to the user
 * 
 * It will also do some logging and timing
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public class TryCatchFinallyAction extends BaseAction<TriedCaughtFinally> {
	
	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		//catch the start time and log the start of the request
		long startTime = System.currentTimeMillis();
		logRequest(ctx, true, -1);
		
		//this has not been applied yet, so catch exceptions
		try {
			try {
				preRequestDangerousActions();
			}
			catch (Exception ex) {
				//do nothing, just swallow :-) this error
			}
			
			//delegate to the actual handler
			return delegate.call(ctx);
		}
		catch (BaseExposedException ex) {
			Logger.warn("Exposed exception caught in " + this.getClass(), ex);
			return ex.result();
		}
		catch (Exception ex) {
			Logger.error("Exception caught in " + this.getClass(), ex);
			//TODO figure out which default error to display to the user
			if (AppContext.Mode.isProduction()) {
				return new InternalServerErrorExposedException(ex).result();
			}
			else {
				throw ex;
			}
		}
		finally {
			long duration = System.currentTimeMillis() - startTime;
			logRequest(ctx, false, duration);
		}
	}
	
	/** Actions to perform before the request whose exceptions should be suppressed */
	private void preRequestDangerousActions() {
		//TODO formalize this. And if it gets too hefty a method, it's got to move somewhere else
		//if there is a user, modify their last access time
		if (SessionContext.hasUser()) {
			SessionContext.user().setLastAccessTimeAndUpdate();
		}
	}
	
	/** Helper method for logging the beginning and end of a request */
	private static void logRequest(Context ctx, boolean isStart, long duration) {
		//create the string to log
		//TODO include other info about request (IP addres, etc)
		String message = (isStart ? "Started" : "Finished") +
						 " handling request " + ctx.request() +
						 " on session " + ctx.session() +
						 (duration < 0 ? "" : ", took " + duration + "ms");
		
		//choose the level to log at
		//TODO formalize this
		if (duration > 6000) Logger.error(message);
		else if (duration > 3000) Logger.warn(message);
		else Logger.info(message);
	}

}
