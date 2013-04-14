package forms;

import interfaces.Renderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import play.api.templates.Html;
import play.mvc.Http.Request;

/**
 * TODO
 */
public abstract class Form<T> implements Renderable {
	
	//TODO add form title?
	private final List<String> elementNames;
	private final Map<String, FormElement> elementMap;

	@SuppressWarnings("unchecked")
	public Form(FormElement... elements) throws IllegalStateException {
		if (elements == null) elements = new FormElement[0];
		
		//create the list of element names in order
		elementNames = new ArrayList<>();
		for (FormElement element : elements) {
			elementNames.add(element.getName());
		}
		
		//put the elements in a map and make sure there are no duplicate names
		elementMap = new HashMap<>();
		for (FormElement element : elements) {
			if (elementMap.containsKey(element.getName())) {
				throw new IllegalStateException("Duplicate name found: " + element.getName());
			}
			elementMap.put(element.getName(), element);
		}
	}
	
	/* **************************************************************************
	 *  BEGIN PUBLIC INTERFACE
	 ************************************************************************** */
	
	public void clear() {
		for (FormElement element : elementMap.values()) {
			element.clear();
		}
	}
	
	public T bind(Request req) throws FormInvalidException {
		Map<String, String> reqParams = null; //TODO
		
		bindValues(reqParams);
		Map<String, String> formValues = getValues();
		
		Map<String, String> validationErrors = validate(formValues);
		if (validationErrors != null) {
			return bind(formValues);
		}
		else {
			bindErrors(validationErrors);
			throw new FormInvalidException();
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
	
	private void bindValues(Map<String, String> values) {
		bind(false, values);
	}
	
	private void bindErrors(Map<String, String> errors) {
		bind(true, errors);
	}
	
	private void bind(boolean isErrors, Map<String, String> data) {
		if (data != null) {
			for (Entry<String, String> entry : data.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				
				FormElement element = elementMap.get(key);
				if (element != null) {
					if (isErrors) element.setError(value);
					else element.setValue(value);
				}
			}
		}
	}
	
	private Map<String, String> getValues() {
		Map<String, String> values = new HashMap<String, String>();
		for (FormElement element : elementMap.values()) {
			values.put(element.getName(), element.getValue());
		}
		return values;
	}
	
}