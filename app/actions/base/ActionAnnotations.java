package actions.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;
import actions.AccessTimeAction;
import actions.AuthAction;
import actions.DevModeProtectAction;
import actions.SessionAction;
import actions.TransactionAction;
import actions.TryCatchAction;

/**
 * The class holds all action annotation definitions
 * 
 * IMPORTANT! Do not break up the classes in this file into their own files.
 * Keeping them together like this helps to show the order in which they must be applied to requests
 * 
 * @author bigpopakap
 * @since 2013-03-22
 *
 */
public abstract class ActionAnnotations {
	
	/**
	 * Annotation for applying {@link TryCatchAction}
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
	 * Annotation for applying TransactionAction
	 * 
	 * @author bigpopakap
	 * @since 2013-04-04
	 *
	 */
	@With({
		TryCatchAction.class,
		TransactionAction.class
	})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Transactioned {}

	/**
	 * Annotation for applying {@link AccessTimeAction}
	 * 
	 * @author bigpopakap
	 * @since 2013-03-06
	 *
	 */
	@With({
		TryCatchAction.class,
		TransactionAction.class,
		AccessTimeAction.class
	})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface AccessTimed {}
	
	/**
	 * Annotation for applying {@link DevModeProtectAction}
	 * 
	 * @author bigpopakap
	 * @since 2013-03-06
	 *
	 */
	@With({
		TryCatchAction.class,
		TransactionAction.class,
		AccessTimeAction.class,
		DevModeProtectAction.class
	})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface DevModeProtected {}
	
	/**
	 * Annotation for applying {@link SessionAction}
	 * 
	 * @param forceRefresh forces a refresh of the session token
	 * 
	 * @author bigpopakap
	 * @since 2013-02-24
	 *
	 */
	@With({
		TryCatchAction.class,
		TransactionAction.class,
		AccessTimeAction.class,
		SessionAction.class
	})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Sessioned {}
	
	/**
	 * Annotation for applying {@link AuthAction}
	 * 
	 * @author bigpopakap
	 * @since 2013-02-24
	 *
	 */
	@With({
		TryCatchAction.class,
		TransactionAction.class,
		AccessTimeAction.class,
		SessionAction.class,
		AuthAction.class
	})
	@Target({ElementType.TYPE, ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Authed {}

}
