package helpers;

import oops.NotAdminAsNotFoundOops;
import oops.NotAuthedOops;
import oops.NotFoundOops;
import oops.base.BaseOops;
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
public abstract class OperationReq extends DependentOperation<Void, Boolean> {
	
	/* **************************************************************************
	 *  STATIC DEFINITIONS
	 ************************************************************************** */
	
	public static final OperationReq IS_LOGGED_IN = new OperationReq() {
		@Override
		protected boolean hook_verify() {
			return SessionContext.hasUser();
		}
		@Override
		protected void hook_throwDefaultOops() throws BaseOops {
			throw new NotAuthedOops();
		}
	};
	
	public static final OperationReq IS_DEV_MODE = new OperationReq() {
		@Override
		protected boolean hook_verify() {
			return AppContext.Mode.isDevelopment();
		}
		@Override
		protected void hook_throwDefaultOops() throws BaseOops {
			throw new NotFoundOops(null);
		}
	};
	
	public static final OperationReq IS_ADMIN_USER = new OperationReq(IS_LOGGED_IN) {
		@Override
		protected boolean hook_verify() {
			return SessionContext.user().getUsername().equals("bigpopakap"); //TODO don't hardcode this
		}
		@Override
		protected void hook_throwDefaultOops() throws BaseOops {
			throw new NotAdminAsNotFoundOops(null);
		}
	};
	
	public static final OperationReq IS_DEV_MODE_OR_ADMIN_USER = or(IS_DEV_MODE, IS_ADMIN_USER);
	
	/* **************************************************************************
	 *  PUBLIC HELPERS
	 ************************************************************************** */
	
	public static OperationReq verifyReqAnd(OperationReq... reqs) {
		if (reqs ==  null) reqs = new OperationReq[0];
		
		for (OperationReq req : reqs) {
			if (!req.verify()) {
				return req;
			}
		}
		
		return null;
	}
	
	public static void verifyThrowAnd(OperationReq... reqs) {
		OperationReq failed = verifyReqAnd(reqs);
		if (failed != null) {
			failed.throwDefaultOops();
		}
	}
	
	public static boolean verifyBooleanAnd(OperationReq... reqs) {
		return verifyReqAnd(reqs) == null;
	}
	
	public static boolean verifyBooleanOr(OperationReq... reqs) {
		if (reqs ==  null) reqs = new OperationReq[0];
		
		for (OperationReq req : reqs) {
			if (req.verify()) {
				return true;
			}
		}
		
		return false;
	}
	
	/* **************************************************************************
	 *  BEGIN CLASS DEFINITION
	 ************************************************************************** */
	
	/* BEGIN PRIVATE COMPOSERS ************************************************ */
	
	private static OperationReq and(final OperationReq... reqs) {
		return new OperationReq() {
			@Override
			protected boolean hook_verify() {
				return verifyBooleanAnd(reqs);
			}
			@Override
			protected void hook_throwDefaultOops() throws BaseOops {
				verifyThrowAnd(reqs);
			}
		};
	}
	
	private static OperationReq or(final OperationReq... reqs) {
		return new OperationReq() {
			@Override
			protected boolean hook_verify() {
				return verifyBooleanOr(reqs);
			}
			@Override
			protected void hook_throwDefaultOops() throws BaseOops {
				//if this check failed, and there is a first req, throw its exception
				if (!verify() && reqs != null && reqs.length > 0 && reqs[0] != null) {
					reqs[0].throwDefaultOops();
				}
			}
		};
	}
	
	/* BEGIN ABSTRACT METHODS AND CONSTRUCTORS TO IMPLEMENT ******************* */
	
	/** Create a new constraint with the given dependencies */
	private OperationReq(OperationReq... dependencies) {
		super(dependencies);
	}
	
	//override this so the new method to be overridden (the one called here) can be more descriptively named
	@Override
	protected final Boolean hook_operatePostDependencies(Void input) {
		return hook_verify();
	}
	
	/* BEGIN PUBLIC METHOD DECLARATIONS  *************************************** */
	
	/** @return true if the requirement passes, false otherwise */
	public boolean verify() {
		return operate(null);
	}
	
	/** Tests the requirement, and throws the default {@link BaseOops} if the req is not passed */
	public void verifyAndThrow() throws BaseOops {
		if (!verify()) {
			throwDefaultOops();
		}
	}
	
	/* BEGIN PRIVATE HELPERS ************************************************** */
	
	/** Throws the default {@link BaseOops} for this requirement */
	private void throwDefaultOops() throws BaseOops {
		//throw the oops
		try {
			hook_throwDefaultOops();
		}
		catch (Exception ex) {
			//make sure the requirement is false, otherwise it makes no sense to be throwing
			if (verify()) {
				//don't throw an exception, just log
				Logger.warn("Throwing " + BaseOops.class.getCanonicalName() +
							" for " + this.getClass().getCanonicalName() +
							" whose conditions have been met");
			}
			
			throw ex;
		}
		
		//if the hook doesn't throw one, throw an exception here
		throw new IllegalStateException("Previous line should have thrown an exception");
	}
	
	/* BEGIN ABSTRACT METHOD DECLARATIONS  ************************************ */
	
	/**
	 * Implements the verification method that checks the requirement passes
	 * @return true if the requirement passes, false otherwise
	 */
	protected abstract boolean hook_verify();
	
	/**
	 * Implements the throwing of the default {@link BaseOops} for this requirement
	 * @throws BaseOops always
	 */
	protected abstract void hook_throwDefaultOops() throws BaseOops;
	
}