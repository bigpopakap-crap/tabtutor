package controllers.exceptions.web;
import play.api.templates.Html;
import play.mvc.Result;
import play.mvc.Results;

/**
 * This class is an exposed error specific to the web interface, where every
 * result should be an HTML page that will be rendered and sent back to the user
 * 
 * @author bigpopakap
 * @since 2013-03-06
 *
 */
public abstract class ErrorPageException extends controllers.exceptions.BaseExposedException {
	
	private static final long serialVersionUID = -8551848133552601737L;
	
	public ErrorPageException(Throwable cause) {
		super(cause);
	}
	
	protected abstract Html hook_render();
	
	@Override
	public Result result() {
		Html page = hook_render();
		if (page == null) {
			throw new IllegalStateException("Page cannot be null");
		}
		return Results.ok(page);
	}

}