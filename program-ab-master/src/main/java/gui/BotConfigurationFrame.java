package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.Properties;
import org.slf4j.Logger;

/**
 * A simple Frame to show configuration information for the Bot to be loaded.
 * The Bot to be loaded is specified in a text field by the user. The default
 * bot is already displayed. Upon loading the properties file of the bot is
 * loaded and it's fields are displayed. The user can modify these if he/she
 * wishes but they are not essential to the workings of the bot. Loading of the
 * properties file is merely to ensure that a bot with the correct directory
 * layout is being loaded.
 * 
 * @author Dave
 *
 */
public class BotConfigurationFrame extends JInternalFrame implements ActionListener {
	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0, height = 600, width = 600;
	Logger log;

	private JTextArea info;
	private JTextField inputTextField;
	private JButton saveButton, exitButton, loadButton;
	private JPanel buttonPanel, leftPanel, rightPanel;
	private Box mainBox;
	private Properties bot = new Properties();
	private HashMap<String, JTextField> fields = new HashMap<String, JTextField>();

	/**
	 * Creates a new Frame for selecting the S.U.P.E.R AIML compliant bot to
	 * use. The bot must be contained in the specified folder
	 * 
	 * @param log
	 *            The main logger
	 */
	public BotConfigurationFrame(Logger log) {
		// Give the frame a name
		super("Bot Configuration", false, true);
		this.log = log;

		mainBox = Box.createVerticalBox();
		add(mainBox, BorderLayout.NORTH);

		info = new JTextArea(
				"Set the Directory Name of the Bot to be used. All of the bots must be stored in the following directory: "
						+ System.getProperty("user.dir") + "\\..\\ab\\");
		info.setFont(new Font("Serif", Font.PLAIN, 16));
		info.setWrapStyleWord(true);
		info.setLineWrap(true);
		info.setEditable(false);
		info.setOpaque(false);
		info.setFocusable(false);
		info.setBackground(Color.LIGHT_GRAY);
		info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		Box topBox = Box.createHorizontalBox();
		mainBox.add(topBox, BorderLayout.NORTH);
		topBox.add(info);

		JLabel botDirectory = new JLabel("Bot Directory:");
		botDirectory.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		inputTextField = new JTextField();
		inputTextField.setText(MagicStrings.botName);
		inputTextField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		loadButton = new JButton("Load Bot");
		saveButton = new JButton("Save");
		exitButton = new JButton("Exit");
		loadButton.addActionListener(this);
		loadButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		Box botBox = Box.createHorizontalBox();
		mainBox.add(botBox, BorderLayout.NORTH);
		botBox.add(botDirectory);
		botBox.add(inputTextField);
		botBox.add(loadButton);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(saveButton, new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(exitButton, new FlowLayout(FlowLayout.RIGHT));
		add(buttonPanel, BorderLayout.SOUTH);

		saveButton.addActionListener(this);
		exitButton.addActionListener(this);

		setBounds(xOffset, yOffset, width, height);
		// xOffset = (xOffset + 30) % 500;
		// yOffset = (yOffset + 30) % 500;

		// Sets the default focus for the enter key
		getRootPane().setDefaultButton(loadButton);

	}

	@Override
	/**
	 * Action listener to handle all buttons for this frame.
	 */
	public void actionPerformed(ActionEvent e) {

		// Load the data based on the selected bot.
		if (e.getSource() == loadButton) {
			if (inputTextField.getText() != null && inputTextField.getText() != "") {
				MagicStrings.botName = inputTextField.getText();
			}
			Bot.setAllPaths(MagicStrings.root_path + "/../ab/", MagicStrings.botName);
			bot.getProperties(MagicStrings.config_path + "/properties.txt");
			log.debug(MagicStrings.config_path + "/properties.txt");
			drawGui();
		}

		// Save the current data fields and close the window
		if (e.getSource() == saveButton) {
			for (String key : fields.keySet()) {
				bot.put(key, fields.get(key).getText());
			}
			bot.setProperties(MagicStrings.config_path + "/properties.txt");
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

		int rowCount = bot.size();
		JScrollPane scrolly = new JScrollPane();
		JPanel dataPanel = new JPanel();
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(rowCount, 1, 0, 7));
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		dataPanel.add(leftPanel, BorderLayout.WEST);
		dataPanel.add(rightPanel, BorderLayout.EAST);
		scrolly.setViewportView(dataPanel);
		scrolly.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.BLUE, Color.BLACK));

		for (String name : bot.keySet()) {
			createRow(name, bot.get(name));
		}

		// clear the window and redraw
		getContentPane().removeAll();
		add(mainBox, BorderLayout.NORTH);
		add(scrolly, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		revalidate();
		repaint();
	}

	private void createRow(String name, String value) {
		JLabel label = new JLabel(name, SwingConstants.RIGHT);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftPanel.add(label);

		JTextField field = new JTextField(30);
		field.setText(value);
		rightPanel.add(field);

		fields.put(name, field);
	}

}
