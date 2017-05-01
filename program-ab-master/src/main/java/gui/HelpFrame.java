package gui;

/**
 * David Monahan 40/04/2017
 * 
 * Frame to show help text
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

@SuppressWarnings("serial")
public class HelpFrame extends JInternalFrame implements ActionListener {
	private static final String HELP_PATH = "lib/help.txt";
//	private JTextArea info;
	private JTextPane pane;
	private StringBuilder text = new StringBuilder();

	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0;

	public HelpFrame() {
		super("Help", false, true);

/*		info = new JTextArea();
		info.setFont(new Font("Serif", Font.PLAIN, 14));
		info.setWrapStyleWord(true);
		info.setLineWrap(true);
		info.setEditable(false);
		info.setOpaque(false);
		info.setFocusable(false);
		info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));*/
		pane = new JTextPane();
		pane.setContentType("text/html");
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(HELP_PATH));
			String line = "";
			while((line = br.readLine()) != null){
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
	@Override
	/**
	 * Action listener
	 */
	public void actionPerformed(ActionEvent e) {

	}

}
