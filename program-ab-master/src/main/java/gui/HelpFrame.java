package gui;

/**
 * David Monahan 30/04/2017
 * 
 * Frame to show help text
 */
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;


/**
 * A simple frame for displaying a HTML formatted text file with short
 * descriptions of each button and some example features of the Chat Bot.
 * 
 * @author Dave
 *
 */
public class HelpFrame extends JInternalFrame {
	private static final String HELP_PATH = "lib/help.txt";
	private JTextPane pane;
	private StringBuilder text = new StringBuilder();

	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0;

	/**
	 * Displays a HelpFrame which has a html formatted textPane with helpful
	 * information on how to use the program and what each of the windows are
	 * used for.
	 */
	public HelpFrame() {
		super("Help", false, true);
		pane = new JTextPane();
		pane.setContentType("text/html");
		try {
			BufferedReader br = new BufferedReader(new FileReader(HELP_PATH));
			String line = "";
			while ((line = br.readLine()) != null) {
				text.append(line + "\n");
			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pane.setText(text.toString());
		JScrollPane scrolly = new JScrollPane();
		scrolly.setViewportView(pane);
		add(scrolly, BorderLayout.CENTER);
		// Makes sure it displays from the start
		pane.setCaretPosition(0);
		setBounds(xOffset, yOffset, 470, 540);
	}
}
