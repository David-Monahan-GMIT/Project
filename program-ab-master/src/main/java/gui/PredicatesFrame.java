package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

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
import org.alicebot.ab.Predicates;
import org.slf4j.Logger;

@SuppressWarnings("serial")
public class PredicatesFrame extends JInternalFrame implements ActionListener {
	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0, height = 600, width = 400;
	Logger log;

	private JButton saveButton, exitButton;
	private JPanel buttonPanel, mainPanel;
	private Predicates predicates = new Predicates();
	private HashMap<String, JTextField> fields = new HashMap<String, JTextField>();

	/**
	 * Create a new window which displays the contents of the Predicates file
	 * within the chat bot hierarchy. This allows the user to customise the
	 * default predicates used by the bot during chat operations. These are not
	 * constants and the bot can modify these during the course of chatting,
	 * changes here only modify the default or initial state.
	 * 
	 * @param log
	 *            The main logger
	 */
	public PredicatesFrame(Logger log) {
		// Give the frame a name
		super("Predicate Configuration", false, true);
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
		predicates.getPredicateDefaults(MagicStrings.config_path + "/predicates.txt");
		log.debug(MagicStrings.config_path + "/predicates.txt");
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
				predicates.put(key, fields.get(key).getText());
			}
			predicates.setPredicateDefaults(MagicStrings.config_path + "/predicates.txt");
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
		mainPanel.setLayout(new GridLayout(predicates.size(), 2, 1, 5));
		scrolly.setViewportView(mainPanel);
		scrolly.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		scrolly.getVerticalScrollBar().setUnitIncrement(16); // Speeds up the scroll speed. Default is very slow

		for (String name : predicates.keySet()) {
			createRow(name, predicates.get(name));
		}
		add(scrolly, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Utility method to create a new row for each entry. Also adds each entry
	 * to the fields Hashmap for easy access later.
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

		JTextField field = new JTextField(value, 15);
		mainPanel.add(field);

		fields.put(name, field);
	}

}