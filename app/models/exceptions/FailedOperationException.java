package models.exceptions;

import models.base.BaseModel;
import types.SqlOperationType;

/**
 * Runtime exception for failed database operations
 * 
 * @author bigpopakap
 * @since 2013-03-20
 *
 */
public class FailedOperationException extends RuntimeException {

	private static final long serialVersionUID = 2430088339171735726L;
	
	private SqlOperationType opType;
	private BaseModel model;
	
	public FailedOperationException(BaseModel model, SqlOperationType opType, Throwable cause) {
		super(cause);
	}
	
	public SqlOperationType getOperationType() {
		return opType;
	}
	
	public BaseModel getModel() {
		return model;
	}

}
