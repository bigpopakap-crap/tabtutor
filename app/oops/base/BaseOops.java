package oops.base;

import play.Logger;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;
import contexts.RequestContext;

/**
 * This is the base exception class for exeptions that should be surfaced to the
 * client as an error response of some sort. These will be handled at the very
 * top level controller
 * 
 * Throwing this exception should interrupt the processing of the current request,
 * and the result() method will generate the result that should be returned
 * to the client
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public abstract class BaseOops extends RuntimeException {

	private static final long serialVersionUID = -935805780499802623L;
	
	/** The message specified by the exception that should be surfaced with the expression */
	private final String uiMessage;
	
	//do *NOT* support a no-argument constructor in order to force classes to specify a cause
	
	/** Only define this constructor so all subclasses are forced to specify the cause */
	public BaseOops(Throwable cause) {
		super(cause);
		this.uiMessage = null; //don't populate since no message is explicitly specified
	}
	
	/** Only define this constructor so all subclasses are forced to specify the cause */
	public BaseOops(Throwable cause, String uiMessage) {
		super(uiMessage, cause);
		this.uiMessage = uiMessage;
	}
	
	/** Gets the {@link #uiMessage} */
	protected String getUiMessage() {
		return uiMessage;
	}
	
	/** Gets the result that should be returned to the client */
	public final Result result() {
		if (RequestContext.isApi()) {
			return apiResult();
		}
		else if (RequestContext.isWeb()) {
			return webResult();
		}
		else {
			//log an error and default to web result
			Logger.error("Unhandled case when returning exposed exception result!");
			return webResult();
		}
	}
	
	/** Returns the result representing this exception that should be sent to the client
	 *  if they are using the web interface. This should be an HTML page */
	protected final Result webResult() {
		Html page = hook_renderWebResult();
		if (page == null) {
			throw new IllegalStateException("Page cannot be null");
		}
		return Results.ok(page);
	}
	
	/** Renders the error page associated with this exposed exception */
	protected abstract Html hook_renderWebResult();
	
	/** Returns the result representing this exception that should be sent to the client
	 *  if they are using the API interface */
	protected abstract Result apiResult();

}
