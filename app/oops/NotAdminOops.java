package oops;

import oops.base.BaseOops;
import play.api.templates.Html;
import play.mvc.Result;

/**
 * Exposed error for when the user needs to be an admin to perform the operation
 * 
 * @author bigpopakap
 * @since 2013-05-03
 *
 */
public class NotAdminOops extends BaseOops {
	
	private static final long serialVersionUID = 8850548943038776244L;
	private static final String UNSUPPORTED_REASON = "This should never be called, since the constructor will throw an exception";
	
	/**
	 * Enum for dictating how this particular exception should be surfaced
	 * 
	 * @author bigpopakap
	 * @since 2013-05-03
	 *
	 */
	public static enum ExposeType {
		
		/** Indicates that this should be surfaced as a NOT FOUND,
		 *  which will help hide the existence of this operation but won't be descriptive */
		NOT_FOUND,
		
		/** Indicates that this should be surfaced as an insufficient privileges
		 *  which will be more descriptive, but expose the existence of the operation */
		INSUFFICIENT_PRIVILEGES
		
	}
	
	public NotAdminOops(ExposeType type) {
		super(null);
		switch (type) {
			case NOT_FOUND: throw new NotFoundOops(this);
			case INSUFFICIENT_PRIVILEGES: throw new InsufficientPrivilegesOops(this);
		}
	}

	@Override
	protected Html hook_renderWebResult() {
		throw new UnsupportedOperationException(UNSUPPORTED_REASON);
	}

	@Override
	protected Result apiResult() {
		throw new UnsupportedOperationException(UNSUPPORTED_REASON);
	}

}
