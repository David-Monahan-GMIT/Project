package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to setup the Desktop frame, Actions and configure base paths for
 * the Bot. This is the primary GUI and as such controls all other GUI elements.
 * 
 * This Program is a fork of Program AB and uses the Ab.jar as the
 * primary reference library. Consequentially the following license applies:
 * 
 * Program AB Reference AIML 2.0 implementation Copyright (C) 2013 ALICE A.I.
 * Foundation Contact: info@alicebot.org
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 * @author Dave
 *
 */

@SuppressWarnings("serial")
public class Main extends JFrame {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private JDesktopPane desktop;
	Action newChatSession, botConfigurationAction, exitAction, predicatesAction, helpScreenAction, pathsAction;

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

		newChatSession = new NewChatSession();
		botConfigurationAction = new BotConfigurationAction();
		predicatesAction = new PredicatesAction();
		pathsAction = new PathsAction();
		helpScreenAction = new HelpScreenAction();
		exitAction = new ExitAction();

		toolBar.add(newChatSession);
		toolBar.add(botConfigurationAction);
		toolBar.add(predicatesAction);
		toolBar.add(pathsAction);
		toolBar.add(helpScreenAction);
		toolBar.add(exitAction);
		toolBar.setFloatable(false);

		// set up menu bar
		JMenuBar menuBar = new JMenuBar();
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
			@Override
			public void windowClosing(WindowEvent event) {
				shutDown();
			}
		});

		// set window size and display window
		Toolkit toolkit = getToolkit();
		Dimension dimension = toolkit.getScreenSize();

		// center window on screen
		setBounds(50, 10, dimension.width - 100, dimension.height - 100);

		setVisible(true);

	} // end constructor

	// close database connection and terminate program
	private void shutDown() {
		System.exit(0); // terminate program
	}

	public static void main(String[] args) {
		// Set the root path to the Project directory
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

		// set up action's name, icon, descriptions and mnemonic
		public NewChatSession() {
			putValue(NAME, "Chat!");
			putValue(LARGE_ICON_KEY, new ImageIcon(MagicStrings.root_path + "/../img/chat.png"));
			log.debug(MagicStrings.root_path + "/../img/chat.png");
			putValue(SHORT_DESCRIPTION, "Chat!");
			putValue(LONG_DESCRIPTION, "Start a Conversation with a Bot");
			putValue(MNEMONIC_KEY, new Integer('N'));
		}

		// display window in which user can input entry
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatFrame frame = new ChatFrame(MagicStrings.botName, MagicBooleans.trace_mode, "chat", desktop, log);
			desktop.add(frame);
			frame.setVisible(true);
			// Set the input focus to the text field after drawing the panel
			frame.setTextAreaFocus();
		}

	} // end inner class NewAction

	private class BotConfigurationAction extends AbstractAction {

		// set up action's name, icon, descriptions and mnemonic
		public BotConfigurationAction() {
			putValue(NAME, "Configure Bot");
			putValue(SMALL_ICON, new ImageIcon(MagicStrings.projectLocation + "img/robot.png"));
			log.debug(MagicStrings.projectLocation + "img/robot.png");
			putValue(SHORT_DESCRIPTION, "Bot Configuration");
			putValue(LONG_DESCRIPTION, "Configure Bot to use and its base settings");
			putValue(MNEMONIC_KEY, new Integer('N'));
		}

		// display window in which user can input entry
		@Override
		public void actionPerformed(ActionEvent e) {
			BotConfigurationFrame frame = new BotConfigurationFrame(log);
			desktop.add(frame);
			frame.setVisible(true);
		}

	} // end inner class NewAction

	private class PredicatesAction extends AbstractAction {

		// set up action's name, icon, descriptions and mnemonic
		public PredicatesAction() {
			putValue(NAME, "Predicates");
			putValue(SMALL_ICON, new ImageIcon(MagicStrings.projectLocation + "img/list.png"));
			log.debug(MagicStrings.projectLocation + "img/list.png");
			putValue(SHORT_DESCRIPTION, "Predicates");
			putValue(LONG_DESCRIPTION, "Change the Predicate settings for the current Bot");
			putValue(MNEMONIC_KEY, new Integer('P'));
		}

		// display window in which user can input entry
		@Override
		public void actionPerformed(ActionEvent e) {
			PredicatesFrame frame = new PredicatesFrame(log);
			desktop.add(frame);
			frame.setVisible(true);
		}

	} // end inner class NewAction

	private class PathsAction extends AbstractAction {

		// set up action's name, icon, descriptions and mnemonic
		public PathsAction() {
			putValue(NAME, "Paths");
			putValue(SMALL_ICON, new ImageIcon(MagicStrings.projectLocation + "img/paths.png"));
			log.debug(MagicStrings.projectLocation + "img/paths.png");
			putValue(SHORT_DESCRIPTION, "Paths");
			putValue(LONG_DESCRIPTION, "Change the Path settings for the current Bot");
			putValue(MNEMONIC_KEY, new Integer('P'));
		}

		// display window in which user can input entry
		@Override
		public void actionPerformed(ActionEvent e) {
			PathsFrame frame = new PathsFrame(log);
			desktop.add(frame);
			frame.setVisible(true);
		}

	} // end inner class NewAction

	private class HelpScreenAction extends AbstractAction {

		// set up action's name, icon, descriptions and mnemonic
		public HelpScreenAction() {
			putValue(NAME, "Help");

			putValue(SMALL_ICON, new ImageIcon(MagicStrings.projectLocation + "img/help.png"));
			log.debug(MagicStrings.projectLocation + "img/help.png");
			putValue(SHORT_DESCRIPTION, "Help");
			putValue(LONG_DESCRIPTION, "How to load and use Bots");
			putValue(MNEMONIC_KEY, new Integer('H'));
		}

		// display window in which user can input entry
		@Override
		public void actionPerformed(ActionEvent e) {
			// create new internal window
			HelpFrame frame = new HelpFrame();
			desktop.add(frame);
			frame.setVisible(true);
		}

	} // end inner class NewAction

	/**
	 * Private inner class to govern shutting down everything properly.
	 * 
	 * @author Dave
	 *
	 */
	private class ExitAction extends AbstractAction {

		public ExitAction() {
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "Exit");
			putValue(SMALL_ICON, new ImageIcon(MagicStrings.projectLocation + "img/exit.png"));
			log.debug(MagicStrings.projectLocation + "img/exit.png");
			putValue(LONG_DESCRIPTION, "Terminate the program");
			putValue(MNEMONIC_KEY, new Integer('x'));
		}

		// terminate program
		@Override
		public void actionPerformed(ActionEvent e) {
			shutDown(); // close database connection and terminate
		}

	} // end inner class ExitAction
}
