package juiforms;

import play.api.templates.Html;
import utils.Message;
import utils.ObjectUtil;
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
	
	/** Creates a new form element */
	public JuiFormInput(JuiFormInputType type, String name, Message label, Message placeholder, Message helpText, boolean keepSubmittedValue, JuiFormInputConstraint[] constraints) {
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
		return this.getClass().getCanonicalName() + ":" + ObjectUtil.getFieldMap(this);
	}
	
	public JuiFormInputType getType() { return type; }
	public String getName() { return name; }
	public boolean hasLabel() { return !StringUtil.isNullOrEmpty(getLabel()); }
	public String getLabel() { return label != null ? label.get() : null; }
	public boolean hasHelpText() { return !StringUtil.isNullOrEmpty(getHelpText()); }
	public String getHelpText() { return helpText != null ? helpText.get() : null; }
	public boolean keepSubmittedValue() { return keepSubmittedValue; }
	public boolean hasPlaceholder() { return !StringUtil.isNullOrEmpty(getPlaceholder()); }
	public String getPlaceholder() { return placeholder != null ? placeholder.get() : null; }
	public boolean hasValue() { return !StringUtil.isNullOrEmpty(getValue()); }
	public String getValue() { return value; }
	public boolean hasError() { return !StringUtil.isNullOrEmpty(getError()); }
	public String getError() { return error; }
	
	public void setValue(String value) { this.value = value; }
	private void setError(String error) { this.error = error; }
	
	public boolean isError() { return getError() != null; }
	
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
		
		return !isError();
	}

	/** Renders the HTML to represent this form input */
	public Html render() {
		return views.html.p_juiFormInput.render(this);
	}
	
}
