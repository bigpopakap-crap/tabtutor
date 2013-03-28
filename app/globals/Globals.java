package globals;

import contexts.AppContext;

/**
 * This class holds all static final variables that should be accessible by the whole app,
 * and not specific to any use case. Other classes in this package may contain other constants
 * specific to certain use cases
 * 
 * These global classes differ from AppContext because these are not meant to be used as environment vars.
 * They are not descriptive of the environment in which the app is running.
 * 
 * @author bigpopakap
 * @since 2013-02-23
 *
 */
public abstract class Globals {
	
	/**
	 * Class that can define variables that have a default value in production
	 * 
	 * These values can be set locally to change behavior, usually for debugging,
	 * but have a default value in production to protect against accidental check-ins
	 * that set it to something stupid
	 * 
	 * Examples of initializing them
	 * 		DevelopmentSwitch<Integer> switch = new DevelopmentSwitch<Integer>(5) 			//creates one with the value 5
	 * 		DevelopmentSwitch<Integer> switch = new DevelopmentSwitch<Integer>(5).set(8)	//creates one with production value 5, and sets it to 8
	 * 
	 * @author bigpopakap
	 * @since 2013-03-27
	 *
	 * @param <T>
	 */
	public static class DevelopmentSwitch<T> {
		
		private T value;
		
		/** Creates a new switch with the given default production value,
		 *  and sets the current value to that value */
		public DevelopmentSwitch(T defaultProductionValue) {
			this.value = defaultProductionValue;
		}
		
		/** Sets the the value if not production mode
		 * @return this object for convenience */
		public synchronized DevelopmentSwitch<T> set(T value) {
			if (!AppContext.Mode.isProduction()) {
				this.value = value;
			}
			return this;
		}
		
		/** Gets the value */
		public synchronized T get() {
			return value;
		}
		
	}
	
}
