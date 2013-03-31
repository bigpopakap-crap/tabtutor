package contexts;

import java.util.HashSet;
import java.util.Set;
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
	
	/** Helper to either get the value from the context (given the key), or
	 *  load it and save it to the context */
	@SuppressWarnings("unchecked")
	protected static synchronized final <T> T getOrLoad(ContextKey contextKey, Callable<T> loader) {
		//try getting the object from the context
		T t = (T) Context.current().args.get(contextKey.get());
		
		//if not retrieved, load it and store it in the context
		if (t == null) {
			try {
				t = loader.call();
			}
			catch (Exception ex) {
				//set to null so this method just does nothing and returns null
				t = null;
			}
			
			set(contextKey, t);
		}
		
		//finally return the value
		return t;
	}
	
	protected static synchronized void set(ContextKey contextKey, Object value) {
		Context.current().args.put(contextKey.get(), value);
	}
	
	/** Helper to clear set all the values to null for the given keys */
	protected static synchronized void refresh(ContextKey... keys) {
		for (ContextKey key : keys) {
			Context.current().args.put(key.get(), null);
		}
	}
	
	/**
	 * Class representing keys that objects can use to be stored in the
	 * request context
	 * 
	 * @author bigpopakap
	 * @since 2013-03-20
	 *
	 */
	public static class ContextKey {
		
		/** The string that will actually be used as the key */
		private final String key;
		
		/** 
		 * 	Creates a new context key
		 *  Note: DOES NOT check if another duplicate key is registered
		 */
		private ContextKey(String key) {
			if (key == null) throw new IllegalArgumentException("Key cannot be null");
			this.key = key;
		}
		
		/** Gets the underlying string to use as the key */
		private synchronized String get() {
			return key;
		}
		
		/** The list of all registered context keys */
		private static final Set<String> REGISTERED_KEYS = new HashSet<>();
		
		/** Determines if the given string is already registered as a context key */
		public static synchronized boolean isRegistered(String key) {
			return REGISTERED_KEYS.contains(key);
		}
		
		/** 
		 * Registers a new context key and returns it
		 * @throws IllegalArgumentException if the key is null or if it is already registered
		 */
		public static synchronized ContextKey register(String key) {
			if (key == null) throw new IllegalArgumentException("Key cannot be null");
			if (isRegistered(key)) throw new IllegalArgumentException("Key is already registered");
			
			ContextKey contextKey = new ContextKey(key); //if this thows an exception, it won't be registered
			REGISTERED_KEYS.add(key);
			return contextKey;
		}
		
	}

}
