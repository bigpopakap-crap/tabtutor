package juiforms;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.SessionCsrfTokenModel;
import models.exceptions.FailedOperationException;
import play.api.templates.Html;
import types.HttpMethodType;
import utils.ObjectUtil;
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
	public JuiForm(JuiFormInput[] elementArr) throws IllegalStateException {
		List<JuiFormInput> elements = new LinkedList<>(Arrays.asList(elementArr));
		
		//append other automatically-added elements
		if (appendCsrfToken()) {
			elements.add(new JuiFormInput(JuiFormInputType.HIDDEN, "csrf", null, null, null, new JuiFormInputConstraint[] {
				JuiFormInputConstraint.CSRF_TOKEN
			}));
		}
		if (appendSubmit()) {
			elements.add(new JuiFormInput(JuiFormInputType.SUBMIT, "submit", "Submit", null, null, null));
		}
		
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
		return this.getClass().getCanonicalName() + ":" + ObjectUtil.getFieldMap(this);
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
			clear();
			bindValues(RequestContext.params());
			validate();
			
			if (isValid()) {
				try {
					return bind(getValues());
				}
				catch (FailedOperationException ex) {
					throw new RuntimeException("Database operation failed", ex);
				}
				catch (Exception ex) {
					//something bad happened while creating the element
					//This should be fixed and not exposed to the user
					throw new RuntimeException(
						"Error while binding in " + this.getClass().getCanonicalName() +
							". Probably an exception thrown while creating a DB object, " +
							"which means the form is missing some validation it needs, like unique value checking",
						ex
					);
				}
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
		//bind default values
		Map<String, String> defaultValues = new HashMap<String, String>();
		preRenderBind(defaultValues);
		bindValues(defaultValues);
		
		return views.html.p_juiForm.render(title, subtitle, method, action, getInputElements());
	}
	
	/* **************************************************************************
	 *  BEGIN ABSTRACT METHODS AND HOOKS
	 ************************************************************************** */
	
	/**
	 * Called just before the form is rendered, allows the form to specify any
	 * default values for the fields
	 * 
	 * Default implementation is to do nothing
	 * 
	 * @param data the name-value map that the method should populate with default
	 * values. Guaranteed to be an initialized empty map when the method is called
	 */
	protected void hook_preRenderBind(Map<String, String> defaultValues) {
		//do nothing
	}
	
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
	
	/**
	 * Controls whether the form automatically appends a submit button
	 * Default implementation returns true
	 */
	protected boolean appendSubmit() {
		return true;
	}
	
	/**
	 * Controls whether the form automatically appends a CSRF token to be validated
	 * Default implementation returns true
	 */
	protected boolean appendCsrfToken() {
		return true;
	}
	
	/* **************************************************************************
	 *  BEGIN PRIVATE HELPERS
	 ************************************************************************** */
	
	private void preRenderBind(Map<String, String> defaultValues) {
		hook_preRenderBind(defaultValues);
		
		//create a new token
		defaultValues.put("csrf", SessionCsrfTokenModel.create().getCsrfToken().toString());
	}
	
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
	 * Binds values to this form. Does not overwrite existing values
	 * @param values a map of the field names to values
	 */
	private void bindValues(Map<String, String> values) {
		for (JuiFormInput input : getInputElements()) {
			if (!input.hasValue()) {
				input.setValue(values.get(input.getName()));
			}
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