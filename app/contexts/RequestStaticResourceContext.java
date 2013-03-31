package contexts;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Context for the request that holds which static resources the returned HTML page
 * is going to reference, so that if multiple partials request the same static resource,
 * it will only be included once
 * 
 * @author bigpopakap
 * @since 2013-03-17
 *
 */
public class RequestStaticResourceContext extends BaseContext {
	
	/** The key used to store the set of resources for the request context */
	private static final ContextKey PAGE_RESOURCE_CONTEXT_KEY = ContextKey.register("pageResourceContextKey");
	
	/** Gets the set of resources for the request context */
	public static synchronized Set<String> get() {
		return getOrLoad(PAGE_RESOURCE_CONTEXT_KEY, RESOURCE_URL_LOADER);
	}
	
	/** Determines if the given static resource url has already been included in this request context */
	public static synchronized boolean contains(String url) {
		return get().contains(url);
	}
	
	/** Adds the given static resource url to the request context
	 *  @return the given url for convenience */
	public static synchronized String add(String url) {
		get().add(url);
		return url;
	}
	
	/* *****************************************************
	 *  BEGIN CALLABLE HELPERS
	 ***************************************************** */
	
	/** The Callable that can get the set of static resoure urls */
	private static final Callable<Set<String>> RESOURCE_URL_LOADER = new Callable<Set<String>>() {

		@Override
		public Set<String> call() throws Exception {
			//just create a new one, since there's no persistent storage for this
			return new HashSet<>();
		}
		
	};
	
}
