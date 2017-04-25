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
import java.io.InputStream;

public class OutOfBoundsHandler {

	OutOfBoundsHandler(String handle) {

		if (handle.contains("Notepad") || handle.contains("notepad")) {
			try {
				Process process = new ProcessBuilder("C:\\Program Files (x86)\\Notepad++\\notepad++.exe").start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (handle.contains("search")) {
			try {
				handle = handle.replace("<search>", "");
				handle = handle.replace("</search>", "");
				handle = handle.replace("</oob>","");
				Process process = new ProcessBuilder("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe","-search",handle).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
