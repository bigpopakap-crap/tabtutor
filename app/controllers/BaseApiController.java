package controllers;

import play.mvc.Controller;

/**
 * This class should be the parent of all classes that route API calls. Api calls should always
 * return either JSON or XML.
 * 
 * API calls should accept at least the following parameters:
 * 		apiv - the version of the API they are using
 * 		apif - the format the result should be returned (json or xml)
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-17
 *
 */
public class BaseApiController extends Controller {
	
	//add exposed API paths when we want to start exposing an API

}
