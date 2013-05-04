package helpers;

import oops.NotAdminAsNotFoundOops;
import oops.NotAuthedOops;
import contexts.AppContext;
import contexts.SessionContext;

/**
 * Represents requirements that can be applied and checked before this requirement,
 * which should be used to validate operations before they are performed
 * 
 * @author bigpopakap
 * @since 2013-05-02
 *
 */
public abstract class OperationReq extends DependentOperation<Void, Void> {
	
	private OperationReq() {} //prevent instantiation
	
	/* **************************************************************************
	 *  STATIC DEFINITIONS
	 ************************************************************************** */
	
	public static final OperationReq IS_LOGGED_IN = new OperationReq() {
		@Override
		protected void hook_requireAndThrow() {
			//ignore the input, just make sure there is a user logged in
			if (!SessionContext.hasUser()) {
				throw new NotAuthedOops();
			}
		}
	};
	
	public static final OperationReq IS_DEV_MODE_OR_ADMIN_USER = new OperationReq() {
		@Override
		protected void hook_requireAndThrow() {
			boolean isDev = AppContext.Mode.isDevelopment();
			boolean isAdmin = SessionContext.hasUser() && SessionContext.user().getUsername().equals("bigpopakap"); //TODO don't hardcode
			if (!isDev && !isAdmin) {
				throw new NotAdminAsNotFoundOops(null);
			}
		}
	};
	
	/* **************************************************************************
	 *  STATIC METHODS
	 ************************************************************************** */
	
	/**
	 * Does the validation of all of the given constraints in order
	 * @see #requireAndThrow()
	 */
	public static void requireAndThrow(OperationReq... constraints) {
		for (OperationReq contraint : constraints) {
			contraint.requireAndThrow();
		}
	}
	
	/* **************************************************************************
	 *  CLASS DEFINITION
	 ************************************************************************** */
	
	/** Create a new constraint with the given dependencies */
	public OperationReq(OperationReq... dependencies) {
		super(dependencies);
	}
	
	/**
	 * Does the validation for this constraint and its dependencies
	 * 
	 * These requirements will not return anything, they will simply throw an exception if the
	 * requirement is not met
	 */
	private void requireAndThrow() {
		operate(null);
	}
	
	//override this so the new method to be overridden (the one called here) can be more descriptively named
	@Override
	protected final Void hook_postDependenciesOperate(Void input) {
		hook_requireAndThrow();
		return null;
	}
	
	protected abstract void hook_requireAndThrow();
	
}