package gui;
/*
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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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

/**
 * New version of main to incorporate a GUI
 * 
 * @author Dave
 *
 */

public class Main extends JFrame {
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private JDesktopPane desktop;
	Action newChatSession, exitAction, highScoreAction, helpScreenAction;

	public Main() {
		super("Chat Bot panel");

		/**
		 * Code added to change the look and feel of the application. Nimbus is
		 * the selected GUI. Find out more here:
		 * http://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/nimbus.html
		 */
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		// Create GUI
		JToolBar toolBar = new JToolBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		newChatSession = new NewChatSession();
		highScoreAction = new HighScoresAction();
		helpScreenAction = new HelpScreenAction();
		exitAction = new ExitAction();

		toolBar.add(newChatSession);
		toolBar.add(highScoreAction);
		toolBar.add(helpScreenAction);

		fileMenu.add(newChatSession);
		fileMenu.add(highScoreAction);
		fileMenu.add(helpScreenAction);
		fileMenu.add(exitAction);

		// set up menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);

		// set up desktop
		desktop = new JDesktopPane();

		// get the content pane to set up GUI
		Container c = getContentPane();
		c.add(toolBar, BorderLayout.NORTH);
		c.add(desktop, BorderLayout.CENTER);

		// register for windowClosing event in case user
		// does not select Exit from File menu to terminate
		// application
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				shutDown();
			}
		});

		// set window size and display window
		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();

		// center window on screen
		setBounds(100, 100, dimension.width - 200, dimension.height - 100);

		setVisible(true);

	} // end constructor

	// close database connection and terminate program
	private void shutDown() {
		System.exit(0); // terminate program
	}

	public static void main(String[] args) {
		MagicStrings.root_path = System.getProperty("user.dir");
		log.info("Working Directory = " + MagicStrings.root_path + "/../ab/");
		AIMLProcessor.extension = new PCAIMLProcessorExtension();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		});

	}

	private class NewChatSession extends AbstractAction {

		/**
		 * Adds a Serialisable id, mostly to shut up the compiler
		 */
		private static final long serialVersionUID = 1L;

		// set up action's name, icon, descriptions and mnemonic
		public NewChatSession() {
			putValue(NAME, "New");
			// putValue(SMALL_ICON, new
			// ImageIcon(getClass().getResource("images/New24.png")));
			putValue(SHORT_DESCRIPTION, "New");
			putValue(LONG_DESCRIPTION, "Start a Conversation with a Bot");
			putValue(MNEMONIC_KEY, new Integer('N'));
		}

		// display window in which user can input entry
		public void actionPerformed(ActionEvent e) {
			ChatFrame frame = new ChatFrame("super", MagicBooleans.trace_mode, "chat", log);
			desktop.add(frame);
			frame.setVisible(true);
		}

	} // end inner class NewAction

	private class HighScoresAction extends AbstractAction {

		// set up action's name, icon, descriptions and mnemonic
		public HighScoresAction() {
			putValue(NAME, "High Scores");
			// putValue(SMALL_ICON, new
			// ImageIcon(getClass().getResource("images/New24.png")));
			putValue(SHORT_DESCRIPTION, "High Scores");
			putValue(LONG_DESCRIPTION, "Show all the High Scores");
			putValue(MNEMONIC_KEY, new Integer('H'));
		}

		// display window in which user can input entry
		public void actionPerformed(ActionEvent e) {
			// create new internal window
		}

	} // end inner class NewAction

	private class HelpScreenAction extends AbstractAction {

		// set up action's name, icon, descriptions and mnemonic
		public HelpScreenAction() {
			putValue(NAME, "Help");
			// putValue(SMALL_ICON, new
			// ImageIcon(getClass().getResource("images/New24.png")));
			putValue(SHORT_DESCRIPTION, "Help");
			putValue(LONG_DESCRIPTION, "Show how to play the game");
			putValue(MNEMONIC_KEY, new Integer('H'));
		}

		// display window in which user can input entry
		public void actionPerformed(ActionEvent e) {
			// create new internal window
			// HelpFrame frame = new HelpFrame();
			// desktop.add(frame);
			// frame.setVisible(true);
		}

	} // end inner class NewAction

	// inner class defines action that closes connection to
	// database and terminates program
	private class ExitAction extends AbstractAction {

		// set up action's name, descriptions and mnemonic
		public ExitAction() {
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "Exit");
			putValue(LONG_DESCRIPTION, "Terminate the program");
			putValue(MNEMONIC_KEY, new Integer('x'));
		}

		// terminate program
		public void actionPerformed(ActionEvent e) {
			shutDown(); // close database connection and terminate
		}

	} // end inner class ExitAction
}

