package helpers;

import contexts.AppContext;

/**
 * Class that can define variables that have a default value in production
 * 
 * These values can be set locally to change behavior, usually for debugging,
 * but have a default value in production to protect against accidental check-ins
 * that set it to something stupid
 * 
 * Examples of initializing them
 * 		DevelopmentSwitch<Integer> switch = new DevelopmentSwitch<>(5) 			//creates one with the value 5
 * 		DevelopmentSwitch<Integer> switch = new DevelopmentSwitch<>(5).set(8)	//creates one with production value 5, and sets it to 8
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 * @param <T>
 */
public class DevelopmentSwitch<T> {
	
	//TODO unit test that this changes in development but not production
	
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