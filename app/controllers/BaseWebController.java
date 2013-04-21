package controllers;

import actions.ActionAnnotations.AccessTimed;
import actions.ActionAnnotations.Sessioned;
import actions.ActionAnnotations.TriedCaught;

/**
 * This class should be the parent of all classes that handle requests for the web interface
 * of this app
 *
 * @author bigpopakap
 * @since 2013-02-17
 *
 */
@TriedCaught @AccessTimed @Sessioned //methods should not forget to include these
public class BaseWebController extends BaseController {
	
}