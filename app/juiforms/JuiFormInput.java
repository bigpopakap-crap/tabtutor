package juiforms;

import helpers.Message;
import play.api.templates.Html;
import utils.ReflectUtil;
import utils.StringUtil;

/**
 * A JUI for an HTML form input element
 * 
 * @author bigpopakap
 * @since 2013-04014
 * 
 */
public class JuiFormInput {
	
	private final JuiFormInputType type;				//the input type
	private final String name;							//the name of the form
														//this is also used as an identifying key when binding data
	private final Message label;					//the label for this input
	private final Message helpText;				//help text for this input
	private final boolean keepSubmittedValue;			//if true, the form will keep the submitted value when rendered back to the user with errors
	private final Message placeholder;				//placeholder for a text input
	private final JuiFormInputConstraint[] constraints;	//constraints this field should be validated against
	private String value;								//the value of this input element (can be set)
	private String error;								//the error string of this input (can be set)
														//TODO allow multiple errors to be associated with this field?
	
	/**
	 * Creates a new form input element
	 * The type and name are the only required values
	 * 
	 * @param type the type of the input (required)
	 * @param name the name of the input (required)
	 * @param label label to display next to the input
	 * @param placeholder placeholder text
	 * @param helpText extra text to help the user fill in the field
	 * @param keepSubmittedValue flag to indicate whether this input should retain
	 * 							the user-submitted value, or be able to overwrite it
	 * @param constraints a list of constraints used to validate the input
	 */
	public JuiFormInput(JuiFormInputType type, String name, Message label, Message placeholder, Message helpText, boolean keepSubmittedValue, JuiFormInputConstraint[] constraints) {
		//type and name are the only required fields
		if (type == null) throw new IllegalArgumentException("type cannot be null");
		if (name == null) throw new IllegalArgumentException("name cannot be null");
		
		this.type = type;
		this.name = name;
		this.label = label;
		this.helpText = helpText;
		this.keepSubmittedValue = keepSubmittedValue;
		this.placeholder = placeholder;
		this.constraints = constraints != null ? constraints : new JuiFormInputConstraint[0];
		clear();
	}
	
	/** Default toString that returns the field=value mappings */
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + ":" + ReflectUtil.getFieldMap(this);
	}
	
	/** Get the type of the input */
	public JuiFormInputType getType() {
		return type;
	}

	/** Get the name of the input */
	public String getName() {
		return name;
	}

	/** Determines if this input has a label */
	public boolean hasLabel() {
		return !StringUtil.isNullOrEmpty(getLabel());
	}

	/** Get the label of the input */
	public String getLabel() {
		return label != null ? label.get() : null;
	}

	/** Determines if this input has a help text */
	public boolean hasHelpText() {
		return !StringUtil.isNullOrEmpty(getHelpText());
	}

	/** Get the help text of the input */
	public String getHelpText() {
		return helpText != null ? helpText.get() : null;
	}

	/** Determines if this input is supposed to retain the value
	 *  submitted by the user */
	public boolean keepSubmittedValue() {
		return keepSubmittedValue;
	}

	/** Determines if this input has a placeholder */
	public boolean hasPlaceholder() {
		return !StringUtil.isNullOrEmpty(getPlaceholder());
	}

	/** Get the placeholder of the input */
	public String getPlaceholder() {
		return placeholder != null ? placeholder.get() : null;
	}

	/** Determines if this input has a value */
	public boolean hasValue() {
		return !StringUtil.isNullOrEmpty(getValue());
	}

	/** Get the value of the input */
	public String getValue() {
		return value;
	}

	/** Determines if this input has any error */
	public boolean hasError() {
		return !StringUtil.isNullOrEmpty(getError());
	}

	/** Get the error of the input */
	public String getError() {
		return error;
	}
	
	/**
	 * Set the value of the input
	 * Trims leading and trailing whitespace
	 */
	public void setValue(String value) {
		//trim the value so whitespace-only is considered an empty string
		if (value != null) {
			value = value.trim();
		}
		this.value = value;
	}

	/** Set the error of the input */
	private void setError(String error) {
		this.error = error;
	}
	
	/** Clears the value and error */
	public void clear() {
		setValue(null);
		setError(null);
	}
	
	/** Determines if the input is valid, checks against the associated constraints.
	 * 	If not valid, populates the  */
	public boolean validate() {
		for (JuiFormInputConstraint constraint : constraints) {
			String error = constraint.validate(this);
			if (error != null) {
				setError(error);
			}
		}
		
		return !hasError();
	}

	/** Renders the HTML to represent this form input */
	public Html render() {
		return views.html.p_juiFormInput.render(this);
	}
	
}
