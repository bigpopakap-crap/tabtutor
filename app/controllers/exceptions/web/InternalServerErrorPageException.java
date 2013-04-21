package controllers.exceptions.web;

import utils.MessagesEnum;

/**
 * Basic error page whose description is that of a INTERNAL SERVER error, and whose
 * link takes the user to the previous page
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class InternalServerErrorPageException extends GoBackErrorPageException {

	private static final long serialVersionUID = 1L;

	public InternalServerErrorPageException(Throwable cause) {
		super(cause, MessagesEnum.errorPage_internalServerErrorDescription.get());
	}

}