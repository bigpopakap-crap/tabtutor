package utils;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that can hold a bunch of unique values
 * 
 * @author bigpopakap
 * @since 2013-04-9
 *
 * @param <T> the type that the universe holds. Must implements a good hashCode()
 */
public class Universe<T> {
	
	/** The set that holds the actual values */
	private final Set<T> set;
	
	/** Creates a new empty universe */
	public Universe() {
		set = new HashSet<T>();
	}
	
	/**
	 * Registers the given value with the universe
	 * @param val the value to register
	 * @return a {@link UniverseElement} object wrapping the registered value
	 * @throws IllegalArgumentException if the value is null
	 * @throws IllegalStateException if the value is already registered
	 */
	public synchronized UniverseElement<T> register(T val) throws IllegalArgumentException, IllegalStateException {
		if (val == null) {
			throw new IllegalArgumentException("val cannot be null");
		}
		else if (set.add(val)) {
			return new UniverseElement<>(this, val);
		}
		else {
			throw new IllegalStateException("value " + val + " already registered");
		}
	}
	
	/**
	 * Gets the value of the universe element
	 * @param elem the element to get the value of
	 * @return the value of the element
	 * @throws IllegalStateException if the element was not registered with this universe
	 */
	public synchronized T extract(UniverseElement<T> elem) throws IllegalStateException {
		if (elem.universe() != this) {
			throw new IllegalStateException("elem was not generated from this universe");
		}
		
		return elem.val();
	}
	
	/**
	 * Object representing a value registered with this {@link Universe}
	 *
	 * @author bigpopakap
	 * @since 2013-04-09
	 * 
	 * @param <T> the type of the value of the element
	 *
	 */
	public static class UniverseElement<T> {
		
		private final Universe<T> universe;	//the universe this was registered to
		private final T val;				//the actual value
		
		/** Creates a new object with the universe it was registered to, and the value */
		private UniverseElement(Universe<T> universe, T val) {
			if (universe == null) throw new IllegalArgumentException("universe cannot be null");
			if (val == null) throw new IllegalArgumentException("val cannot be null");
			this.universe = universe;
			this.val = val;
		}
		
		/** Gets the universe this element was registered to */
		private synchronized Universe<T> universe() {
			return universe;
		}
		
		/** Gets the value of the element */
		private synchronized T val() {
			return val;
		}
		
	}

}
