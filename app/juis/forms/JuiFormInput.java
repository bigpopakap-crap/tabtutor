package juis.forms;

import juis.Renderable;
import play.api.templates.Html;

/**
 * A JUI for an HTML form input element
 * 
 * @author bigpopakap
 * @since 2013-04014
 * 
 */
public class JuiFormInput implements Renderable {
	
	private final JuiFormInputType type;		//the input type
	private final String name;					//the name of the form
												//this is also used as an identifying key when binding data
	private final String label;					//the label for this input
	private final String helpText;				//help text for this input
	private String value;						//the value of this input element (can be set)
	private String error;						//the error stirng of this input (can be set)
	
	/** Creates a new form element */
	public JuiFormInput(JuiFormInputType type, String name, String label) {
		this(type, name, label, null);
	}
	
	/** Creates a new form element */
	public JuiFormInput(JuiFormInputType type, String name, String label, String helpText) {
		if (type == null) throw new IllegalArgumentException("type cannot be null");
		if (name == null) throw new IllegalArgumentException("name cannot be null");
		if (label == null) throw new IllegalArgumentException("helpText cannot be null");
		
		this.type = type;
		this.name = name;
		this.label = label;
		this.helpText = helpText;
		clear();
	}
	
	public JuiFormInputType getType() { return type; }
	public String getName() { return name; }
	public String getLabel() { return label; }
	public String getHelpText() { return helpText; }
	public String getValue() { return value; }
	public String getError() { return error; }
	
	public void setValue(String value) { this.value = value; }
	public void setError(String error) { this.error = error; }
	
	public boolean isError() { return getError() != null; }
	
	public void clear() {
		setValue(null);
		setError(null);
	}

	@Override
	public Html render() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
