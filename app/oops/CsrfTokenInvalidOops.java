package oops;

import helpers.Message;
import oops.base.BaseOops;
import oops.base.OopsPageRenderUtil;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;

/**
 * Error page when a CSRF validation fails
 * 
 * @author bigpopakap
 * @since 2013-04-21
 *
 */
public class CsrfTokenInvalidOops extends BaseOops {

	private static final long serialVersionUID = -3672421677154333902L;

	public CsrfTokenInvalidOops() {
		super(null, Message.errorPage_csrfTokenInvalidDescription.get());
	}

	@Override
	protected Html hook_renderWebResult() {
		return OopsPageRenderUtil.renderGoHomePage(this, getUiMessage());
	}

	@Override
	protected Result apiResult() {
		//TODO what should this be, actually
		return Results.badRequest();
	}
	
}