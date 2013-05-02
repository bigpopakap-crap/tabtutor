package models.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

import models.base.BaseModel;

/**
 * To mark a model's field as the one that holds the updated time
 * This will be updated in {@link BaseModel}
 * 
 * Note that this doesn't cover some cases, like updating the time
 * when the user logs in, because those cases don't need this column
 * to be modified on *ever* single update operation
 * 
 * This should only be associated with {@link Date} fields
 * 
 * @author bigpopakap
 * @since 2013-04-16
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateTime {

	/** If true, does not get updated unless the row actually changes.
	 *  Otherwise will get updated only when the row actually changes */
	public boolean ignoreNoOp() default true;
	
}