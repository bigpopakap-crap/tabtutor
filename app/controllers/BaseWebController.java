package controllers;

import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Result;
import actions.ErrorCatchAction.ErrorCaught;
import actions.SessionAction.Sessioned;
import exeptions.BaseExposedException;

/**
 * This class should be the parent of all classes that handle requests for the web interface
 * of this app
 *
 * @author bigpopakap@gmail.com
 * @since 2013-02-17
 *
 */
@ErrorCaught //top level action to catch all unhandled exceptions
@Sessioned //this is important to make sure all web requests enforce the creation of a session
public class BaseWebController extends Controller {
	
	/**
	 * This class is an exposed error specific to the web interface, where every
	 * result should be an HTML page that will be rendered and sent back to the user
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-03-06
	 *
	 */
	protected static abstract class ErrorPageException extends BaseExposedException {

		private static final long serialVersionUID = -8551848133552601737L;
		
		/** Provides static methods for creating instances of this class */
		public static class Factory {
			
			//TODO append the stack trace to the page except in production mode
			//TODO add ability to specify page title
			
			public static ErrorPageException notFoundPage() {
				//TODO use translation for this
				return goBackPage("We couldn't find the page you were looking for");
			}
			
			/** Returns an error page with a link to go back to the previous page */
			public static ErrorPageException goBackPage() {
				return goBackPage(null);
			}
			
			/** Returns an error page with the given short description of the error a link to go back to the previous page */
			public static ErrorPageException goBackPage(String description) {
				//TODO use translation for this
				return goToPage(description, "javascript:history.back()", "to go back");
			}
			
			/** 
			 * Returns an error page with a link
			 * 
			 * @param url the location for the link to go
			 * @param toMessage the message to be used after the link to explain where it goes
			 * 					"Click <a>here</a> *toMessage*
			 */
			public static ErrorPageException goToPage(String url, String toMessage) {
				return goToPage(null, url, toMessage);
			}
			
			/** 
			 * Returns an error page with a short description of the error a link
			 * 
			 * @param description the description of the error. This can be null
			 * @param url the location for the link to go. Cannot be null
			 * @param toMessage the message to be used after the link to explain where it goes
			 * 					"Click <a>here</a> *toMessage*
			 * 					This cannot be null
			 * @throws IllegalArgumentException if either the url of the toMessage is null
			 */
			public static ErrorPageException goToPage(String description, String url, String toMessage) {
				//TODO style the page
				if (url == null) throw new IllegalArgumentException("Url cannot be null in error page");
				if (toMessage == null) throw new IllegalArgumentException("toMessage cannot be null in error page");
				return custom(views.html.errorPage.render(description, url, toMessage));
			}
			
			/** Returns a custom error page that will return the given HTML */
			public static ErrorPageException custom(final Html page) {
				return new ErrorPageException() {

					private static final long serialVersionUID = -3927537673541009374L;

					@Override
					public Result result() {
						return ok(page);
					}
					
				};
			}
			
		} //end Factory class
		
	} //end ErrorPageException class
	
}