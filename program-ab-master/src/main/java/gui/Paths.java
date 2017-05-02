package gui;

/**
 * David Monahan 01/05/2017
 * Utility Hashmap for loading the program paths
 * This is pretty much a copy paste of the modified predicates class
 */
/* Program AB Reference AIML 2.0 implementation
        Copyright (C) 2013 ALICE A.I. Foundation
        Contact: info@alicebot.org

        This library is free software; you can redistribute it and/or
        modify it under the terms of the GNU Library General Public
        License as published by the Free Software Foundation; either
        version 2 of the License, or (at your option) any later version.

        This library is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
        Library General Public License for more details.

        You should have received a copy of the GNU Library General Public
        License along with this library; if not, write to the
        Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
        Boston, MA  02110-1301, USA.
*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A functional copy of the Predicates and Properties classes used in AB.jar.
 * The is a utility class that functions as a Hashmap of all the paths currently
 * defined in the Bots paths.txt file. Includes utility methods for getting and
 * setting paths as well as loading and saving values from the paths.txt file.
 *
 * @author Dave
 */
public class Paths extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(Paths.class);

	/**
	 * save a path value
	 *
	 * @param key
	 *            path name
	 * @param value
	 *            path value
	 * @return path value
	 */
	@Override
	public String put(String key, String value) {
		if (MagicBooleans.trace_mode)
			log.info("Setting path {} to {}", key, value);
		return super.put(key, value);
	}

	/**
	 * get a path value
	 *
	 * @param key
	 *            path name
	 * @return path value
	 */
	public String get(String key) {
		String result = super.get(key);
		if (result == null)
			return MagicStrings.unknown_path_value;
		else
			return result;
	}

	/**
	 * Read path default values from an input stream
	 *
	 * @param in
	 *            input stream
	 */
	public void getPathDefaultsFromInputStream(InputStream in) {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		try {
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				if (strLine.contains(":")) {
					String property = strLine.substring(0, strLine.indexOf(":"));
					String value = strLine.substring(strLine.indexOf(":") + 1);
					put(property, value);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * read path defaults from a file
	 *
	 * @param filename
	 *            name of file
	 */
	public void getPathDefaults(String filename) {
		try {
			// Open the file that is the first
			// command line parameter
			File file = new File(filename);
			if (file.exists()) {
				FileInputStream fstream = new FileInputStream(filename);
				// Get the object
				getPathDefaultsFromInputStream(fstream);
				fstream.close();
			}
		} catch (Exception e) {// Catch exception if any
			log.error("Cannot get path defaults from '" + filename + "': " + e, e);
		}
	}

	/**
	 * Save the current paths to the selected file
	 * 
	 * @param filename
	 *            the file to save to
	 */
	public void setPathDefaults(String filename) {
		log.info("Set Properties: " + filename);
		try {
			// Open the file that is the first
			// command line parameter
			File file = new File(filename);
			if (file.exists()) {
				log.info("Exists: " + filename);
				FileOutputStream fstream = new FileOutputStream(filename);
				// Get the object
				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(fstream));
				// Read File Line By Line
				for (String key : this.keySet()) {
					wr.write(key + ":" + this.get(key));
					wr.newLine();
					log.info(key + ":" + this.get(key));
				}
				// Close the output stream
				wr.close();
			}
		} catch (Exception e) {// Catch exception if any
			log.error("Cannot get properties from '" + filename + "': " + e, e);
		}

	}
}
