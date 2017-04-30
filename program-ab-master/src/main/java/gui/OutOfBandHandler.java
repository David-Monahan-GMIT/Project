package gui;

/**
 * David Monahan 25/04/2017 Final Year Project
 * 
 * Handler class for OOB calls made by the chatbot. The default api for OOB is tailored for
 * Android compatibility. This class just keeps a small list of some basic examples to show how 
 * this functionality can be adapted for use on any machine. Ideally this would use a config file 
 * based on the current execution environment for portability. 
 */

import java.io.IOException;
import java.util.HashMap;
import org.alicebot.ab.MagicStrings;
import org.slf4j.Logger;

public class OutOfBandHandler {
	
	private Logger log;
	private HashMap<String, String> paths = new HashMap<String, String>();
	private String response;
	private String[] args;
	private String command;

	public OutOfBandHandler(String handle, Logger log) {
		this.log = log;
		response = "";
		
		// Initialise paths to programs on the pc
		paths.put("notepad", "C:\\Program Files (x86)\\Notepad++\\notepad++.exe");
		paths.put("browser", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe" );
		paths.put("music", "C:\\Program Files (x86)\\iTunes\\iTunes.exe");
		
		// Use a regex to pull out the command in the tags
		command = handle.split(MagicStrings.commandRegex)[0];
		
		if (handle.contains("<search>")) {
			// Clean up the request
			handle = handle.replace("<search>", "");
			handle = handle.replace("</search>", "");
			handle = handle.replace("</oob>","");
			handle = handle.toLowerCase();
			log.debug(handle);
			
			args = handle.split(" ");
			if (handle.contains("browser")) {								
				try {
					response = "\nStarting: " + args[0];
					Process process = new ProcessBuilder(paths.get("browser"),"-search",args[0]).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (paths.keySet().contains(args[0])) {			
				try {
					response = "\nStarting: " + args[0];
					Process process = new ProcessBuilder(paths.get(args[0])).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else {
			response = "\nNo Out of Band available for command: " + args[0];
		}
	}
	
	public String getResponse(){
		return response;
	}

}
