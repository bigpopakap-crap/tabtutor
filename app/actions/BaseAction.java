package actions;

import java.util.Collections;
import java.util.LinkedList;
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
			Logger.debug("Calling into " + this.getClass());
			throwIfDependenciesNotSatisfied();
			RequestActionContext.put((Class<BaseAction<?>>) this.getClass());
			
			//delegate to the implementing action
			Logger.debug("Calling into " + this.getClass() + " implementation");
			return hook_call(ctx);
		}
		finally {
			Logger.debug("Exiting from " + this.getClass());
		}
	}
	
	private List<Class<? extends BaseAction<?>>> listDependencies() {
		List<Class<? extends BaseAction<?>>> dependencies = new LinkedList<Class<? extends BaseAction<?>>>();
		
		//add the default dependencies if this is not one of them
		if (!(this instanceof ErrorCatchAction)) {
			dependencies.add(ErrorCatchAction.class);
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
											"This: " + this.getClass() + "\n" +
											"Needs: " + dependencies + "\n" +
											"Has: " + RequestActionContext.get());
		}
	}
	
	/** Lists the actions on which this action is dependent */
	protected abstract List<Class<? extends BaseAction<?>>> hook_listDependencies();
	
	/** Implements the actual action. Must eventually delegate to the delegate */
	protected abstract Result hook_call(Context ctx) throws Throwable;
	
}
