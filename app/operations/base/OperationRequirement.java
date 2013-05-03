package operations.base;

import oops.NotAuthedOops;
import helpers.DependentOperation;
import contexts.SessionContext;

/**
 * Represents constraints that can be applied and checked before 
 * 
 * @author bigpopakap
 * @since 2013-05-02
 *
 */
public abstract class OperationRequirement extends DependentOperation<Void, Void> {
	
	/* **************************************************************************
	 *  STATIC DEFINITIONS
	 ************************************************************************** */
	
	public static final OperationRequirement LOGGED_IN = new OperationRequirement() {
		@Override
		protected Void hook_postDependenciesOperate(Void input) {
			//ignore the input, just make sure there is a user logged in
			if (!SessionContext.hasUser()) {
				throw new NotAuthedOops();
			}
			
			//shut up the compiler
			return null;
		}
	};
	
	/* **************************************************************************
	 *  STATIC METHODS
	 ************************************************************************** */
	
	/** Does the validation of all of the given constraints in order */
	public static void validate(OperationRequirement... constraints) {
		for (OperationRequirement contraint : constraints) {
			contraint.validate();
		}
	}
	
	/* **************************************************************************
	 *  CLASS DEFINITION
	 ************************************************************************** */
	
	/** Create a new constraint with the given dependencies */
	public OperationRequirement(OperationRequirement... dependencies) {
		super(dependencies);
	}
	
	/** Does the validation for this constraint and its dependencies */
	private void validate() {
		operate(null);
	}
	
}