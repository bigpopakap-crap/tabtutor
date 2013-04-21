package juiforms;

import models.SessionCsrfTokenModel;
import utils.ConcurrentUtil;
import utils.MessagesEnum;
import utils.StringUtil;
import controllers.exceptions.web.CsrfTokenInvalidErrorPageException;

public abstract class JuiFormInputConstraint {
	
	/* **************************************************************************
	 *  STATIC DEFINITIONS
	 ************************************************************************** */
	
	/** Makes an input field required */
	public static final JuiFormInputConstraint REQUIRED = new JuiFormInputConstraint() {
			
		@Override
		protected String hook_validate(JuiFormInput input) {
			if (input.getValue().isEmpty()) return MessagesEnum.formError_required.get(input.getName());
			else return null;
		}

	};
	
	public static final JuiFormInputConstraint CSRF_TOKEN = new JuiFormInputConstraint() {
		
		@Override
		protected String hook_validate(JuiFormInput input) {
			if (!SessionCsrfTokenModel.isValidToken(input.getValue())) {
				//throw an exception here instead, because this is bad
				throw new CsrfTokenInvalidErrorPageException();
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
			protected String hook_validate(JuiFormInput input) {
				validator.setValue(input.getValue());
				
				//call the validator, let any exceptions bubble up as runtime exceptions
				if (ConcurrentUtil.callQuietly(validator)) {
					//the value is indeed unique
					return null;
				}
				else {
					return MessagesEnum.formError_notUnique.get(input.getName());
				}
			}
			
		};
	}
	
	/** Ensures that the value has at least the given length */
	public static final JuiFormInputConstraint MIN_LENGTH(int len) {
		return new JuiFormInputConstraint() {
			
			@Override
			protected String hook_validate(JuiFormInput input) {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	/** Ensures that the value represents an integer */
	public static final JuiFormInputConstraint IS_INTEGER = new JuiFormInputConstraint() {

		@Override
		protected String hook_validate(JuiFormInput input) {
			if (!StringUtil.isInteger(input.getValue())) return MessagesEnum.formError_notInteger.get(input.getName());
			else return null;
		}
		
	};
	
	/* **************************************************************************
	 *  CLASS DEFINITION
	 ************************************************************************** */
	
	private final JuiFormInputConstraint[] dependencies;
	
	private JuiFormInputConstraint(JuiFormInputConstraint... dependencies) {
		this.dependencies = dependencies != null ? dependencies : new JuiFormInputConstraint[0];
	}
	
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
		
		return hook_validate(input);
	}
	
	protected abstract String hook_validate(JuiFormInput input);

}
