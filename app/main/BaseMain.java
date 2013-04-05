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
	
	protected static final String APP_PROCFILE_NAME = "Procfile.dev";
	protected static final String TEST_PROCFILE_NAME = "Procfile.test";
	protected static final String ENV_FILE_NAME = "env.dev";
	
	/**
	 * Launches foreman on the given procfile and environment file and prints the output
	 * of that process to stdout.
	 * 
	 * Adds a hook to shutdown the child process when this process exits, and
	 * waits for the process to exit
	 * 
	 * @param procfileName name of the procfile to use, or no procfile if this is null
	 * @param envFileName name of the environment file to use, or no environment file if this is null
	 */
	protected static final void foreman(String procfileName, String envFileName) {
		BufferedReader outputReader = null;
		try {
			//TODO detect what OS this is and run the appropriate command
			//start the process
			final Process process = Runtime.getRuntime().exec("cmd.exe /c foreman start" +
															 (procfileName != null ? " -f " + procfileName : "") +
															 (envFileName != null ? " -e " + envFileName : ""));
			
			//get the reader for the child's output and pipe it
			outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String outputLine;
			while ((outputLine = outputReader.readLine()) != null) {
				System.out.println(outputLine);
			}
			
			//TODO figure out how to kill the process if/when Eclipse is killed
			//wait for the process to end
			process.waitFor();
		}
		catch (Exception ex) {
			 Logger.error("Exception caught while running foreman", ex);
		}
		finally {
			if (outputReader != null) {
				try {
					outputReader.close();
				}
				catch (Exception ex) {
					Logger.error("Exception caught while closing stream", ex);
				}
			}
			
			Logger.info("Foreman process ended");
		}
	}

}
