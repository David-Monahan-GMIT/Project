package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.Predicates;
import org.slf4j.Logger;

@SuppressWarnings("serial")
public class PredicatesFrame extends JInternalFrame implements ActionListener {
	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0, height = 600, width = 600;
	Logger log;

	private JButton saveButton, exitButton;
	private JPanel buttonPanel, mainPanel;
	private Predicates predicates = new Predicates();
	private HashMap<String, JTextField> fields = new HashMap<String, JTextField>();

	public PredicatesFrame(Logger log) {
		// Give the frame a name
		super("Predicate Configuration", false, true);
		this.log = log;
		
		saveButton = new JButton("Save");
		exitButton = new JButton("Exit");

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(saveButton, new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(exitButton, new FlowLayout(FlowLayout.RIGHT));
		add(buttonPanel, BorderLayout.SOUTH);

		saveButton.addActionListener(this);
		exitButton.addActionListener(this);

		setBounds(xOffset, yOffset, width, height);

		// Sets the default focus for the enter key
		getRootPane().setDefaultButton(saveButton);

		Bot.setAllPaths(MagicStrings.root_path + "/../ab/", MagicStrings.botName);
		predicates.getPredicateDefaults(MagicStrings.config_path+"/predicates.txt");
		log.debug(MagicStrings.config_path+"/predicates.txt");
		drawGui();
		
	}

	@Override
	/**
	 * Action listener to handle all 3 buttons for this frame.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == saveButton) {
			for (String key : fields.keySet()) {
				predicates.put(key, fields.get(key).getText());
			}
			predicates.setPredicateDefaults(MagicStrings.config_path+"/predicates.txt");
			this.dispose();
		}
		if (e.getSource() == exitButton) {
			this.dispose();
		}

	}

	private void drawGui() {
		
		int rowCount = predicates.size();
		JScrollPane scrolly = new JScrollPane();
		JPanel dataPanel = new JPanel();
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(rowCount, 2, 1, 5));
		dataPanel.add(mainPanel, BorderLayout.EAST);
		scrolly.setViewportView(dataPanel);
		scrolly.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		for(String name: predicates.keySet()) {
			createRow(name, predicates.get(name));
		}
		
		// clear the window and redraw
		getContentPane().removeAll();
		add(scrolly, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		revalidate();
		repaint();
	}
	
	private void createRow(String name, String value) {
		JLabel label = new JLabel(name, SwingConstants.LEFT);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(label);

		JTextField field = new JTextField(value,15);
		mainPanel.add(field);

		fields.put(name, field);		
	}

}