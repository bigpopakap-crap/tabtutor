package contexts;

import java.util.concurrent.Callable;

import play.mvc.Http.Context;

/**
 * Parent class of all context classes
 * Provides some helper methods
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public abstract class BaseContext {
	
	//TODO test that there are no duplicate values used as keys
	
	/** Helper to either get the value from the context (given the key), or
	 *  load it and save it to the context */
	@SuppressWarnings("unchecked")
	protected static final <T> T getOrLoad(String contextKey, Callable<T> loader) {
		//try getting the object from the context
		T t = (T) Context.current().args.get(contextKey);
		
		//if not retrieved, load it and store it in the context
		if (t == null) {
			try {
				t = loader.call();
			}
			catch (Exception ex) {
				//set to null so this method just does nothing and returns null
				t = null;
			}
			
			Context.current().args.put(contextKey, t);
		}
		
		//finally return the value
		return t;
	}

}
