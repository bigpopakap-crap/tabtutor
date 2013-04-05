package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import play.Logger;

/**
 * Provides helper methods for the main classes
 * 
 * @author bigpopakap
 * @since 2013-04-04
 *
 */
public abstract class BaseMain {
	
	/**
	 * Launches foreman on the given procfile and environment file and prints the output
	 * of that process to stdout. Adds a hook to shutdown the child process when this process exits
	 * 
	 * @param procfileName name of the procfile to use, or no procfile if this is null
	 * @param envFileName name of the environment file to use, or no environment file if this is null
	 */
	protected static final void foreman(String procfileName, String envFileName) throws Exception {
		//TODO detect what OS this is and run the appropriate command
		//start the process
		final Process process = Runtime.getRuntime().exec("cmd.exe /c foreman start" +
													(procfileName != null ? " -f " + procfileName : "") +
													(envFileName != null ? " -e " + envFileName : ""));
		
		//pipe the output
		final BufferedReader inReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = inReader.readLine()) != null) {
			System.out.println(line);
		}
		
		//kill the child process on JVM shutdown
		Runnable destroyHook = new Runnable() {
			@Override
			public void run() {
				//kill the process
				process.destroy();
				
				//close the reader
				if (inReader != null) {
					try {
						inReader.close();
					}
					catch (Exception ex) {
						Logger.error("Error closing stream", ex);
					}
				}
			}
		};
		Runtime.getRuntime().addShutdownHook(new Thread(destroyHook));
	}

}
