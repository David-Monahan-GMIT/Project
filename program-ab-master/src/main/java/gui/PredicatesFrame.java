package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
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
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.Properties;
import org.slf4j.Logger;

public class PredicatesFrame extends JInternalFrame implements ActionListener {
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
		// xOffset = (xOffset + 30) % 500;
		// yOffset = (yOffset + 30) % 500;

		// Sets the default focus for the enter key
		getRootPane().setDefaultButton(saveButton);

		Bot.setAllPaths(MagicStrings.root_path + "/../ab/", MagicStrings.botName);
		bot.getProperties(MagicStrings.config_path + "/properties.txt");
		log.debug(MagicStrings.config_path + "/properties.txt");
		drawGui();
		
	}

	@Override
	/**
	 * Action listener to handle all 3 buttons for this frame.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == saveButton) {
			for (String key : fields.keySet()) {
				bot.put(key, fields.get(key).getText());
			}
			bot.setProperties(MagicStrings.config_path + "/properties.txt");
			this.dispose();
		}
		if (e.getSource() == exitButton) {
			this.dispose();
		}

	}

	private void drawGui() {
		
		int rowCount = bot.size();
		JScrollPane scrolly = new JScrollPane();
		JPanel dataPanel = new JPanel();
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		dataPanel.add(leftPanel, BorderLayout.WEST);
		dataPanel.add(rightPanel, BorderLayout.EAST);
		scrolly.setViewportView(dataPanel);
		scrolly.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, Color.BLUE, Color.BLACK));
		
		for(String name: bot.keySet()) {
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