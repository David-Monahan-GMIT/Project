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
	private JPanel mainPanel;

	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0;

	public HelpFrame() {
		super("Help", false, true);
		// Set the rows and columns for GridLayout
		int rowCount = 1;
		int colCount = 1;

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(rowCount, colCount, 5, 5));
		JTextArea display = new JTextArea();
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(HELP_PATH));
			String line = "";
			while((line = br.readLine()) != null){
				display.append(line + "\n");
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		mainPanel.add(display);
		
		Container container = getContentPane();
		container.add(mainPanel, BorderLayout.WEST);
		

		setBounds(xOffset, yOffset, 470, 540);
		xOffset = (xOffset + 30) % 470;
		yOffset = (yOffset + 30) % 540;
	}
	@Override
	/**
	 * Action listener
	 */
	public void actionPerformed(ActionEvent e) {

	}

}
