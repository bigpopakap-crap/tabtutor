package operations;

import java.util.List;

import juiforms.JuiFormValidationException;
import models.SongModel;
import models.forms.SongModelJuiForm;
import operations.base.BaseOperator;
import operations.base.OperationRequirement;

/**
 * Class that holds all operations the app can perform related to songs
 * 
 * @author bigpopakap
 * @since 2013-05-02
 *
 */
public abstract class SongsOperator extends BaseOperator {
	
	/** Get all songs */
	public static List<SongModel> getAll() {
		return SongModel.getAll();
	}
	
	/** Get a particular song */
	public static SongModel getByPk(String pk) {
		return SongModel.getByPk(pk);
	}
	
	/** Creates a new song */
	public static SongModel create(SongModelJuiForm songModelForm) throws JuiFormValidationException {
		//verify the requirements
		OperationRequirement.require(OperationRequirement.LOGGED_IN);
		
		//do the operation
		return songModelForm.bind();
	}

}
