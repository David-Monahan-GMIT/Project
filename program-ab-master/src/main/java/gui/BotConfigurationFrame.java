package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.Properties;
import org.slf4j.Logger;

public class BotConfigurationFrame extends JInternalFrame implements ActionListener {
	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0, height = 700, width = 600;
	Logger log;
	
	private JTextArea info;
	private JTextField inputTextField;
	private JButton saveButton, exitButton, loadButton;
	Box mainBox;
	Properties bot = new Properties();

	public BotConfigurationFrame(Logger log) {
		// Give the frame a name
		super("Bot Configuration", false, true);
		this.log = log;

		mainBox = Box.createVerticalBox();
		add(mainBox, BorderLayout.NORTH);

		info = new JTextArea(
				"Set the Directory of the Bot to be used. "
				+ "All of the bots must stored in the following directory: " + System.getProperty("user.dir") + "\\..\\ab\\");
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
		loadButton.addActionListener(this);
		loadButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		Box botBox = Box.createHorizontalBox();
		mainBox.add(botBox, BorderLayout.NORTH);
		botBox.add(botDirectory);
		botBox.add(inputTextField);
		botBox.add(loadButton);

		setBounds(xOffset, yOffset, width, height);
		// xOffset = (xOffset + 30) % 500;
		// yOffset = (yOffset + 30) % 500;

		// Sets the default focus for the enter key
		getRootPane().setDefaultButton(saveButton);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == loadButton) {
			if (inputTextField.getText() != null && inputTextField.getText() != "") {
				MagicStrings.botName = inputTextField.getText();
			}
			Bot.setAllPaths (MagicStrings.bot_path, MagicStrings.botName);
			bot.getProperties(MagicStrings.config_path+"/properties.txt");
			drawGui();
		}

	}
	
	private void drawGui() {
		
	}

}
