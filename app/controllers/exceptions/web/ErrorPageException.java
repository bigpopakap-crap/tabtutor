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
	
	private final Html page;

	public ErrorPageException(Throwable cause, Html page) {
		super(cause);
		if (page == null) throw new IllegalArgumentException("Page cannot be null");
		this.page = page;
	}
	
	@Override
	public Result result() {
		return Results.ok(page);
	}

}