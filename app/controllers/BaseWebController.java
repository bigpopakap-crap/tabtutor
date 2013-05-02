package controllers;

import actions.annotations.ActionAnnotations.AccessTimed;
import actions.annotations.ActionAnnotations.Sessioned;
import actions.annotations.ActionAnnotations.Transactioned;
import actions.annotations.ActionAnnotations.TriedCaught;

/**
 * This class should be the parent of all classes that handle requests for the web interface
 * of this app
 *
 * @author bigpopakap
 * @since 2013-02-17
 *
 */
@TriedCaught @Transactioned @AccessTimed @Sessioned //methods should not forget to include these
public class BaseWebController extends BaseController {
	
}
