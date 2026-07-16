package com.careplus.client.employee.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Employee side of the live chat staff pick a patient and exchange messages,
 * responding to and updating patients on their requests.
 */
public class StaffChat extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;
	private JTextField txtPatient;

	private JTextArea txtConversation;
	private JTextField txtMessage;

	private JButton btnSend;
	private JButton btnRefresh;
	private JButton btnClear;

	public StaffChat() {

		super("Staff Chat", true, true, true, true);

		initializeComponents();
		buildGUI();

		setSize(850, 600);
		setVisible(true);
	}

	private void initializeComponents() {

		lblTitle = new JLabel("Live Chat");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		txtPatient = new JTextField(12);

		txtConversation = new JTextArea();
		txtConversation.setEditable(false);
		txtConversation.setLineWrap(true);
		txtConversation.setWrapStyleWord(true);

		txtMessage = new JTextField();

		btnSend = new JButton("Send");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");
	}

	private void buildGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(lblTitle);
		topPanel.add(new JLabel("   Patient ID:"));
		topPanel.add(txtPatient);

		JScrollPane conversationPane = new JScrollPane(txtConversation);

		JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
		bottomPanel.add(txtMessage, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(btnSend);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);

		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(conversationPane, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	public void appendMessage(String message) {
		txtConversation.append(message + "\n");
	}

	public void clearConversation() {
		txtConversation.setText("");
	}

	public void clearMessageField() {
		txtMessage.setText("");
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JTextField getTxtPatient() {
		return txtPatient;
	}

	public JTextArea getTxtConversation() {
		return txtConversation;
	}

	public JTextField getTxtMessage() {
		return txtMessage;
	}

	public JButton getBtnSend() {
		return btnSend;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}
}
