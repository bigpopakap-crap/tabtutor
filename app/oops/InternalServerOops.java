package oops;

import oops.base.BaseOops;
import oops.base.OopsPageRenderUtil;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;


/**
 * Basic error page whose description is that of a INTERNAL SERVER error, and whose
 * link takes the user to the previous page
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class InternalServerOops extends BaseOops {

	private static final long serialVersionUID = 1L;

	public InternalServerOops(Throwable cause) {
		super(cause);
	}
	
	public InternalServerOops(Throwable cause, String message) {
		super(cause, message);
	}

	@Override
	protected Html hook_renderWebResult() {
		return OopsPageRenderUtil.renderGoBackPage(this, getMessage());
	}

	@Override
	protected Result apiResult() {
		return Results.internalServerError();
	}

}