package com.careplus.server.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ServerView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton startBtn;
	private JButton stopBtn;
	private JButton resetDbs;
	
	private JTextArea txtConsole;
	
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
		add(topPanel, BorderLayout.CENTER);
		
		setVisible(true);
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
