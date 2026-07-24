package com.careplus.client.employee.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
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

import com.careplus.client.employee.controller.StaffChatController;

/**
 * Employee side of the live chat staff pick a patient and exchange messages,
 * responding to and updating patients on their requests.
 *
 * Available to all three staff roles, since any of them may need to answer a
 * patient directly. The conversation is a read only JTextArea rather than a
 * table because a chat reads as continuous text, and making it non editable
 * keeps the transcript a true record of what was actually sent.
 *
 * The patient is picked from a combo the controller fills from the server,
 * matching how the patient side chooses its recipient. 
 */
public class StaffChatView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;
	private JLabel lblPatient;
	private JLabel lblMessage;
	private JComboBox<String> cboPatient;

	private JTextArea txtConversation;
	private JTextField txtMessage;

	private JButton btnSend;
	private JButton btnRefresh;
	private JButton btnClear;

	public StaffChatView() {

		super("Staff Chat", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(850, 600);
		setVisible(true);
	}

	private void initializeComponents() {

		lblTitle = new JLabel("Live Chat");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblPatient = new JLabel("Patient:");
		lblMessage = new JLabel("Message:");

		cboPatient = new JComboBox<>();

		txtConversation = new JTextArea();
		txtConversation.setEditable(false);
		txtConversation.setLineWrap(true);
		txtConversation.setWrapStyleWord(true);

		txtMessage = new JTextField();

		lblPatient.setDisplayedMnemonic(KeyEvent.VK_P);
		lblPatient.setLabelFor(cboPatient);

		lblMessage.setDisplayedMnemonic(KeyEvent.VK_M);
		lblMessage.setLabelFor(txtMessage);

		cboPatient.setToolTipText("Select the patient to chat with. Shortcut: Alt+P.");
		txtConversation.setToolTipText("Displays the sender, message content, timestamp and read status.");
		txtMessage.setToolTipText("Enter the message content. Shortcut: Alt+M.");

		btnSend = new JButton("Send");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnSend.setMnemonic(KeyEvent.VK_S);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnSend.setToolTipText("Send the message to the patient. Shortcut: Alt+S or Ctrl+Enter.");
		btnRefresh.setToolTipText("Reload the conversation. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the message field. Shortcut: Alt+C or Escape.");
	}

	private void buildGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(lblTitle);
		topPanel.add(lblPatient);
		topPanel.add(cboPatient);

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

	/*
	 * Configure Keyboard Shortcuts
	 */
	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "send");

		getRootPane().getActionMap().put("send", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnSend.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),
				"refresh");

		getRootPane().getActionMap().put("refresh", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnRefresh.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				"clear");

		getRootPane().getActionMap().put("clear", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
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

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(StaffChatController controller) {

		btnSend.addActionListener(e -> controller.sendMessage());
		btnRefresh.addActionListener(e -> controller.refresh());
		btnClear.addActionListener(e -> clearMessageField());

		// Switching patient swaps straight to that conversation, no Refresh needed.
		cboPatient.addActionListener(e -> controller.refresh());

	}

	public void clearMessageField() {
		txtMessage.setText("");
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JComboBox<String> getCboPatient() {
		return cboPatient;
	}

	public JTextField getTxtMessage() {
		return txtMessage;
	}
}