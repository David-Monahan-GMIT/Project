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

	private ArrayList<JButton> buttons;

	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0;

	private int rowCount = 5;
	private int colCount = 5;

	public ChatFrame(String botName, Boolean traceMode, String action, Logger log) {
		super("Chat Session", true, true);

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(rowCount, colCount, 5, 5));

		Container container = getContentPane();
		container.add(mainPanel, BorderLayout.CENTER);

		setBounds(xOffset, yOffset, 500, 500);
		xOffset = (xOffset + 30) % 500;
		yOffset = (yOffset + 30) % 500;
		
        Bot bot = new Bot(botName, MagicStrings.root_path+"/../ab/", action);
		Chat chatSession = new Chat(bot);
//       bot.preProcessor.normalizeFile("c:/ab/bots/super/aiml/thats.txt", "c:/ab/bots/super/aiml/normalthats.txt");
       bot.brain.nodeStats();
       MagicBooleans.trace_mode = traceMode;
       String textLine="";
       while (true) {
           System.out.print("Human: ");
			textLine = IOUtils.readInputTextLine();
           if (textLine == null || textLine.length() < 1)  textLine = MagicStrings.null_input;
           if (textLine.equals("q")) System.exit(0);
           else if (textLine.equals("wq")) {
               bot.writeQuit();
               System.exit(0);
           }
           //else if (textLine.equals("ab")) testAB(bot);
           else {
               String request = textLine;
               log.debug("STATE="+request+":THAT="+chatSession.thatHistory.get(0).get(0)+":TOPIC="+chatSession.predicates.get("topic"));
               String response = chatSession.multisentenceRespond(request);
               while (response.contains("&lt;")) response = response.replace("&lt;","<");
               while (response.contains("&gt;")) response = response.replace("&gt;",">");
               log.info("Robot: "+response);

           }

       }
	}



	@Override
	/**
	 * 
	 */
	public synchronized void actionPerformed(ActionEvent e) {
		
	}


}
