package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.alicebot.ab.MagicStrings;
import org.slf4j.Logger;

public class BotConfigurationFrame extends JInternalFrame implements ActionListener {
	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0, height = 700, width = 600;
	Logger log;
	
	private JTextArea info;
	private JTextField inputTextField;
	private JButton saveButton, exitButton, loadButton;

	public BotConfigurationFrame(Logger log) {
		// Give the frame a name
		super("Bot Configuration", false, true);
		this.log = log;

		Box mainBox = Box.createVerticalBox();
		add(mainBox, BorderLayout.NORTH);

		info = new JTextArea(
				"Set the name of the Bot to be used here. Note this is not the name of the bot when "
				+ "chatting but the name of the folder where all of the bots aiml, csv and configuration files are stored. "
				+ "All of the bots must stored in the following directory: " + System.getProperty("user.dir") + "\\..\\ab\\");
		info.setFont(new Font("Serif", Font.PLAIN, 16));
		info.setWrapStyleWord(true);
		info.setLineWrap(true);
		info.setEditable(false);
		info.setOpaque(false);
		info.setFocusable(false);
	    info.setBackground(Color.LIGHT_GRAY);
	    info.setBorder(UIManager.getBorder("Label.border"));
		Box topBox = Box.createHorizontalBox();
		mainBox.add(topBox, BorderLayout.NORTH);
		topBox.add(info);
		
		JLabel botName = new JLabel("Bot Name:");
		inputTextField = new JTextField();
		inputTextField.setText(MagicStrings.botName);

		loadButton = new JButton("Load Bot");
		loadButton.addActionListener(this);
		Box botBox = Box.createHorizontalBox();
		mainBox.add(botBox, BorderLayout.NORTH);
		botBox.add(botName);
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

	}

}
