package contexts;

import java.util.concurrent.Callable;

import utils.DbTypesUtil;
import utils.ObjectUtils;

/**
 * Class for gathering stats about the request
 * 
 * @author bigpopakap
 * @since 2013-03-31
 *
 */
public class RequestStatsContext extends BaseContext {
	
	private static final ContextKey REQUEST_STATS_CONTEXT_KEY = ContextKey.register("requestStatsContextKey");
	
	private boolean isComplete;
	private long startTime;
	private long endTime;
	private int numModelOperationFailures;
	private int numModelOperationRetries;
	
	private RequestStatsContext() {
		this.isComplete = false;
		this.startTime = DbTypesUtil.nowMillis();
		this.endTime = 0;
		this.numModelOperationFailures = 0;
		this.numModelOperationRetries = 0;
	}
	
	/* **************************************************************************
	 * PUBLIC OVERRIDES
	 ************************************************************************** */
	
	@Override
	public String toString() {
		return ObjectUtils.getFieldsToString(this);
	}
	
	/* **************************************************************************
	 *  BEGIN GETTERS
	 ************************************************************************** */
	
	/** Determines if the request is basically done processing */
	public boolean isComplete() {
		return isComplete;
	}
	
	/** Gets the duration to handle the request in seconds */
	public double getDurationSeconds() {
		return ((double) (endTime - startTime)) / 1000.0;
	}
	
	/** Gets the number of failed database/model operations */
	public int getNumModelOperationFailures() {
		return numModelOperationFailures;
	}
	
	/** Gets the number of database/model operation retries */
	public int getNumModelOperationRetries() {
		return numModelOperationRetries;
	}
	
	/* **************************************************************************
	 *  BEGIN MODIFIERS
	 ************************************************************************** */
	
	/** Marks the request completed */
	public void setCompleted() {
		isComplete = true;
		endTime = DbTypesUtil.nowMillis();
	}
	
	/** Add one to the number of database/model operation failures */
	public void incrModelOperationFailures() {
		numModelOperationFailures++; 
	}
	
	/** Add one to the number of database/model operation retries */
	public void incrModelOperationRetries() {
		numModelOperationRetries++;
	}
	
	/* **************************************************************************
	 *  BEGIN STATIC ACESSORS
	 ************************************************************************** */
	
	public static synchronized RequestStatsContext get() {
		return getOrLoad(REQUEST_STATS_CONTEXT_KEY, REQUEST_STATS_CALLABLE);
	}
	
	/* **************************************************************************
	 *  BEGIN PRIVATE HELPERS
	 ************************************************************************** */
	
	private static final Callable<RequestStatsContext> REQUEST_STATS_CALLABLE = new Callable<RequestStatsContext>() {

		@Override
		public RequestStatsContext call() throws Exception {
			return new RequestStatsContext();
		}
		
	};

}
