package juiforms;

import utils.MessagesEnum;
import utils.StringUtil;




public enum JuiFormInputConstraint {
	
	REQUIRED() {

		@Override
		protected String hook_validate(JuiFormInput input) {
			if (input.getValue().isEmpty()) return MessagesEnum.formError_required.get(input.getName());
			else return null;
		}
		
	},
	IS_INTEGER() {

		@Override
		protected String hook_validate(JuiFormInput input) {
			if (!StringUtil.isInteger(input.getValue())) return MessagesEnum.formError_notInteger.get(input.getName());
			else return null;
		}
		
	};
	
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
