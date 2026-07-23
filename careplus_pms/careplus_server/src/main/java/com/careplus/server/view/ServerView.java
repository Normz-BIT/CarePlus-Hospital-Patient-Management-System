package com.careplus.server.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


import com.careplus.server.net.Server;

//public class ServerView extends JFrame  {

import com.careplus.server.util.ServerConsole;

import com.careplus.server.controller.ServerController;

public class ServerView extends JFrame implements ServerConsole {

	private static final long serialVersionUID = 1L;

	private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

	// Buttons
	private JButton startBtn;
	private JButton stopBtn;
	private JButton resetDbs;
	
	//icons
	List<Image> icons;
	// Console
	private JTextArea txtConsole;

	
	private Server server;
	private Thread serverThread;
	

	public ServerView() {

		setTitle("CarePlus Hospital Server");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		initializeComponents();
		buildGUI();

		setVisible(true);
	}

	private void initializeComponents() {

		startBtn = new JButton("Start Server");
		stopBtn = new JButton("Stop Server");
		resetDbs = new JButton("Clear/ Reset Database");

		startBtn.setToolTipText("Begin listening for client connections.");
		stopBtn.setToolTipText("Stop listening and disconnect all clients.");
		resetDbs.setToolTipText("Drop careplus_db and rebuild it from careplus_create_database.sql.");

		// Nothing is listening yet.
		stopBtn.setEnabled(false);

		txtConsole = new JTextArea();
		txtConsole.setEditable(false);
		txtConsole.setFont(new Font("Consolas", Font.PLAIN, 12));
	}

	private void buildGUI() {

		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

		topPanel.add(startBtn);
		topPanel.add(stopBtn);
		topPanel.add(resetDbs);

		JScrollPane consoleScroll = new JScrollPane(txtConsole);
		consoleScroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		add(topPanel, BorderLayout.NORTH);

		//add(txtConsole, BorderLayout.CENTER);
		add(new JScrollPane(txtConsole), BorderLayout.CENTER);

		add(consoleScroll, BorderLayout.CENTER);

		setVisible(true);
		
		startBtn.addActionListener(e -> startServer());

		stopBtn.addActionListener(e -> stopServer());
	}
	
	private void stopServer() {

	    if (server == null) {
	        appendMessage("Server is not running.");
	        return;
	    }

	    server.stopServer();

	    server = null;
	    serverThread = null;

	    appendMessage("Server stopped.");
	}

	private void startServer() {

	    if (serverThread != null && serverThread.isAlive()) {
	        appendMessage("Server is already running.");
	        return;
	    }

	    serverThread = new Thread(() -> {

	        appendMessage("Starting server...");

	        server = new Server(this);

	    });

	    serverThread.start();
	}
	public void appendMessage(String message) {
		 SwingUtilities.invokeLater(() -> {
		        txtConsole.append(message + "\n");
		    });

		setWindowIcon();
	}

	/*
	 * Appends a timestamped line to the console. Safe to call from any thread - the
	 * server and database work run off the event dispatch thread.
	 */
	@Override
	public void println(String message) {

		SwingUtilities.invokeLater(() -> {

			txtConsole.append("[" + LocalTime.now().format(TIME) + "] " + message + "\n");
			txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
		});
	}

	/*
	 * Unlike println above, this touches the text area directly with no invokeLater,
	 * so it is only safe from the event dispatch thread. That holds today because
	 * its only caller is a button handler, but it must not be called from the server
	 * or database worker threads the way println can be.
	 */
	public void clearConsole() {
		txtConsole.setText("");
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(ServerController controller) {

		startBtn.addActionListener(e -> controller.start());
		stopBtn.addActionListener(e -> controller.stop());
		resetDbs.addActionListener(e -> controller.resetDatabase());


	}
	
	
	private void setWindowIcon() {

		icons = new ArrayList<>();

		// Load multiple resolutions of the logo
		icons.add(new ImageIcon(getClass().getResource("/CarePlusLogo64.png")).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
		icons.add(new ImageIcon(getClass().getResource("/CarePlusLogo32.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
		icons.add(new ImageIcon(getClass().getResource("/CarePlusLogo128.png")).getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH));

		// Apply the list to the window
		this.setIconImages(icons);

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/*
	 * Asks the user to confirm a destructive action. Returns true only on Yes.
	 */
	public boolean confirm(String message, String title) {

		return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
	}

	/*
	 * Reflects whether the server is currently listening.
	 */
	public void setRunning(boolean running) {

		startBtn.setEnabled(!running);
		stopBtn.setEnabled(running);
	}

	public void setResetEnabled(boolean enabled) {
		resetDbs.setEnabled(enabled);
	}

}
