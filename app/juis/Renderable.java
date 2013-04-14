package juis;

import play.api.templates.Html;

/**
 * Interface for any object that can render HTML
 * 
 * @author bigpopakap
 * @since 2013-04-13
 *
 */
public interface Renderable {
	
	/** Generate the HTML to display this object */
	public Html render();

}
