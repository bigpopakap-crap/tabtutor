package actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;

public class ModeProtectAction extends Action.Simple {
	
	/**
	 * Annotation for applying ModeProtectAction
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-03-06
	 *
	 */
	@With(ModeProtectAction.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ModeProtected {}

	/** Implements the action */
	@Override
	public Result call(Context ctx) throws Throwable {
		// TODO Take a mode parameter
		// TODO Take default ErrorPageException parameter
		return null;
	}

}
