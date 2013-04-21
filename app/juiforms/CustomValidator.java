package juiforms;

import java.util.concurrent.Callable;

/**
 * Class that can do custom validation on an input field. Usually, it will be
 * supplied as an argument to a {@link JuiFormInputConstraint} value, and the
 * required behavior of the validator should be specified in the constraint
 * 
 * @author bigpopakap
 * @since 2013-04-17
 *
 * @param <I> the value that this class will take in (in the constructor)
 * @param <O>
 */
public abstract class CustomValidator<I, O> implements Callable<O> {
	
	//TODO this can later be extended to allow custom validators to take in multiple values
	/** The value of the field the custom validator should validate */
	private I value;
	
	/** Gets the input's value */
	public I value() {
		return value;
	}
	
	/** 
	 * Sets the input's value.
	 * 
	 * This should really only ever be called by the constraint to set the value,
	 * which is why is has the default access level
	 */
	void setValue(I value) {
		this.value = value;
	}

}
