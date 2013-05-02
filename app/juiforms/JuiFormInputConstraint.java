package juiforms;

import helpers.Message;
import models.SessionCsrfTokenModel;
import utils.ConcurrentUtil;
import utils.StringUtil;
import controllers.exceptions.CsrfTokenInvalidExposedException;

/**
 * Constraints that can be added to form inputs. These provide an implementation for
 * verifying them on the server side, and can have UI couterparts that can be enforced
 * by Javascript
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-04-14
 *
 */
public abstract class JuiFormInputConstraint {
	
	/* **************************************************************************
	 *  STATIC DEFINITIONS
	 ************************************************************************** */
	
	/** Makes an input field required */
	public static final JuiFormInputConstraint REQUIRED = new JuiFormInputConstraint() {
			
		@Override
		protected Message hook_validate(JuiFormInput input) {
			if (input == null || !input.hasValue() || StringUtil.isOnlyWhitespace(input.getValue())) return Message.formError_required;
			else return null;
		}

	};
	
	/** Does CSRF validation */
	public static final JuiFormInputConstraint CSRF_TOKEN = new JuiFormInputConstraint(REQUIRED) {
		
		@Override
		protected Message hook_validate(JuiFormInput input) {
			if (!SessionCsrfTokenModel.isValidToken(input.getValue())) {
				//throw an exception here instead, because this is bad
				throw new CsrfTokenInvalidExposedException();
			}
			else return null;
		}
		
	};
	
	/**
	 * Ensures that the value is unique. The given custom validator should do whatever it needs to do to
	 * verify the uniqueness.
	 * 
	 * The custom validator should take in the String value of the input, and return:
	 * true, if the value is indeed unique
	 * false, if the value is not unique
	 *  
	 */
	public static final JuiFormInputConstraint UNIQUE(final CustomValidator<String, Boolean> validator) {
		return new JuiFormInputConstraint() {

			@Override
			protected Message hook_validate(JuiFormInput input) {
				validator.setValue(input.getValue());
				
				//call the validator, let any exceptions bubble up as runtime exceptions
				if (ConcurrentUtil.callQuietly(validator)) {
					//the value is indeed unique
					return null;
				}
				else {
					return Message.formError_notUnique;
				}
			}
			
		};
	}
	
	/** Ensures that the value has at least the given length */
	public static final JuiFormInputConstraint MIN_LENGTH(int len) {
		return new JuiFormInputConstraint() {
			
			@Override
			protected Message hook_validate(JuiFormInput input) {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	/** Ensures that the value represents an integer */
	public static final JuiFormInputConstraint IS_INTEGER = new JuiFormInputConstraint() {

		@Override
		protected Message hook_validate(JuiFormInput input) {
			if (!StringUtil.isInteger(input.getValue())) return Message.formError_notInteger;
			else return null;
		}
		
	};
	
	/* **************************************************************************
	 *  CLASS DEFINITION
	 ************************************************************************** */
	
	/** Other constraints on which this one depends */
	private final JuiFormInputConstraint[] dependencies;
	
	/** Create a new constraint with the given dependencies (in order) */
	private JuiFormInputConstraint(JuiFormInputConstraint... dependencies) {
		this.dependencies = dependencies != null ? dependencies : new JuiFormInputConstraint[0];
	}
	
	/** 
	 * Validate the given input field. Does some error checking, but the
	 * actual implementation is in {@link #hook_validate(JuiFormInput)}
	 * 
	 * @param input the field to validate
	 * @return the error message to display
	 */
	public final String validate(JuiFormInput input) {
		if (input == null) {
			throw new IllegalStateException("input cannot be null");
		}
		else if (input.getValue() == null) {
			throw new IllegalStateException("value cannot be null");
		}
		//if this is not the required constraint and the value is empty, just skip it
		else if (this != REQUIRED && input.getValue().isEmpty()) {
			return null;
		}
		
		//validate against the dependencies
		for (JuiFormInputConstraint constraint : dependencies) {
			String error = constraint.validate(input);
			if (error != null) {
				return error;
			}
		}
		
		Message errorMessage = hook_validate(input);
		return errorMessage != null ? errorMessage.get(input.getName()) : null;
	}
	
	/**
	 * Implements the validation of the input field
	 * 
	 * @param input the field to validate
	 * @return the {@link Message} representing the error, or null if the input is valid.
	 * 			The returned message must take 0 or 1 arguments, and when rendered it will be passed
	 * 			the name of the input field
	 */
	protected abstract Message hook_validate(JuiFormInput input);

}
