package contexts;

import java.util.concurrent.Callable;

import play.mvc.Http.Context;

/**
 * Provides utils for the context package
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-10
 *
 */
public abstract class ContextPackageUtils {
	
	//TODO test that there are no duplicate values used as keys
	
	/** Helper to either get the value from the context (given the key), or
	 *  load it and save it to the context */
	@SuppressWarnings("unchecked")
	static <T> T getOrLoad(String contextKey, Callable<T> loader) {
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