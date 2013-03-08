package controllers;

import exeptions.BaseExposedException;
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
	
	/**
	 * This class is an exposed error specific to the API interface, where every
	 * result should be an API result describing the error
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-03-06
	 *
	 */
	protected static abstract class ErrorResponseException extends BaseExposedException {

		private static final long serialVersionUID = 6385621008097532663L;
		
	}

}
