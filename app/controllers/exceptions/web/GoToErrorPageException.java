package controllers.exceptions.web;

/**
 * This is the basic error page, which has displays:
 * 		- a short description of the error (optional)
 * 		- a url for the link to go (required)
 * 		- a message to be used after the link to explain where it goes (required)
 * 					Where it's placed: "Click <a>here</a> *toMessage*
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class GoToErrorPageException extends ErrorPageException {

	private static final long serialVersionUID = 1L;

	public GoToErrorPageException(Throwable cause, String url, String toMessage) {
		this(cause, url, toMessage, null);
	}

	public GoToErrorPageException(Throwable cause, String url, String toMessage, String description) {
		super(cause, views.html.errorPage.render(description, url, toMessage));
		
		//do after-the fact variable checking
		if (url == null) throw new IllegalArgumentException("Url cannot be null in error page");
		if (toMessage == null) throw new IllegalArgumentException("toMessage cannot be null in error page");
	}
	
}