package controllers.exceptions.web;

import helpers.Message;

/**
 * Basic error page with a link to go home
 * 
 * @author bigpopakap
 * @since 2013-04-21
 *
 */
public class GoHomeErrorPageException extends GoToErrorPageException {

	private static final long serialVersionUID = 911075521154891655L;

	public GoHomeErrorPageException(Throwable cause) {
		this(cause, null);
	}

	public GoHomeErrorPageException(Throwable cause, String description) {
		super(cause, "/", Message.errorPage_toGoHome.get(), description);
	}
	
}