package oops;

import oops.base.BaseOops;
import oops.base.OopsPageRenderUtil;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;

/**
 * Basic error page whose description is that of a NOT FOUND error, and whose
 * link takes the user to the previous page
 * 
 * @author bigpopakap
 * @since 2013-03-27
 *
 */
public class NotFoundOops extends BaseOops {

	private static final long serialVersionUID = 1L;

	public NotFoundOops(Throwable cause) {
		super(cause);
	}

	@Override
	protected Html hook_renderWebResult() {
		return OopsPageRenderUtil.renderNotFoundPage(this);
	}

	@Override
	protected Result apiResult() {
		return Results.notFound();
	}

}