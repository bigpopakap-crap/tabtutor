package forms;

import interfaces.Renderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import play.api.templates.Html;
import play.mvc.Http.Request;
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
public abstract class JuiFormElement<T> implements Renderable {
	
	//TODO add form title?
	private final List<String> elementNames;			//lists element names in the order they should appear
	private final Map<String, JuiFormInputElement> elementMap; 	//maps element names to the element objects
	private boolean isBound;							//flag to determine whether the form has bound values

	//TODO add default CSRF token
	
	/**
	 * Creates a new form object with the given form elements in the order given
	 * Forms must not have elements with duplicate names
	 * @throws IllegalStateException any two form elements have the same name
	 */
	public JuiFormElement(JuiFormInputElement... elements) throws IllegalStateException {
		if (elements == null) elements = new JuiFormInputElement[0];
		
		//create the list of element names in order
		elementNames = new ArrayList<>();
		for (JuiFormInputElement element : elements) {
			elementNames.add(element.getName());
		}
		
		//put the elements in a map and make sure there are no duplicate names
		elementMap = new HashMap<>();
		for (JuiFormInputElement element : elements) {
			if (elementMap.containsKey(element.getName())) {
				throw new IllegalStateException("Duplicate name found: " + element.getName());
			}
			elementMap.put(element.getName(), element);
		}
		
		clear();
	}
	
	/* **************************************************************************
	 *  BEGIN PUBLIC INTERFACE
	 ************************************************************************** */
	
	/** Determines whether the form has bound values */
	public boolean isBound() {
		return isBound;
	}
	
	/** Clears any bound values */
	public void clear() {
		try {
			for (JuiFormInputElement element : elementMap.values()) {
				element.clear();
			}
		}
		finally {
			isBound = false;
		}
	}
	
	/** Binds the form to the parameters in the given request */
	public T bind(Request req) throws JuiFormElementInvalidException {
		try {
			bindValues(RequestContext.queryParams());
			Map<String, String> formValues = getValues();
			
			Map<String, String> validationErrors = validate(formValues);
			if (validationErrors != null) {
				return bind(formValues);
			}
			else {
				bindErrors(validationErrors);
				throw new JuiFormElementInvalidException();
			}
		}
		finally {
			isBound = true;
		}
	}
	
	@Override
	public Html render() {
		//TODO
		return null;
	}
	
	/* **************************************************************************
	 *  BEGIN ABSTRACT METHODS
	 ************************************************************************** */
	
	protected abstract T bind(Map<String, String> data);
	protected abstract Map<String, String> validate(Map<String, String> data);
	
	/* **************************************************************************
	 *  BEGIN PRIVATE HELPERS
	 ************************************************************************** */
	
	/**
	 * Binds values to this form
	 * @param values a map of the field names to values
	 */
	private void bindValues(Map<String, String> values) {
		bind(false, values);
	}
	
	/**
	 * Binds errors to this form
	 * @param errors a map of the field names to error
	 */
	private void bindErrors(Map<String, String> errors) {
		bind(true, errors);
	}
	
	/**
	 * Binds values or errors to the form
	 * @param isErrors flag indicating whether the data represents field values or errors
	 * @param data a map of fiend names to the values/errors
	 */
	private void bind(boolean isErrors, Map<String, String> data) {
		if (data != null) {
			for (Entry<String, String> entry : data.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				
				JuiFormInputElement element = elementMap.get(key);
				if (element != null) {
					if (isErrors) element.setError(value);
					else element.setValue(value);
				}
			}
		}
	}
	
	/** Gets the values of the fields in this form */
	private Map<String, String> getValues() {
		//TODO cache the result of this method
		Map<String, String> values = new HashMap<String, String>();
		for (JuiFormInputElement element : elementMap.values()) {
			values.put(element.getName(), element.getValue());
		}
		return values;
	}
	
}