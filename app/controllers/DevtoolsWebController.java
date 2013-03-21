package controllers;

import actions.ModeProtectAction.ModeProtected;

/**
 * This class handles routes (or parents classes that handle routes) which are used as developer tools
 * It is protected to only be available in DEVELOPMENT mode
 * 
 * @author bigpopakap
 * @since 2013-03-21
 *
 */
@ModeProtected
public class DevtoolsWebController extends BaseWebController {

}
