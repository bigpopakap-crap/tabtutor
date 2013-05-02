package controllers.exceptions;

import helpers.Message;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;
import controllers.exceptions.utils.ErrorPageRenderUtil;

/**
 * Basic error page whose description is that of a NOT FOUND error, and whose
 * link takes the user to the previous page
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class NotFoundExposedException extends BaseExposedException {

	private static final long serialVersionUID = 1L;

	public NotFoundExposedException(Throwable cause) {
		super(cause, Message.errorPage_pageNotFoundDescription.get());
	}

	@Override
	protected Html hook_renderWebResult() {
		return ErrorPageRenderUtil.goBack(this, getMessage());
	}

	@Override
	protected Result apiResult() {
		return Results.notFound();
	}

}