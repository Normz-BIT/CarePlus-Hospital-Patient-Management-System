package com.careplus.client.patient.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public class PaymentView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblAmount;
	private JLabel lblDescription;

	// Fields
	private JTextField txtAmount;
	private JTextArea txtDescription;

	// Buttons
	private JButton btnPay;
	private JButton btnRefresh;
	private JButton btnClear;

	// Table
	private JTable tblPayments;
	private DefaultTableModel tableModel;

	public PaymentView() {

		super("Payments", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(900, 600);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Payment Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblAmount = new JLabel("Amount");
		lblDescription = new JLabel("Description");

		txtAmount = new JTextField(20);
		txtDescription = new JTextArea(3,20);
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);

		lblAmount.setDisplayedMnemonic(KeyEvent.VK_A);
		lblAmount.setLabelFor(txtAmount);

		lblDescription.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDescription.setLabelFor(txtDescription);

		txtAmount.setToolTipText("Enter the amount to pay. Shortcut: Alt+A.");
		txtDescription.setToolTipText("Enter what the payment is for. Shortcut: Alt+D.");

		btnPay = new JButton("Make Payment");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnPay.setMnemonic(KeyEvent.VK_P);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnPay.setToolTipText("Submit the payment. Shortcut: Alt+P or Ctrl+Enter.");
		btnRefresh.setToolTipText("Reload payment records. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the payment fields. Shortcut: Alt+C or Escape.");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(new Object[] { "Payment ID", "Amount Paid", "Outstanding Balance", "Description", "Date" });

		tblPayments = new JTable(tableModel);
		tblPayments.setRowHeight(25);

	}

	private void buildGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel formPanel = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(6, 6, 6, 6);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;

		formPanel.add(lblTitle, gbc);

		gbc.gridwidth = 1;

		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(lblAmount, gbc);

		gbc.gridx = 1;
		formPanel.add(txtAmount, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		
		formPanel.add(lblDescription, gbc);

		gbc.gridx = 1;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;		
		formPanel.add(txtDescription, gbc);
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(btnPay);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblPayments), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

	}

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "pay");

		getRootPane().getActionMap().put("pay", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnPay.doClick();
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

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addPayment(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {

		txtAmount.setText("");
		txtDescription.setText("");

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JTextField getTxtAmount() {
		return txtAmount;
	}


	public JTextArea getTxtDescription() {
		return txtDescription;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnPay() {
		return btnPay;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}


}
