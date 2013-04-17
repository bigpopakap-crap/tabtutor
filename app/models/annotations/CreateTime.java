package models.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

import models.BaseModel;

/**
 * To mark a model's field as the one that holds the created time
 * This will be updated in {@link BaseModel}
 * 
 * This should only be associated with {@link Date} fields
 * 
 * @author bigpopakap
 * @since 2013-04-16
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateTime {

}
