package gui;

/**
 * David Monahan 25/04/2017 Final Year Project
 * 
 * OOb calls are made by framing the request in xml tags and passing them back in the chat response. 
 * Implementation of this handle is to filter out those xml tags and try to offer functionality for 
 * some of the most common ones. 
 */

import java.io.IOException;
import org.alicebot.ab.MagicStrings;
import org.slf4j.Logger;

/**
 * This class is a simple handler to parse responses which contain an \<oob\> tag.
 * The Bot returns an xml response for Out of Band calls and this handler is
 * designed to parse those commands and use a text file listing paths to various
 * programs on the current machine in order to try and handle those requests.
 * 
 * New paths can be added by editing the paths.txt file located in the
 *\<botname\>/config/ folder
 * 
 * @author Dave
 *
 */
public class OutOfBandHandler {

	private String response;
	private String[] args;
	private String command;
	private Paths paths = new Paths();

	public OutOfBandHandler(String handle, Logger log) {
		response = "";

		paths.getPathDefaults(MagicStrings.config_path + "/paths.txt");

		// Use a regex to pull out the command in the tags
		command = handle.split(MagicStrings.commandRegex)[2];

		if (handle.contains("<search>")) {
			// Clean up the request
			handle = handle.replace("<search>", "");
			handle = handle.replace("</search>", "");
			handle = handle.replace("</oob>", "");
			log.debug(handle);

			args = handle.split(" ");
			if (handle.contains("browser")) {
				try {
					handle = handle.replace("i'm opening your browser.", "");
					response = "\nStarting: " + " Web Browser";
					new ProcessBuilder(paths.get("browser"), "-search", handle).start();
					log.debug(paths.get("browser") + "-search" + handle);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (paths.keySet().contains(args[0])) {
				try {
					response = "\nStarting: " + args[0];
					new ProcessBuilder(paths.get(args[0])).start();
					log.debug(paths.get(args[0]));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				response = "\nNo Out of Band available for command: " + command;
			}

		} else {
			response = "\nNo Out of Band available for command: " + command;
		}
	}

	/**
	 * Utility method to offer a response based on the passed command
	 * 
	 * @return Customised response based on the command executed
	 */
	public String getResponse() {
		return response;
	}

}
