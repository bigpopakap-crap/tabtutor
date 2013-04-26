package interfaces;

import play.api.templates.Html;

/**
 * Interface that marks objects that have a way to display themselves
 * in HTML
 * 
 * @author bigpopakap
 * @since 2013-04-26
 *
 */
public interface Renderable {

	/** Returns some HTML that can be used to render this object */
	public Html render();
	
}
