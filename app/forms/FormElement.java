package forms;

import interfaces.Renderable;
import play.api.templates.Html;

/**
 * TODO
 */
public class FormElement implements Renderable {
	
	private final FormElementType type;
	private final String name;
	private final String helpText;
	private final String helpSubtext;
	private String value;
	private String error;
	
	public FormElement(FormElementType type, String name, String helpText, String helpSubtext) {
		if (type == null) throw new IllegalArgumentException("type cannot be null");
		if (name == null) throw new IllegalArgumentException("name cannot be null");
		if (helpText == null) throw new IllegalArgumentException("helpText cannot be null");
		
		this.type = type;
		this.name = name;
		this.helpText = helpText;
		this.helpSubtext = helpSubtext;
		clear();
	}
	
	public FormElementType getType() { return type; }
	public String getName() { return name; }
	public String getHelpText() { return helpText; }
	public String getHelpSubtext() { return helpSubtext; }
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
