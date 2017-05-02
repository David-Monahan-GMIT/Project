package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicStrings;
import org.slf4j.Logger;


/**
 * Utility frame to allow the user to change the paths associated with the
 * current Bot and set them to values relevant for their PC. New paths can be
 * added manually by editing the paths.txt file in the Bot's <botname>/config
 * folder. 
 * 
 * @author Dave
 *
 */
public class PathsFrame extends JInternalFrame implements ActionListener {
	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0, height = 600, width = 600;
	Logger log;

	private JButton saveButton, exitButton;
	private JPanel buttonPanel, mainPanel;
	private Paths paths = new Paths();
	private HashMap<String, JTextField> fields = new HashMap<String, JTextField>();

	/**
	 * Create a new window which displays the contents of the Paths file within
	 * the chat bot hierarchy. This allows the user to customise the default
	 * paths used by the bot during chat operations. These are used by the bot
	 * to be able to launch applications.
	 * 
	 * @param log
	 *            The main logger
	 */
	public PathsFrame(Logger log) {
		// Give the frame a name
		super("Path Configuration", false, true);
		this.log = log;

		saveButton = new JButton("Save");
		exitButton = new JButton("Exit");

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(saveButton, new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(exitButton, new FlowLayout(FlowLayout.RIGHT));
		add(buttonPanel, BorderLayout.SOUTH);

		saveButton.addActionListener(this);
		exitButton.addActionListener(this);

		setBounds(xOffset, yOffset, width, height);

		// Sets the default focus for the enter key
		getRootPane().setDefaultButton(saveButton);

		Bot.setAllPaths(MagicStrings.root_path + "/../ab/", MagicStrings.botName);
		paths.getPathDefaults(MagicStrings.config_path + "/paths.txt");
		log.debug(MagicStrings.config_path + "/paths.txt");
		drawGui();

	}

	@Override
	/**
	 * Action listener to handle all buttons for this frame.
	 */
	public void actionPerformed(ActionEvent e) {

		// Save all the data in the fields to file and close the window
		if (e.getSource() == saveButton) {
			for (String key : fields.keySet()) {
				paths.put(key, fields.get(key).getText());
			}
			paths.setPathDefaults(MagicStrings.config_path + "/paths.txt");
			this.dispose();
		}
		// Close the window without saving
		if (e.getSource() == exitButton) {
			this.dispose();
		}

	}

	/**
	 * Creates the GUI. Checks the size of the Hashmap to see how many rows need
	 * to be created then generates each a row for each entry. Each row is on a
	 * JPanel contained within a JScrollPane to allow for large amounts of data
	 * to be displayed easily.
	 */
	private void drawGui() {

		JScrollPane scrolly = new JScrollPane();
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(paths.size(), 50, 0, 5));
		scrolly.setViewportView(mainPanel);
		scrolly.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		// Speeds up the scroll speed. Default is very slow
		scrolly.getVerticalScrollBar().setUnitIncrement(16);
		// Create a sorted set with just the keys so as to display them
		// alphabetically
		SortedSet<String> keys = new TreeSet<String>(paths.keySet());
		for (String name : keys) {
			createRow(name, paths.get(name));
		}
		add(scrolly, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Utility method to create a new row for each entry. Also adds each entry
	 * to the fields Hashmap in order to match up keys with their textField.
	 * 
	 * @param name
	 *            The name to be used for the lhs JLabel
	 * @param value
	 *            The default value to be displayed in the JTextField
	 */
	private void createRow(String name, String value) {

		JLabel label = new JLabel(name, SwingConstants.LEFT);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(label);

		JTextField field = new JTextField(value, 5);
		mainPanel.add(field);

		fields.put(name, field);
	}
}
