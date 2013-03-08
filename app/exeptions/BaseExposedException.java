package exeptions;

import play.mvc.Result;

/**
 * This is the base exception class for exeptions that should be surfaced to the
 * client as an error response of some sort. These will be handled at the very
 * top level controller
 * 
 * Throwing this exception should interrupt the processing of the current request,
 * and the result() method will generate the result that should be returned
 * to the client
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-06
 *
 */
public abstract class BaseExposedException extends RuntimeException {

	private static final long serialVersionUID = -935805780499802623L;
	
	/** Returns the result representing this exception that should be sent to the client */
	public abstract Result result();
	
	/** This class has static methods to create default instances of this class */
	public static class Factory {
		
		/** Returns an exposed exception that simply responds with a NOT FOUND error with no body */
		public static BaseExposedException notFound() {
			return notFound(null);
		}
		
		/** Returns an exposed exception that simply responds with a NOT FOUND error with the given message */
		public static BaseExposedException notFound(final String msg) {
			return new BaseExposedException() {

				private static final long serialVersionUID = 3981117505185668591L;

				@Override
				public Result result() {
					return msg != null
							? play.mvc.Results.notFound(msg)
							: play.mvc.Results.notFound();
				}
				
			};
		}
		
		/** Returns an exposed exception that simply responds with an INTERNAL SERVER ERROR with no body */
		public static BaseExposedException internalServerError() {
			return internalServerError(null);
		}
		
		/** Returns an exposed exception that simply responds with an INTERNAL SERVER ERROR with the given message */
		public static BaseExposedException internalServerError(final String msg) {
			return new BaseExposedException() {

				private static final long serialVersionUID = 7329710051663165079L;

				@Override
				public Result result() {
					return msg != null
							? play.mvc.Results.internalServerError(msg)
							: play.mvc.Results.internalServerError();
				}
				
			};
		}
		
	}

}
