package com.careplus.client.patient.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class ChatView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblRecipient;
	private JLabel lblMessage;

	// Components
	private JComboBox<String> cboRecipient;
	private JTextArea txtConversation;
	private JTextField txtMessage;

	// Buttons
	private JButton btnSend;
	private JButton btnRefresh;
	private JButton btnClear;

	public ChatView() {

		super("Patient Support Chat", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(850, 600);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Patient Support Chat");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblRecipient = new JLabel("To:");
		lblMessage = new JLabel("Message:");

		cboRecipient = new JComboBox<>();
		cboRecipient.addItem("Receptionist");
		cboRecipient.addItem("Doctor");
		cboRecipient.addItem("Nurse");

		txtConversation = new JTextArea();
		txtConversation.setEditable(false);
		txtConversation.setLineWrap(true);
		txtConversation.setWrapStyleWord(true);

		txtMessage = new JTextField();

		lblRecipient.setDisplayedMnemonic(KeyEvent.VK_T);
		lblRecipient.setLabelFor(cboRecipient);

		lblMessage.setDisplayedMnemonic(KeyEvent.VK_M);
		lblMessage.setLabelFor(txtMessage);

		cboRecipient.setToolTipText("Select the recipient of the message. Shortcut: Alt+T.");
		txtConversation.setToolTipText("Displays the sender, message content, timestamp and read status.");
		txtMessage.setToolTipText("Enter the message content. Shortcut: Alt+M.");

		btnSend = new JButton("Send");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnSend.setMnemonic(KeyEvent.VK_S);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnSend.setToolTipText("Send the message. Shortcut: Alt+S or Ctrl+Enter.");
		btnRefresh.setToolTipText("Reload the conversation. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the message field. Shortcut: Alt+C or Escape.");

	}

	private void buildGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(lblTitle);
		topPanel.add(lblRecipient);
		topPanel.add(cboRecipient);

		JScrollPane conversationPane = new JScrollPane(txtConversation);

		JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

		bottomPanel.add(lblMessage, BorderLayout.WEST);
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

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "send");

		getRootPane().getActionMap().put("send", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnSend.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh");

		getRootPane().getActionMap().put("refresh", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnRefresh.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear");

		getRootPane().getActionMap().put("clear", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnClear.doClick();
			}
		});

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

	public JComboBox<String> getCboRecipient() {

		return cboRecipient;

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
