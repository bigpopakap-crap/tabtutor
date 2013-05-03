package oops.base;

import helpers.Message;
import play.api.templates.Html;

/**
 * A class that has utilities to render various error pages
 * 
 * @author bigpopakap
 * @since 2013-05-02
 *
 */
public abstract class OopsPageRenderUtil {
	
	/**
	 * This is the basic error page the provides a link to go to some other URL
	 * @param ex the exception that is the source of this error
	 * @param description a short description of the error (optional)
	 * @param url a url for the link to go (required)
	 * @param toMessage a message to be used after the link to explain where it goes (required)
	 * 					Where it's placed: "Click <a>here</a> *toMessage*
	 */
	public static Html goTo(Throwable ex, String url, String toMessage, String description) {
		if (url == null) throw new IllegalArgumentException("Url cannot be null in error page");
		if (toMessage == null) throw new IllegalArgumentException("toMessage cannot be null in error page");
		return views.html.errorPage.render(description, url, toMessage, ex);
	}
	
	/** A page to go back to the previous page */
	public static Html goBack(Throwable ex, String description) {
		return goTo(ex, "javascript:history.back()", Message.errorPage_toGoBack.get(), description);
	}
	
	/** A page with a link to go back home */
	public static Html goHome(Throwable ex, String description) {
		return goTo(ex, "/", Message.errorPage_toGoHome.get(), description);
	}

}
