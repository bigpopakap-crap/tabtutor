package contexts;

import java.util.concurrent.Callable;

import utils.Universe.UniverseElement;
import controllers.exceptions.BaseExposedException;

/**
 * This class can be used to store any errors and error messages for the duration of the
 * request. Note that all the data stored in this class will be lost at the end of the
 * request handling
 * 
 * One use case is for tracking form errors (not yet supported, but can be added when
 * needed). After validation, errors associated with each field can be stored in this class
 * and retrieved from the template
 * 
 * @author bigpopakap
 * @since 2013-03-10
 *
 */
public abstract class RequestErrorContext extends BaseContext {
	
	private static final UniverseElement<String> FB_CONNECTION_ERROR_CONTEXT_KEY = CONTEXT_KEY_UNIVERSE.register("fbConnectionErrorContextKey");
	private static final UniverseElement<String> CAUSE_ERROR_CONTEXT_KEY = CONTEXT_KEY_UNIVERSE.register("causeErrorContextKey");
	
	/** Gets any exception thrown with this request, or null if there is none */
	public static synchronized BaseExposedException getCause() {
		return getOrLoad(CAUSE_ERROR_CONTEXT_KEY, CAUSE_CALLABLE);
	}
	
	/** Determines if the current request has an exception associated with it */
	public static synchronized boolean hasCause() {
		return getCause() != null;
	}
	
	/** Sets the exception to be associated with this request */
	public static synchronized void setCause(BaseExposedException ex) {
		set(CAUSE_ERROR_CONTEXT_KEY, ex);
	}
	
	/** Returns true if there was an error accessing Facebook for this session context */
	public static synchronized boolean fbConnectionError() {
		//any value set is considered true
		return getOrLoad(FB_CONNECTION_ERROR_CONTEXT_KEY, FB_CONNECTION_ERROR_CALLABLE);
	}
	
	/** Sets whether or not there was an FB connection error in the request context */
	public static synchronized void setFbConnectionError(boolean b) {
		set(FB_CONNECTION_ERROR_CONTEXT_KEY, b);
	}
	
	//TODO add ability to get form errors
	
	/* ***************************************************************
	 * BEGIN PRIVATE HELPERS
	 *************************************************************** */
	
	private static final Callable<Boolean> FB_CONNECTION_ERROR_CALLABLE = new Callable<Boolean>() {

		@Override
		public Boolean call() throws Exception {
			return false;
		}
		
	};
	
	private static final Callable<BaseExposedException> CAUSE_CALLABLE = new Callable<BaseExposedException>() {

		@Override
		public BaseExposedException call() throws Exception {
			return null;
		}
		
	};
	
}
