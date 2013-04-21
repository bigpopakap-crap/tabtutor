package juiforms;

/**
 * The supported types of form input elements
 * 
 * @author bigpopakap
 * @since 2013-04-14
 *
 */
public enum JuiFormInputType {
	
	BUTTON,
	EMAIL,
	HIDDEN,
	NUMBER,
	PASSWORD,
	RESET,
	SUBMIT,
	TEXT;
	
	private final String htmlType;
	
	private JuiFormInputType() {
		this(null);
	}
	
	/**
	 * Create a new input type, using the specified value as HTML attribute
	 * @param htmlType the value to use as the value of the HTML "type" attribute. If
	 * 			null, defaults to the name of the enum member in lowercase (using {@link #name()})
	 */
	private JuiFormInputType(String htmlType) {
		this.htmlType = htmlType != null ? htmlType : name().toLowerCase();
	}
	
	/** Returns the value that should be as the value of the HTML "type" attribute */
	public String getHtmlType() {
		return htmlType;
	}
	
	/** Simply returns {@link #getHtmlType()} */
	@Override
	public String toString() {
		return getHtmlType();
	}

}
