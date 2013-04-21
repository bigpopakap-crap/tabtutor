package controllers.exceptions.web;

import utils.MessagesEnum;


/**
 * Error page when a CSRF validation fails
 * 
 * @author bigpopakap
 * @since 2013-04-21
 *
 */
public class CsrfTokenInvalidErrorPageException extends GoHomeErrorPageException {

	private static final long serialVersionUID = -3672421677154333902L;

	public CsrfTokenInvalidErrorPageException() {
		super(null, MessagesEnum.errorPage_csrfTokenInvalid.get());
	}
	
}