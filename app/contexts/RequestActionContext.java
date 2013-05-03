package contexts;

import helpers.Universe.UniverseElement;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import contexts.base.BaseContext;

import actions.base.BaseAction;

/**
 * This class holds information about which actions have been applied
 * to this request
 * 
 * This is mainly for debugging purposes
 * 
 * @author bigpopakap
 * @since 2013-03-21
 *
 */
public class RequestActionContext extends BaseContext {
	
	//TODO static test to make sure all actions call the put() method
	
	private static final UniverseElement<String> REQUEST_ACTION_LIST_CONTEXT_KEY = CONTEXT_KEY_UNIVERSE.register("requestActionContextKey");
	
	/** Gets the list of actions applied in this request, in the order they were applied */
	public static synchronized List<Class<? extends BaseAction<?>>> get() {
		return getOrLoad(REQUEST_ACTION_LIST_CONTEXT_KEY, REQUEST_ACTION_LIST_CALLABLE);
	}
	
	/** 
	 * Gets the number of times this actions has been applied in this request
	 * Uses the class of the given action to do the lookup
	 */
	public static synchronized boolean has(BaseAction<?> action) {
		return action != null && get().contains(action.getClass());
	}
	
	/** Determines if the given actions were applied in this request */
	public static synchronized boolean has(Collection<Class<? extends BaseAction<?>>> actions) {
		return actions != null && get().containsAll(actions);
	}
	
	/** Appends the action to the list of those applied during the request.
	 *  Actions should call this as the first thing they do */
	public static synchronized void put(Class<? extends BaseAction<?>> action) {
		get().add(action);
	}
	
	/** The callable to load the list of Actions */
	private static final Callable<List<Class<? extends BaseAction<?>>>> REQUEST_ACTION_LIST_CALLABLE = new Callable<List<Class<? extends BaseAction<?>>>>() {

		@Override
		public List<Class<? extends BaseAction<?>>> call() throws Exception {
			//just create a new one, since there's no persistent storage for this
			return new LinkedList<>();
		}
		
	};

}
