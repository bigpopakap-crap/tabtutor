package actions;

import models.SessionModel;
import play.mvc.Http.Context;
import play.mvc.Result;
import actions.ActionAnnotations.Sessioned;
import contexts.SessionContext;

/**
 * This Action will add a session cookie to the browser, and create
 * any session-related database entries
 * 
 * @author bigpopakap
 * @since 2013-02-24
 *
 */
public class SessionAction extends BaseAction<Sessioned> {
	
	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		SessionModel session = SessionContext.session(); //use this method because it is cached
		if (session == null || configuration.forceRefresh()) {
			SessionContext.init(ctx);
		}

		return delegate.call(ctx);
	}

}
