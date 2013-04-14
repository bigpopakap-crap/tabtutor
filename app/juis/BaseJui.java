package juis;

import utils.ObjectUtil;

/**
 * The base class for all JUI (Java UI) classes. These represent the bridge
 * between the rendered HTML and data associated with it
 * 
 * @author bigpopakap
 * @since 2013-04-14
 *
 */
public abstract class BaseJui {
	
	/** Default toString that returns the field=value mappings */
	@Override
	public String toString() {
		return this.getClass().getCanonicalName() + ":" + ObjectUtil.getFieldMap(this);
	}

}
