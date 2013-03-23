package actions;

import java.util.Collections;
import java.util.List;

import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import contexts.RequestActionContext;

/**
 * This the base class for all actions, which executes some common code
 * before calling into the meat of the action
 * 
 * @author bigpopakap
 * @since 2013-03-22
 *
 * @param <T> the annotation that configures this action
 */
public abstract class BaseAction<T> extends Action<T> {
	
	//TODO test that all actions have their dependencies satisfied
	
	/** Helper value for subclasses */
	protected static final List<Class<? extends BaseAction<?>>> NO_DEPENDENCIES = Collections.emptyList();
	
	@Override
	@SuppressWarnings("unchecked")
	public final Result call(Context ctx) throws Throwable {
		try {
			//ensure dependencies, put this into requestactioncontext, and log
			if (!RequestActionContext.has(hook_listDependencies())) {
				throw new IllegalStateException("Action dependencies haevn't been applied on this request");
			}
			RequestActionContext.put((Class<BaseAction<?>>) this.getClass());
			Logger.debug("Calling into " + this.getClass());
			
			//delegate to the implementing action
			return callImpl(ctx);
		}
		finally {
			Logger.debug("Exiting from " + this.getClass());
		}
	}
	
	/** Lists the actions on which this action is dependent */
	protected abstract List<Class<? extends BaseAction<?>>> hook_listDependencies();
	
	/** Implements the actual action. Must eventually delegate to the delegate */
	protected abstract Result callImpl(Context ctx) throws Throwable;
	
}
