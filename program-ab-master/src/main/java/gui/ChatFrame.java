package gui;

/**
 * David Monahan 19/04/2017
 * Final Year Project
 * Frame to allow a user to interact with a Chat Bot
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import org.alicebot.ab.AB;
import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Category;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;
import org.alicebot.ab.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatFrame extends JInternalFrame implements ActionListener {

	private JPanel mainPanel;

	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0;

	private int rowCount = 2;
	private int colCount = 3;

	Bot bot;
	Chat chatSession;
	Logger log;
	
    private JTextArea textArea;
    private JTextField inputTextField;
    private JButton sendButton;

	public ChatFrame(String botName, Boolean traceMode, String action, Logger log) {
		// Give the frame a name
		super("Chat Session", true, true);
		
		// Setup the parameters needed for the bot to run.
		bot = new Bot(botName, MagicStrings.root_path + "/../ab/", action);
		chatSession = new Chat(bot);
		this.log = log;
		bot.brain.nodeStats();
		MagicBooleans.trace_mode = traceMode;

		// Create a gui frame
/*		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(rowCount, colCount, 5, 5));
		chatHistory = new JTextArea(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		queryBox = new JTextArea();
		JLabel user = new JLabel("User:");
		JLabel robot = new JLabel("Bot:");
	//	JButton sendQuery = new JButton("SendQuery");
		sendQuery.addActionListener(this);
		JLabel spacer = new JLabel("");
		
		mainPanel.add(robot);
		mainPanel.add(chatHistory);
		mainPanel.add(spacer);
		mainPanel.add(user);
		mainPanel.add(queryBox);
		mainPanel.add(sendQuery);*/

        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        Box box = Box.createHorizontalBox();
        add(box, BorderLayout.SOUTH);
        inputTextField = new JTextField();
        sendButton = new JButton("Send");
        box.add(inputTextField);
        box.add(sendButton);
        
        inputTextField.addActionListener(this);
        sendButton.addActionListener(this);
		

		//Container container = getContentPane();
		//container.add(mainPanel, BorderLayout.CENTER);

		setBounds(xOffset, yOffset, 500, 500);
		xOffset = (xOffset + 30) % 500;
		yOffset = (yOffset + 30) % 500;


	}

	@Override
	/**
	 * 
	 */
	public synchronized void actionPerformed(ActionEvent e) {
		textArea.append("User: " + inputTextField.getText() + "\r\n");		
		textArea.append("Bot: " + chat(inputTextField.getText()) + "\r\n");
		inputTextField.setText("");

	}

	/**
	 * Utilty method for chatting to the specified bot.
	 * 
	 * @param query
	 *            The User message to the bot
	 * @return The response from the bot to the query, returns "Invalid Query"
	 *         if a bad query is passed.
	 */
	private synchronized String chat(String query) {

		String textLine = query;
		// System.out.print("Human: ");
		// textLine = IOUtils.readInputTextLine();
		if (textLine == null || textLine.length() < 1)
			textLine = MagicStrings.null_input;
		if (textLine.equals("q"))
			System.exit(0);
		else if (textLine.equals("wq")) {
			bot.writeQuit();
			System.exit(0);
		} else {
			String request = textLine;
			log.debug("STATE=" + request + ":THAT=" + chatSession.thatHistory.get(0).get(0) + ":TOPIC="
					+ chatSession.predicates.get("topic"));
			String response = chatSession.multisentenceRespond(request);
			while (response.contains("&lt;"))
				response = response.replace("&lt;", "<");
			while (response.contains("&gt;"))
				response = response.replace("&gt;", ">");
			log.info("Robot: " + response);

			return response;
		}
		return "Invalid Query";
	}

}