/*
 * public class Main { private static final Logger log =
 * LoggerFactory.getLogger(Main.class); public static void main (String[] args)
 * { MagicStrings.root_path = System.getProperty("user.dir");
 * log.info("Working Directory = " + MagicStrings.root_path + "/../ab/");
 * AIMLProcessor.extension = new PCAIMLProcessorExtension(); mainFunction(args);
 * } public static void mainFunction (String[] args) { String botName = "Bob";
 * String action = "chat"; log.info(MagicStrings.programNameVersion); for
 * (String s : args) { log.info(s); String[] splitArg = s.split("="); if
 * (splitArg.length >= 2) { String option = splitArg[0]; String value =
 * splitArg[1]; if (option.equals("bot")) botName = value; if
 * (option.equals("action")) action = value; if (option.equals("trace") &&
 * value.equals("true")) MagicBooleans.trace_mode = true; else
 * MagicBooleans.trace_mode = false; } }
 * log.info("trace mode = "+MagicBooleans.trace_mode);
 * Graphmaster.enableShortCuts = true; Timer timer = new Timer(); Bot bot = new
 * Bot(botName, MagicStrings.root_path+"/../ab/", action); //
 * //bot.preProcessor.normalizeFile("c:/ab/log1.txt",
 * "c:/ab/data/lognormal.txt"); if (bot.brain.getCategories().size() < 100)
 * bot.brain.printgraph(); if (action.equals("chat")) testChat(bot,
 * MagicBooleans.trace_mode); else if (action.equals("test")) testSuite(bot,
 * MagicStrings.root_path+"/data/find.txt"); else if (action.equals("ab"))
 * testAB(bot); else if (action.equals("aiml2csv") || action.equals("csv2aiml"))
 * convert(bot, action); else if (action.equals("abwq")) AB.abwq(bot); } public
 * static void convert(Bot bot, String action) { if (action.equals("aiml2csv"))
 * bot.writeAIMLIFFiles(); else if (action.equals("csv2aiml"))
 * bot.writeAIMLFiles(); } public static void testAB (Bot bot) {
 * MagicBooleans.trace_mode = true; AB.ab(bot); AB.terminalInteraction(bot) ; }
 * public static void testShortCuts () { testAB(new Bot("alice"));
 * Graphmaster.enableShortCuts = false; Bot bot = new Bot("alice");
 * bot.brain.printgraph(); bot.brain.nodeStats(); Graphmaster.enableShortCuts =
 * true; bot = new Bot("alice"); bot.brain.printgraph(); bot.brain.nodeStats();
 * } public static void testChat (Bot bot, boolean traceMode) { Chat chatSession
 * = new Chat(bot); //
 * bot.preProcessor.normalizeFile("c:/ab/bots/super/aiml/thats.txt",
 * "c:/ab/bots/super/aiml/normalthats.txt"); bot.brain.nodeStats();
 * MagicBooleans.trace_mode = traceMode; String textLine=""; while (true) {
 * System.out.print("Human: "); textLine = IOUtils.readInputTextLine(); if
 * (textLine == null || textLine.length() < 1) textLine =
 * MagicStrings.null_input; if (textLine.equals("q")) System.exit(0); else if
 * (textLine.equals("wq")) { bot.writeQuit(); System.exit(0); } else if
 * (textLine.equals("ab")) testAB(bot); else { String request = textLine;
 * log.debug("STATE="+request+":THAT="+chatSession.thatHistory.get(0).get(0)+
 * ":TOPIC="+chatSession.predicates.get("topic")); String response =
 * chatSession.multisentenceRespond(request); while (response.contains("&lt;"))
 * response = response.replace("&lt;","<"); while (response.contains("&gt;"))
 * response = response.replace("&gt;",">"); log.info("Robot: "+response);
 * 
 * }
 * 
 * } } public static void testBotChat () { Bot bot = new Bot("alice");
 * log.info(bot.brain.upgradeCnt+" brain upgrades"); bot.brain.nodeStats();
 * //bot.brain.printgraph(); Chat chatSession = new Chat(bot); String request =
 * "Hello.  How are you?  What is your name?  Tell me about yourself."; String
 * response = chatSession.multisentenceRespond(request);
 * log.info("Human: "+request); log.info("Robot: "+response); } public static
 * void testSuite (Bot bot, String filename) { try{ AB.passed.readAIMLSet(bot);
 * AB.testSet.readAIMLSet(bot);
 * log.info("Passed "+AB.passed.size()+" samples."); String textLine=""; Chat
 * chatSession = new Chat(bot); FileInputStream fstream = new
 * FileInputStream(filename); // Get the object BufferedReader br = new
 * BufferedReader(new InputStreamReader(fstream)); String strLine; //Read File
 * Line By Line int count = 0; HashSet<String> samples = new HashSet<String>();
 * while ((strLine = br.readLine())!= null) { samples.add(strLine); }
 * ArrayList<String> sampleArray = new ArrayList<String>(samples);
 * Collections.sort(sampleArray); for (String request : sampleArray) { if
 * (request.startsWith("Human: ")) request =
 * request.substring("Human: ".length(), request.length()); Category c = new
 * Category(0, bot.preProcessor.normalize(request), "*", "*",
 * MagicStrings.blank_template, MagicStrings.null_aiml_file); if
 * (AB.passed.contains(request)) log.info("--> Already passed "+request); else
 * if (!bot.deletedGraph.existsCategory(c) && !AB.passed.contains(request)) {
 * String response = chatSession.multisentenceRespond(request);
 * log.info(count+". Human: "+request); log.info(count+". Robot: "+response);
 * textLine = IOUtils.readInputTextLine(); AB.terminalInteractionStep(bot,
 * request, textLine, c); count += 1; } } //Close the input stream br.close(); }
 * catch (Exception e){//Catch exception if any log.error("testSuite Error: " +
 * e, e); } }
 * 
 * }
 */