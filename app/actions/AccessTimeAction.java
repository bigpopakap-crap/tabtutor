package actions;

import play.mvc.Http.Context;
import play.mvc.Result;
import actions.ActionAnnotations.AccessTimed;
import contexts.AppContext;
import contexts.SessionContext;

public class AccessTimeAction extends BaseAction<AccessTimed> {

	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		try {
			//update the session access time if there is one
			if (SessionContext.session() != null) {
				SessionContext.session().setLastAccessTimeAndUpdate();
			}
			
			//update the user last access time if there is one
			if (SessionContext.hasUser()) {
				SessionContext.user().setLastAccessTimeAndUpdate();
			}
		}
		catch (Exception ex) {
			//ignore this error unless it's not production
			if (!AppContext.Mode.isProduction()) {
				throw ex;
			}
		}
		
		//delegate
		return delegate.call(ctx);
	}

}
