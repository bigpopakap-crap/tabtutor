package operations;

import java.util.List;

import juiforms.JuiFormValidationException;
import models.AlbumModel;
import models.forms.AlbumModelJuiForm;
import operations.base.BaseOperator;
import operations.base.OperationRequirement;

/**
 * Class that holds all operations the app can perform related to albums
 * 
 * @author bigpopakap
 * @since 2013-05-03
 *
 */
public abstract class AlbumsOperator extends BaseOperator {
	
	/** Get all albums */
	public static List<AlbumModel> getAll() {
		return AlbumModel.getAll();
	}
	
	/** Get a particular album */
	public static AlbumModel getByPk(String pk) {
		return AlbumModel.getByPk(pk);
	}
	
	/** Creates a new album */
	public static AlbumModel create(AlbumModelJuiForm albumModelForm) throws JuiFormValidationException {
		//verify the requirements
		OperationRequirement.require(OperationRequirement.LOGGED_IN);
		
		//do the operation
		return albumModelForm.bind();
	}

}
