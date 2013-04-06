package actions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
	
	/**
	 * Lists the actions on which this action is dependent
	 * Some dependencies are enforced for all (see {@link #listDependencies()})
	 * 
	 * The default implementation is to return an empty list, but subclasses
	 * can override this to specify more dependencies
	 * 
	 * Currently, the order of the dependencies are not checked, just that they
	 * were indeed called
	 */
	protected Set<Class<? extends BaseAction<?>>> hook_listDependencies() {
		return NO_DEPENDENCIES;
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
			throwIfDependenciesNotSatisfied();
			RequestActionContext.put((Class<BaseAction<?>>) this.getClass());
			
			//delegate to the implementing action
			Logger.trace("Calling into " + this.getClass().getCanonicalName() + " implementation");
			return hook_call(ctx);
		}
		finally {
			Logger.trace("Exiting from " + this.getClass().getCanonicalName());
		}
	}
	
	private List<Class<? extends BaseAction<?>>> listDependencies() {
		List<Class<? extends BaseAction<?>>> dependencies = new LinkedList<>();
		
		//add the default dependencies if this is not one of them
		if (!(this instanceof TryCatchAction)) {
			dependencies.add(TryCatchAction.class);
		}
		
		//add dependencies specific to the subclass
		dependencies.addAll(hook_listDependencies());
		return dependencies;
	}
	
	/** Throws an exception if the dependencies were not satisfied */
	private void throwIfDependenciesNotSatisfied() {
		List<Class<? extends BaseAction<?>>> dependencies = listDependencies();
		if (!RequestActionContext.has(dependencies)) {
			throw new IllegalStateException("Action dependencies haevn't been applied on this request.\n" +
											"This: " + this.getClass().getCanonicalName() + "\n" +
											"Needs: " + dependencies + "\n" +
											"Has: " + RequestActionContext.get());
		}
	}
	
}
