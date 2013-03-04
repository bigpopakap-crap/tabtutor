package launch;

import models.BaseModel;
import base.BaseFuncTest;

/**
 * Test class for making sure that database models match the database schema
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-02-26
 *
 */
public class SchemaTest extends BaseFuncTest {
	
	/** Helper for testing that a database table matches the model */
	public void doTestModelMatchesTable(Class<? extends BaseModel> model) {
		//TODO make sure the various Ebean modules match the database tables
	}

}
