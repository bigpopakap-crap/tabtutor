package actions;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import actions.ActionAnnotations.TriedCaughtFinally;
import contexts.AppContext;
import contexts.RequestStatsContext;
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
		//this has not been applied yet, so catch exceptions
		try {
			try {
				Logger.info("Started handling " + ctx.request() + " for session " + ctx.session());
				RequestStatsContext.get();	//Initialize the stats by getting them
				preRequestDangerousActions();
			}
			catch (Exception ex) {
				if (AppContext.Mode.isProduction()) {
					Logger.error("Exception caught before delegating from " + this.getClass(), ex);
				}
				else {
					throw ex;
				}
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
			try {
				//print out the request stats
				RequestStatsContext.get().setCompleted();
				Logger.info("Finished handling request " + ctx.request() + " for session " + ctx.session() + "\n" +
							RequestStatsContext.get());
			}
			catch (Exception ex) {
				if (AppContext.Mode.isProduction()) {
					Logger.error("Exception caught in finally of " + this.getClass(), ex);
				}
				else {
					throw ex;
				}
			}
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

}
