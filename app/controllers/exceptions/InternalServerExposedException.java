package controllers.exceptions;

import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;
import controllers.exceptions.utils.ErrorPageRenderUtil;


/**
 * Basic error page whose description is that of a INTERNAL SERVER error, and whose
 * link takes the user to the previous page
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class InternalServerExposedException extends BaseExposedException {

	private static final long serialVersionUID = 1L;

	public InternalServerExposedException(Throwable cause) {
		super(cause);
	}
	
	public InternalServerExposedException(Throwable cause, String message) {
		super(cause, message);
	}

	@Override
	protected Html hook_renderWebResult() {
		return ErrorPageRenderUtil.goBack(this, getMessage());
	}

	@Override
	protected Result apiResult() {
		return Results.internalServerError();
	}

}