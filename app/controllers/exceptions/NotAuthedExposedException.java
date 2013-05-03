package controllers.exceptions;

import helpers.Message;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;
import contexts.RequestContext;
import controllers.exceptions.utils.ErrorPageRenderUtil;

public class NotAuthedExposedException extends BaseExposedException {

	private static final long serialVersionUID = -7098751117037866137L;

	public NotAuthedExposedException() {
		super(null);
	}

	@Override
	protected Html hook_renderWebResult() {
		return ErrorPageRenderUtil.goTo(this, RequestContext.loginUrl(), Message.errorPage_toSignIn.get(), Message.errorPage_notAuthedDescription.get());
	}

	@Override
	protected Result apiResult() {
		//TODO what should this be, actually
		return Results.badRequest();
	}

}
