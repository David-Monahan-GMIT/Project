package gui;

/**
 * David Monahan 19/04/2017
 * Final Year Project
 * Frame to allow a user to interact with a Chat Bot
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.net.URL;
import java.awt.BorderLayout;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.slf4j.Logger;

@SuppressWarnings("serial")
public class ChatFrame extends JInternalFrame implements ActionListener {

	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0;

	Bot bot;
	Chat chatSession;
	Logger log;

	private JTextArea textArea;
	private JTextField inputTextField;
	private JButton sendButton;
	private JDesktopPane desktop;

	/**
	 * Creates a new Chat Frame within the Desktop to allow the user to chat
	 * with the Bot. This is effectively the main window of the application.
	 * 
	 * @param botName
	 *            The name of the Bot to be run. This refers to the name of the
	 *            root Folder of the Bot.
	 * @param traceMode
	 *            Enable or disable Verbose output from the bot. For Debugging.
	 *            Only really affects the console output.
	 * @param action
	 *            The core Bot class has numerous Action states depending on
	 *            what you want it to do. Mostly this will be Chat.
	 * @param desktop
	 *            The Desktop being used to display this window on. Used to draw
	 *            subframes for things like images.
	 * @param log
	 *            The Main Logger.
	 */
	public ChatFrame(String botName, Boolean traceMode, String action, JDesktopPane desktop, Logger log) {
		// Give the frame a name
		super("Chat Session", true, true);

		// Setup the parameters needed for the bot to run.
		bot = new Bot(botName, MagicStrings.root_path + "/../ab/", action);
		chatSession = new Chat(bot);
		this.desktop = desktop;
		this.log = log;
		bot.brain.nodeStats();
		MagicBooleans.trace_mode = traceMode;

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		add(new JScrollPane(textArea), BorderLayout.CENTER);

		Box box = Box.createHorizontalBox();
		add(box, BorderLayout.SOUTH);
		inputTextField = new JTextField();
		sendButton = new JButton("Send");
		box.add(inputTextField);
		box.add(sendButton);

		// Removed due to redundancy adds an actionlistener to the textArea that
		// triggers when the enter button is pressed
		// inputTextField.addActionListener(this);

		sendButton.addActionListener(this);

		setBounds(xOffset, yOffset, 600, 600);
		// xOffset = (xOffset + 30) % 500;
		// yOffset = (yOffset + 30) % 500;

		// Sets the default focus for the enter key
		getRootPane().setDefaultButton(sendButton);

	}

	/**
	 * Utility method to allow me to call the text area to request focus after
	 * the JPanel has been drawn and displayed
	 */
	public void setTextAreaFocus() {
		inputTextField.requestFocus();
	}

	@Override
	/**
	 * Action Listener for handling chat requests. Triggers whenever the send
	 * button is pressed. Uses the current chatSession to assign user and Bot
	 * names for the chat frame.
	 * 
	 */
	public synchronized void actionPerformed(ActionEvent e) {
		// Use the current user name. Defaults are specified by predicates
		textArea.append(chatSession.predicates.get("name") + ": " + inputTextField.getText() + "\r\n");
		// Use the current Bot name. Defaults are specified by predicates
		textArea.append(chatSession.predicates.get("customname") + ": " + chat(inputTextField.getText()) + "\r\n");
		// Sets the carat position in the text area so that it also scrolls when
		// new text is added
		textArea.setCaretPosition(textArea.getText() != null ? textArea.getText().length() : 0);
		inputTextField.setText(""); // clear the input field
		// ensures the input focus snaps back to the textArea ie after an image
		// is displayed
		inputTextField.requestFocus();
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
		if (textLine == null || textLine.length() < 1)
			textLine = MagicStrings.null_input;
		if (textLine.equals("q"))
			System.exit(0);
		// Used for experimental aiml generator
		else if (textLine.equals("wq")) {
			bot.writeQuit();
			System.exit(0);
		} else {
			String request = textLine;
			log.debug("STATE=" + request + ":THAT=" + chatSession.thatHistory.get(0).get(0) + ":TOPIC="
					+ chatSession.predicates.get("topic"));
			String response = chatSession.multisentenceRespond(request);

			response = parseResponse(response);
			log.info("Robot: " + response);
			return response;
		}
		return "Invalid Query";
	}

	/**
	 * Method for cleaning up the response from the chat bot. This will also
	 * filter out and attempt to interpret responses from the bot for additional
	 * features, such as image urls and out of bounds calls.
	 * 
	 * 
	 * @param response
	 *            The chatbot response that needs to be parsed
	 * @return A cleaned up version of the chatbot response
	 */
	private synchronized String parseResponse(String response) {
		while (response.contains("&lt;"))
			response = response.replace("&lt;", "<");
		while (response.contains("&gt;"))
			response = response.replace("&gt;", ">");
		if (response.contains("<oob>")) {
			response = response.toLowerCase();
			log.debug("OOB Handler call using: " + response.split(MagicStrings.oobRegex)[1]);
			OutOfBandHandler handle = new OutOfBandHandler(response.split(MagicStrings.oobRegex)[1], log);
			return response.split(MagicStrings.oobRegex)[0] + handle.getResponse();
		}
		if (response.contains("<a href=")) {
			try {
				String url = response.split(MagicStrings.imageRegex)[1];
				url = url.replaceAll("\"\\><.*", "");
				log.debug(url);
				BufferedImage img = ImageIO.read(new URL(url));
				JLabel label = new JLabel(new ImageIcon(img));
				JInternalFrame f = new JInternalFrame("Image", false, true);
				f.getContentPane().add(label);
				f.pack();
				f.setLocation(610, yOffset + 10);
				desktop.add(f);
				f.setVisible(true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			return (response.split(MagicStrings.imageRegex)[0]);
		} else if (response.contains("<img src=\"")) {
			try {
				String url = response.substring(response.indexOf("<img src=\""), (response.length() - 3));
				url = url.replace("<img src=\"", "");
				log.debug(url);
				BufferedImage img = ImageIO.read(new URL(url));
				JLabel label = new JLabel(new ImageIcon(img));
				JInternalFrame f = new JInternalFrame("Image", false, true);
				f.getContentPane().add(label);
				f.pack();
				f.setLocation(610, yOffset + 10);
				desktop.add(f);
				f.setVisible(true);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			return (response.substring(0, response.indexOf("<img src=\"")));
		}
		return response;
	}

}
