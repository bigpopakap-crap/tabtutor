package controllers.exceptions.web;

import utils.MessagesEnum;

/**
 * Basic error page whose description is that of a NOT FOUND error, and whose
 * link takes the user to the previous page
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class NotFoundErrorPageException extends GoBackErrorPageException {

	private static final long serialVersionUID = 1L;

	public NotFoundErrorPageException(Throwable cause) {
		super(cause, MessagesEnum.errorPage_pageNotFoundDescription.get());
	}

}