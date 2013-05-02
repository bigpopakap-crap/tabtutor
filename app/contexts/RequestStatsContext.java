package contexts;

import helpers.Universe.UniverseElement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import contexts.base.BaseContext;

import utils.DateUtil;

/**
 * Class for gathering stats about the request
 * 
 * @author bigpopakap
 * @since 2013-03-31
 *
 */
public class RequestStatsContext extends BaseContext {
	
	private static final UniverseElement<String> REQUEST_STATS_CONTEXT_KEY = CONTEXT_KEY_UNIVERSE.register("requestStatsContextKey");
	
	private boolean isComplete;
	private long startTime;
	private long endTime;
	private int numModelOperationFailures;
	private int numModelOperationRetries;
	
	private RequestStatsContext() {
		this.isComplete = false;
		this.startTime = DateUtil.nowMillis();
		this.endTime = 0;
		this.numModelOperationFailures = 0;
		this.numModelOperationRetries = 0;
	}
	
	/* **************************************************************************
	 * PUBLIC OVERRIDES
	 ************************************************************************** */
	
	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<>();
		map.put("isComplete", isComplete());
		if (isComplete()) map.put("duration", getDurationSeconds() + "s");
		map.put("numDbFailures", getNumModelOperationFailures());
		map.put("numDbRetries", getNumModelOperationRetries());
		
		return this.getClass().getCanonicalName() + ":" + map;
	}
	
	/* **************************************************************************
	 *  BEGIN GETTERS
	 ************************************************************************** */
	
	/** Determines if the request is basically done processing */
	public synchronized boolean isComplete() {
		return isComplete;
	}
	
	/** Gets the duration to handle the request in seconds */
	public synchronized double getDurationSeconds() {
		return ((double) (endTime - startTime)) / 1000.0;
	}
	
	/** Gets the number of failed database/model operations */
	public synchronized int getNumModelOperationFailures() {
		return numModelOperationFailures;
	}
	
	/** Gets the number of database/model operation retries */
	public synchronized int getNumModelOperationRetries() {
		return numModelOperationRetries;
	}
	
	/* **************************************************************************
	 *  BEGIN MODIFIERS
	 ************************************************************************** */
	
	/** Marks the request completed */
	public synchronized void setCompleted() {
		isComplete = true;
		endTime = DateUtil.nowMillis();
	}
	
	/** Add one to the number of database/model operation failures */
	public synchronized void incrModelOperationFailures() {
		numModelOperationFailures++; 
	}
	
	/** Add one to the number of database/model operation retries */
	public synchronized void incrModelOperationRetries() {
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
