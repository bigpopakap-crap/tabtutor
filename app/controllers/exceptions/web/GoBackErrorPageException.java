package controllers.exceptions.web;

import utils.MessagesEnum;

/**
 * Basic error page whose link takes the user to the previous page
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class GoBackErrorPageException extends GoToErrorPageException {
	
	private static final long serialVersionUID = 1L;

	public GoBackErrorPageException(Throwable cause) {
		this(cause, null);
	}

	public GoBackErrorPageException(Throwable cause, String description) {
		super(cause, "javascript:history.back()", MessagesEnum.errorPage_toGoBack.get(), description);
	}

}