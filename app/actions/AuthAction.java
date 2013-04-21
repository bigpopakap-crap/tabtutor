package actions;

import java.lang.annotation.Annotation;

import models.SessionModel;
import play.mvc.Http.Context;
import play.mvc.Result;
import utils.Logger;
import actions.ActionAnnotations.Authed;
import contexts.SessionContext;
import controllers.FbLoginWebController;

/**
 * This Action will log the user in through Facebook, and ensure that the authentication
 * token has not expired. It will also create a user object if necessary
 * 
 * It is assumed that the session has already been set up
 * 
 * @author bigpopakap
 * @since 2013-02-24
 *
 */
public class AuthAction extends BaseAction<Authed> {
	
	@Override
	protected Result hook_call(Context ctx) throws Throwable {
		//get the session object
		final SessionModel session = SessionContext.session();
		if (session == null) throw new IllegalStateException("Session should have been populated by now");
		
		//if it is a real user and we need to force re-auth or the auth info is invalid, redirect to fb login
		//TODO add ability to force re-authentication: need to worry about getting caught in infinite loop
		if (!session.hasUser()) {
			Logger.debug("Session needs Facebook auth. Redirecting to the login flow");
			return FbLoginWebController.fblogin(null, null, ctx.request().path());
		}
		//TODO find a way to force a permission access level
		else {
			return delegate.call(ctx);
		}
	}

	@Override
	protected Authed createConfiguration() {
		return new Authed() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return this.getClass();
			}
			
		};
	}

}
