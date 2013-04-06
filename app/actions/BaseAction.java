package actions;

import java.util.Collections;
import java.util.Set;

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
	protected static final Set<Class<? extends BaseAction<?>>> NO_DEPENDENCIES = Collections.emptySet();
	
	/**
	 * Called at the very beginning of the action, before the action is registered
	 * to have been called. If true, it will check to see if this action has already
	 * been called for the request and bypass the action if it was
	 * 
	 * If an action wants to execute every time it is applied to the request, it should
	 * override this method and return false
	 */
	protected boolean isIdempotent() {
		return true;
	}
	
	/** Implements the actual action. Must eventually delegate to the delegate */
	protected abstract Result hook_call(Context ctx) throws Throwable;
	
	@Override
	@SuppressWarnings("unchecked")
	public final Result call(Context ctx) throws Throwable {
		//short circuit if requested
		if (isIdempotent() && RequestActionContext.has(this)) {
			return delegate.call(ctx);
		}
		
		//proceed with the action
		try {
			Logger.trace("Calling into " + this.getClass().getCanonicalName());
			RequestActionContext.put((Class<? extends BaseAction<?>>) this.getClass());
			
			//delegate to the implementing action (delegate for them if they return null)
			Logger.trace("Calling into " + this.getClass().getCanonicalName() + " implementation");
			Result result = hook_call(ctx);
			if (result == null) {
				Logger.error("Action " + getClass().getCanonicalName() + " returned null from hook_call");
				result = delegate.call(ctx);
			}
			return result;
		}
		finally {
			Logger.trace("Exiting from " + this.getClass().getCanonicalName());
		}
	}

}
