package oops;

import helpers.Message;
import oops.base.BaseOops;
import oops.base.OopsPageRenderUtil;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;

public class InsufficientPrivilegesOops extends BaseOops {

	private static final long serialVersionUID = -4281589322363049604L;

	public InsufficientPrivilegesOops(Throwable cause) {
		super(cause, Message.errorPage_insufficientPrivilegesDescription.get());
	}

	@Override
	protected Html hook_renderWebResult() {
		return OopsPageRenderUtil.renderGoBackPage(this, getMessage());
	}

	@Override
	protected Result apiResult() {
		//TODO what should this be, actually
		return Results.badRequest();
	}

}
