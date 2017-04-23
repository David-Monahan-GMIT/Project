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
		highScoreAction = new PredicatesAction();
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

	private class PredicatesAction extends AbstractAction {

		// set up action's name, icon, descriptions and mnemonic
		public PredicatesAction() {
			putValue(NAME, "Predicates");
			// putValue(SMALL_ICON, new
			// ImageIcon(getClass().getResource("images/New24.png")));
			putValue(SHORT_DESCRIPTION, "Predicates");
			putValue(LONG_DESCRIPTION, "Change the Predicate settings for the current Bot");
			putValue(MNEMONIC_KEY, new Integer('P'));
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
			putValue(LONG_DESCRIPTION, "How to load and use Bots");
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
