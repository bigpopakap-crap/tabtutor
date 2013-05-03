package actions;

import play.mvc.Http.Context;
import play.mvc.Result;
import actions.base.BaseAction;
import actions.base.ActionAnnotations.Transactioned;

import com.avaje.ebean.Ebean;

import contexts.AppContext;

/**
 * Action for creating and managing a transaction for the duration of the request
 * 
 * @author bigpopakap
 * @since 2013-04-04
 *
 */
public class TransactionAction extends BaseAction<Transactioned> {

	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		//TODO make it possible to get an extra transaction (either nested, or separate)
		//TODO allow for transactions across multiple requests
		boolean successful = false;
		try {
			Ebean.beginTransaction();
			
			Result result = delegate.call(ctx);
			successful = true;
			return result;
		}
		finally {
			//if running tests, don't commit
			if (AppContext.Mode.isRunningTests() || !successful) {
				Ebean.rollbackTransaction();
			}
			else {
				//TODO do retry logic
				Ebean.commitTransaction();
			}
		}
	}

}
