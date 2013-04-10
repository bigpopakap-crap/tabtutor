package contexts;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import utils.Universe;
import utils.Universe.UniverseElement;

/**
 * A class that allows other to add any random variables they want
 * 
 * @author bigpopakap
 * @since 2013-04-09
 *
 */
public class AnyContext extends BaseContext {
	
	private static final UniverseElement<String> ANY_CONTEXT_OBJ_KEY = CONTEXT_KEY_UNIVERSE.register("anyContextMapKey");
	public static final Universe<String> ANY_CONTEXT_KEY_UNIVERSE = new Universe<>();
	
	/** Puts a new value into this context */
	public static synchronized void put(UniverseElement<String> anyContextKey, Object object) {
		Map<String, Object> map = getOrLoad(ANY_CONTEXT_OBJ_KEY, OBJECT_MAP_CALLABLE);
		map.put(ANY_CONTEXT_KEY_UNIVERSE.extract(anyContextKey), object);
		set(ANY_CONTEXT_OBJ_KEY, map);
	}
	
	/** Gets the value of the given key */
	public static synchronized Object get(UniverseElement<String> anyContextKey) {
		Map<String, Object> map = getOrLoad(ANY_CONTEXT_OBJ_KEY, OBJECT_MAP_CALLABLE);
		return map.get(ANY_CONTEXT_KEY_UNIVERSE.extract(anyContextKey));
	}
	
	/** Returns a new String-Object map */
	private static final Callable<Map<String, Object>> OBJECT_MAP_CALLABLE = new Callable<Map<String, Object>>() {

		@Override
		public Map<String, Object> call() throws Exception {
			return new HashMap<>();
		}
		
	};

}
