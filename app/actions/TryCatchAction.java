package actions;

import play.mvc.Http.Context;
import play.mvc.Result;
import utils.Logger;
import actions.ActionAnnotations.TriedCaught;
import contexts.AppContext;
import contexts.RequestStatsContext;
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
public class TryCatchAction extends BaseAction<TriedCaught> {
	
	/** If a request takes more than this number of seconds, log it with an error level */
	private static final double REQUEST_DURATION_ERROR_THRESHOLD_SECONDS = 5;
	
	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		//this has not been applied yet, so catch exceptions
		try {
			Logger.info("Started handling " + ctx.request() + " for session " + ctx.session());
			RequestStatsContext.get();	//Initialize the stats by getting them
			
			//delegate to the actual handler
			return delegate.call(ctx);
		}
		catch (BaseExposedException ex) {
			Logger.warn("Exposed exception caught in " + this.getClass().getCanonicalName(), ex);
			return ex.result();
		}
		catch (Exception ex) {
			Logger.error("Exception caught in " + this.getClass().getCanonicalName(), ex);
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
				String message = "Finished handling request " + ctx.request() +
									" for session " + ctx.session() +
									" with stats " + RequestStatsContext.get();

				//log at error if the request took a long time
				if (RequestStatsContext.get().getDurationSeconds() > REQUEST_DURATION_ERROR_THRESHOLD_SECONDS) {
					Logger.error(message);
				}
				else {
					Logger.info(message);
				}
			}
			catch (Exception ex) {
				if (AppContext.Mode.isProduction()) {
					Logger.error("Exception caught in finally of " + this.getClass().getCanonicalName(), ex);
				}
				else {
					throw ex;
				}
			}
		}
	}

}
