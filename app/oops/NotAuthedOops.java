package oops;

import helpers.Message;
import oops.base.BaseOops;
import oops.base.OopsPageRenderUtil;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;
import contexts.RequestContext;

/**
 * Exposed error for when the user needs to be logged in
 * 
 * @author bigpopakap
 * @since 2013-05-02
 *
 */
public class NotAuthedOops extends BaseOops {

	private static final long serialVersionUID = -7098751117037866137L;
	
	public NotAuthedOops() {
		super(null);
	}

	@Override
	protected Html hook_renderWebResult() {
		return OopsPageRenderUtil.goTo(this, RequestContext.loginUrl(), Message.errorPage_toSignIn.get(), Message.errorPage_notAuthedDescription.get());
	}

	@Override
	protected Result apiResult() {
		//TODO what should this be, actually
		return Results.badRequest();
	}

}
