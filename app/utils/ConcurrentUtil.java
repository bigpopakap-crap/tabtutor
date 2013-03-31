package utils;

import globals.Globals.DevelopmentSwitch;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import play.Logger;

/**
 * Provides utility methods for executing methods in their own thread, waiting for them
 * and returning their returned values
 * 
 * @author bigpopakap
 * @since 2013-03-23
 *
 */
public abstract class ConcurrentUtil {
	
	/** Setting to false will not use threads for these method calls for debugging purposes
	 *  This value is ignored and treated as true if in production mode */
	public static final DevelopmentSwitch<Boolean> USE_THREADING = new DevelopmentSwitch<Boolean>(false);
	
	/** Default number of seconds to wait */
	private static final DevelopmentSwitch<Long> DEFAULT_THREAD_TIMEOUT_SECONDS = new DevelopmentSwitch<Long>(10L).set(20L);

	/**
	 * Executes the given callable on a separate thread, awaits its return value and returns it here
	 * 
	 * Note: be careful about referencing singletons in threaded methods. Since they are run in a separate
	 * thread, the ThreadLocal variables might not be available
	 * TODO this problem should be fixed automatically in this method here
	 * 
	 * @param callable the method to run
	 * @return the value returned by that method, which is run in its own thread
	 * @throws InterruptedException if the thread running the method is interrupted
	 * @throws TimeoutException if the thread times out
	 * @throws Exception if the callable throws an exception during its execution, the exception
	 * 					is captured and thrown here
	 */
	public static <T> T joinThread(Callable<T> callable) throws InterruptedException, TimeoutException, Exception {
		return joinThread(callable, DEFAULT_THREAD_TIMEOUT_SECONDS.get());
	}
	
	/** Same as {@link #joinThread(Callable)}, specifying a timeout in seconds */
	public static <T> T joinThread(Callable<T> callable, long timeoutSeconds) throws InterruptedException, TimeoutException, Exception {
		//bypass the creation of a Thread object if we're not threading
		if (!USE_THREADING.get()) {
			return callable.call();
		}
		
		boolean threadCompleted = false;
		long threadId = -1;
		try {
			//initialize synchronization variables
			CountDownLatch latch = new CountDownLatch(1);
			AtomicReference<Exception> atomicException = new AtomicReference<Exception>(null);
			AtomicReference<T> atomicReturned = new AtomicReference<T>(null);
			
			//start the callable in a new thread
			threadId = forkThread(new ThreadedMethodRunner<T>(latch, atomicException, atomicReturned, callable));
			Logger.trace("About to start threaded method in thread " + threadId);
			
			//wait for the thread and get its return values
			if (!latch.await(timeoutSeconds, TimeUnit.SECONDS)) {
				throw new TimeoutException("Thread timed out");
			}
			threadCompleted = true;
			Exception exception = atomicException.get();
			T returned = atomicReturned.get();
			
			//either throw the exception or
			if (exception != null) throw exception;
			else return returned;
		}
		finally {
			Logger.trace("Thread " + threadId + (threadCompleted ? " completed successfully" : " timed out"));
		}
	}
	
	/** Executes the callable in a new thread, without doing any waiting
	 *  @return the ID of the thread, or -1 if no threading is used */
	public static <T> long forkThread(Callable<T> callable) {
		//bypass the creating a runnable if we're not threading
		if (USE_THREADING.get()) {
			return forkThread(callableToRunnable(callable));
		}
		else {
			suppressCall(callable);
			return -1;
		}
	}
	
	/** Executes the runnable in a new thread, without doing any waiting
	 *  @return the ID of the thread */
	public static <T> long forkThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		
		//either start in a new thread or in current thread
		if (USE_THREADING.get()) thread.start();
		else thread.run();
		
		return thread.getId();
	}
	
	/** Converts a callable to a runnable */
	public static <T> Runnable callableToRunnable(final Callable<T> callable) {
		return new Runnable() {
			@Override
			public void run() {
				suppressCall(callable);
			}
		};
	}
	
	/** Calls a callable and converts explicit exceptions to runtime exceptions */
	public static <T> T suppressCall(Callable<T> callable) {
		try {
			return callable.call();
		}
		catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * The runnable that implements the thread that gets run to execute the
	 * threadedmethod
	 * 
	 * @author bigpopakap
	 * @since 2013-03-23
	 *
	 * @param <T> the type that the method will return
	 */
	private static class ThreadedMethodRunner<T> implements Runnable {
		
		private final CountDownLatch latch;
		private final AtomicReference<Exception> atomicException;
		private final AtomicReference<T> atomicReturned;
		private final Callable<T> callable;
		
		/**
		 * Creates a new threaded method runner
		 * @param latch the latch to countdown when the thread is complete
		 * @param atomicException ref to put any exceptions thrown while executing the method
		 * @param atomicRef ref to put the return value of the method
		 * @param callable the callable that executes the method
		 */
		private ThreadedMethodRunner(CountDownLatch latch, AtomicReference<Exception> atomicException,
									 AtomicReference<T> atomicRef, Callable<T> callable) {
			if (latch == null) throw new IllegalArgumentException("latch cannot be null");
			if (atomicException == null) throw new IllegalArgumentException("atomicException cannot be null");
			if (atomicRef == null) throw new IllegalArgumentException("atomicRef cannot be null");
			if (callable == null) throw new IllegalArgumentException("callable cannot be null");
			
			this.latch = latch;
			this.atomicException = atomicException;
			this.atomicReturned = atomicRef;
			this.callable = callable;
		}

		@Override
		public void run() {
			try {
				atomicReturned.set(callable.call());
			}
			catch (Exception ex) {
				//mark that there was an exception
				atomicException.set(ex);
			}
			finally {
				Logger.trace("Thread " + Thread.currentThread().getId() + " about to countdown on latch");
				latch.countDown();
			}
		}
		
	}

}