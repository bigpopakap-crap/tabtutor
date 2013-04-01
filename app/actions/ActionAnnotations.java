package actions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;
import contexts.AppContext;

/**
 * The class holds all action annotation definitions
 * 
 * @author bigpopakap
 * @since 2013-03-22
 *
 */
public abstract class ActionAnnotations {
	
	/**
	 * Annotation for applying ErrorCatchAction
	 * 
	 * @author bigpopakap
	 * @since 2013-03-06
	 *
	 */
	@With(TryCatchAction.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface TriedCaught {}
	
	/**
	 * Annotation for applying ModeProtectAction
	 * 
	 * @author bigpopakap
	 * @since 2013-03-06
	 *
	 */
	@With(ModeProtectAction.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ModeProtected {
		AppContext.Mode allowedMode() default AppContext.Mode.DEVELOPMENT;
	}
	
	/**
	 * Annotation for applying SessionAction
	 * 
	 * @param forceRefresh forces a refresh of the session token
	 * 
	 * @author bigpopakap
	 * @since 2013-02-24
	 *
	 */
	@With(SessionAction.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Sessioned {
		boolean forceRefresh() default false;
	}
	
	/**
	 * Annotation for applying AuthAction
	 * 
	 * @author bigpopakap
	 * @since 2013-02-24
	 *
	 */
	@With(AuthAction.class)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Authed {}

}
