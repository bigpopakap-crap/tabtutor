package contexts;

import java.util.concurrent.Callable;

import play.mvc.Http.Context;
import utils.ConcurrentUtil;
import utils.Universe;
import utils.Universe.UniverseElement;

/**
 * Parent class of all context classes
 * Provides some helper methods
 * 
 * @author bigpopakap
 * @since 2013-03-18
 *
 */
public abstract class BaseContext {
	
	protected static final Universe<String> CONTEXT_KEY_UNIVERSE = new Universe<>();
	
	/** Helper to either get the value from the context (given the key), or
	 *  load it and save it to the context */
	@SuppressWarnings("unchecked")
	protected static synchronized final <T> T getOrLoad(UniverseElement<String> contextKey, Callable<T> loader) {
		//try getting the object from the context
		T t = (T) Context.current().args.get(CONTEXT_KEY_UNIVERSE.extract(contextKey));
		
		//if not retrieved, load it and store it in the context
		if (t == null) {
			t = ConcurrentUtil.callQuietly(loader); //let exceptions be thrown
			set(contextKey, t);
		}
		
		//finally return the value
		return t;
	}
	
	/** Helper to set context key values in the context.
	 *  This does no checks, and will overwrite existing values */
	protected static synchronized void set(UniverseElement<String> contextKey, Object value) {
		Context.current().args.put(CONTEXT_KEY_UNIVERSE.extract(contextKey), value);
	}
	
	/** Helper to clear set all the values to null for the given keys */
	@SafeVarargs
	protected static synchronized void refresh(UniverseElement<String>... contextKeys) {
		for (UniverseElement<String> contextKey : contextKeys) {
			Context.current().args.put(CONTEXT_KEY_UNIVERSE.extract(contextKey), null);
		}
	}
	
}
