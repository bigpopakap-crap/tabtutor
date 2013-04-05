package actions;

import play.mvc.Http.Context;
import play.mvc.Result;
import actions.ActionAnnotations.Transactioned;

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
		// TODO Auto-generated method stub
		return null;
	}

}
