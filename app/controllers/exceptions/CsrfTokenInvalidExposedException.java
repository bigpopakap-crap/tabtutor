package controllers.exceptions;

import helpers.Message;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;
import controllers.exceptions.utils.ErrorPageRenderUtil;

/**
 * Error page when a CSRF validation fails
 * 
 * @author bigpopakap
 * @since 2013-04-21
 *
 */
public class CsrfTokenInvalidExposedException extends BaseExposedException {

	private static final long serialVersionUID = -3672421677154333902L;

	public CsrfTokenInvalidExposedException() {
		super(null, Message.errorPage_csrfTokenInvalidDescription.get());
	}

	@Override
	protected Html hook_renderWebResult() {
		return ErrorPageRenderUtil.goHome(this, getMessage());
	}

	@Override
	protected Result apiResult() {
		//TODO what should this be, actually
		return Results.badRequest();
	}
	
}