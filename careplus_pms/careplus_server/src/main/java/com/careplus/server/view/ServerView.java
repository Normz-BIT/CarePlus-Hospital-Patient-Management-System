package com.careplus.server.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.careplus.server.net.Server;

public class ServerView extends JFrame  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton startBtn;
	private JButton stopBtn;
	private JButton resetDbs;
	
	private JTextArea txtConsole;
	
	private Server server;
	private Thread serverThread;
	
	public ServerView() {
		
		setTitle("CarePlus Hospital Server");
		setSize(600,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		startBtn = new JButton("Start Server");
		stopBtn = new JButton("Stop Server");
		resetDbs = new JButton("Clear/ Reset Database");
		
		txtConsole = new JTextArea();
		txtConsole.setEditable(false);
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		topPanel.add(startBtn);
		topPanel.add(stopBtn);
		topPanel.add(resetDbs);
		
		add(topPanel, BorderLayout.NORTH);
		//add(txtConsole, BorderLayout.CENTER);
		add(new JScrollPane(txtConsole), BorderLayout.CENTER);
		
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
	}
	
	public JButton getStartBtn() {
		return startBtn;
	}
	public JButton getStopBtn() {
		return stopBtn;
	}
	public JButton getRestartDbs() {
		return resetDbs;
	}
	public JTextArea getTxtConsole() {
		return txtConsole;
	}
	
public static void main(String[] args) {
	
	new ServerView();
}

}
