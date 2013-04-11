package controllers;

import play.api.templates.Html;
import play.mvc.Result;
import utils.MessagesEnum;
import actions.ActionAnnotations.AccessTimed;
import actions.ActionAnnotations.Sessioned;
import actions.ActionAnnotations.Transactioned;
import actions.ActionAnnotations.TriedCaught;
import controllers.exceptions.BaseExposedException;

/**
 * This class should be the parent of all classes that handle requests for the web interface
 * of this app
 *
 * @author bigpopakap
 * @since 2013-02-17
 *
 */
@TriedCaught @Transactioned @AccessTimed @Sessioned //methods should not forget to include these
public class BaseWebController extends BaseController {
	
	@Override
	public BaseExposedException getDefaultExposedException(Throwable cause) {
		return new InternalServerErrorPageException(cause);
	}
	
	/**
	 * This class is an exposed error specific to the web interface, where every
	 * result should be an HTML page that will be rendered and sent back to the user
	 * 
	 * @author bigpopakap
	 * @since 2013-03-06
	 *
	 */
	protected static abstract class ErrorPageException extends controllers.exceptions.BaseExposedException {
		
		private static final long serialVersionUID = -8551848133552601737L;
		
		private final Html page;

		public ErrorPageException(Throwable cause, Html page) {
			super(cause);
			if (page == null) throw new IllegalArgumentException("Page cannot be null");
			this.page = page;
		}
		
		@Override
		public Result result() {
			return ok(page);
		}

	}
	
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
	protected static class GoToErrorPageException extends ErrorPageException {

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
	
	/**
	 * Basic error page whose link takes the user to the previous page
	 * 
	 * @author bigpopakap
	 * @since 2013-03-27
	 *
	 */
	protected static class GoBackErrorPageException extends GoToErrorPageException {
		
		private static final long serialVersionUID = 1L;

		public GoBackErrorPageException(Throwable cause) {
			this(cause, null);
		}

		public GoBackErrorPageException(Throwable cause, String description) {
			super(cause, "javascript:history.back()", MessagesEnum.errorPage_toGoBack.get(), description);
		}

	}
	
	/**
	 * Basic error page whose description is that of a NOT FOUND error, and whose
	 * link takes the user to the previous page
	 * 
	 * @author bigpopakap
	 * @since 2013-03-27
	 *
	 */
	protected static class NotFoundErrorPageException extends GoBackErrorPageException {

		private static final long serialVersionUID = 1L;

		public NotFoundErrorPageException(Throwable cause) {
			super(cause, MessagesEnum.errorPage_pageNotFoundDescription.get());
		}

	}
	
	/**
	 * Basic error page whose description is that of a INTERNAL SERVER error, and whose
	 * link takes the user to the previous page
	 * 
	 * @author bigpopakap
	 * @since 2013-03-27
	 *
	 */
	protected static class InternalServerErrorPageException extends GoBackErrorPageException {

		private static final long serialVersionUID = 1L;

		public InternalServerErrorPageException(Throwable cause) {
			super(cause, MessagesEnum.errorPage_internalServerErrorDescription.get());
		}

	}
	
}
