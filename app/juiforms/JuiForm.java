package juiforms;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import play.api.templates.Html;
import types.HttpMethodType;
import utils.ReflectUtil;
import contexts.RequestContext;

/**
 * A JUI for HTML forms. Can render the form,
 * can specify validation, create an object from the values, and
 * also render any form errors
 * 
 * @author bigpopakap
 * @since 2013-04-14
 *
 * @param <T> the object this is a form for
 */
public abstract class JuiForm<T> {
	
	private final List<String> elementNames;				//lists element names in the order they should appear
	private final Map<String, JuiFormInput> elementMap; 	//maps element names to the element objects
	private boolean isBound;								//flag to determine whether the form has bound values
	private boolean isValid;								//flag to determine whether the data bound to the form is valid

	//TODO add default CSRF token
	
	/**
	 * Creates a new form object with the given form elements in the order given
	 * Forms must not have elements with duplicate names
	 * @throws IllegalStateException any two form elements have the same name
	 */
	public JuiForm(JuiFormInput[] elements) throws IllegalStateException {
		if (elements == null) elements = new JuiFormInput[0];
		
		//create the list of element names in order
		elementNames = new ArrayList<>();
		for (JuiFormInput element : elements) {
			elementNames.add(element.getName());
		}
		
		//put the elements in a map and make sure there are no duplicate names
		elementMap = new HashMap<>();
		for (JuiFormInput element : elements) {
			if (elementMap.containsKey(element.getName())) {
				throw new IllegalStateException("Duplicate name found: " + element.getName());
			}
			elementMap.put(element.getName(), element);
		}
		
		clear();
	}
	
	/* **************************************************************************
	 *  BEGIN PUBLIC OVERRIDES
	 ************************************************************************** */
	
	/** Default toString that returns the field=value mappings */
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + ":" + ReflectUtil.getFieldMap(this);
	}
	
	/* **************************************************************************
	 *  BEGIN PUBLIC INTERFACE
	 ************************************************************************** */
	
	/** Determines whether the form has bound values */
	public boolean isBound() {
		return isBound;
	}
	
	/** Determines whether the data bound to the form is valid.
	 *  The value of this method means nothing if {@link #isBound()} is false */
	public boolean isValid() {
		return isValid;
	}
	
	/** Clears any bound values */
	public void clear() {
		try {
			for (JuiFormInput element : elementMap.values()) {
				element.clear();
			}
		}
		finally {
			isBound = false;
			isValid = false;
		}
	}
	
	/** Binds the form to the parameters in the given request */
	public T bind() throws JuiFormValidationException {
		try {
			bindValues(RequestContext.queryParams());
			validate();
			
			if (isValid()) {
				return bind(getValues());
			}
			else {
				throw new JuiFormValidationException();
			}
		}
		finally {
			isBound = true;
		}
	}
	
	/**
	 * Renders the HTML to represent this form
	 * TODO add option to display on one line/mult-line?
	 * 
	 * @param method the method for the form to use on submit
	 * @param action the URL for the form to submit to
	 */
	public Html render(String title, String subtitle, HttpMethodType method, String action) {
		return views.html.p_juiForm.render(title, subtitle, method, action, getInputElements());
	}
	
	/* **************************************************************************
	 *  BEGIN ABSTRACT METHODS
	 ************************************************************************** */
	
	/**
	 * Creates an object, given the data bound to the form.
	 * 
	 * Note: this method will only be called on a form that has been
	 * validated against the contraints associated with each field.
	 * Therefore, it is assumed that the data is valid and an object
	 * can be created from this form
	 * 
	 * @param data a map from field names to their values
	 * @return the object derived from these values
	 */
	protected abstract T bind(Map<String, String> data);
	
	/* **************************************************************************
	 *  BEGIN PRIVATE HELPERS
	 ************************************************************************** */
	
	/** Validates the fields against the data bound to them.
	 *  Populates them with error messages, if applicable */
	private void validate() {
		int badCount = 0; //use a count instead of just &&ing values together to avoid it skipping executions
		for (JuiFormInput input : getInputElements()) {
			if (!input.validate()) {
				badCount++;
			}
		}
		
		isValid = (badCount == 0);
	}
	
	/**
	 * Binds values to this form
	 * @param values a map of the field names to values
	 */
	private void bindValues(Map<String, String> values) {
		for (JuiFormInput input : getInputElements()) {
			input.setValue(values.get(input.getName()));
		}
	}
	
	/** Gets the values of the fields in this form */
	private Map<String, String> getValues() {
		//TODO cache the result of this method
		Map<String, String> values = new HashMap<String, String>();
		for (JuiFormInput element : elementMap.values()) {
			values.put(element.getName(), element.getValue());
		}
		return values;
	}
	
	/** Gets the input elements in order */
	private List<JuiFormInput> getInputElements() {
		//TODO cache the result of this method (this can probably be calculated in the constructor itself)
		List<JuiFormInput> inputs = new LinkedList<>();
		for (String name : elementNames) {
			inputs.add(elementMap.get(name));
		}
		return inputs;
	}
	
}