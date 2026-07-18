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
import javax.swing.JComboBox;
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

public class ComplaintView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblCategory;
	private JLabel lblPriority;
	private JLabel lblDescription;
	private JLabel lblParentId;
	private JLabel lblDateSubmitted;
	private JLabel lblResponse;
	private JLabel lblResponseDate;

	// Components
	private JComboBox<String> cboCategory;
	private JComboBox<String> cboPriority;
	private JTextArea txtDescription;
	private JTextField txtParentId;
	private JTextField txtDateSubmitted;
	private JTextArea txtResponse;
	private JTextField txtResponseDate;

	// Buttons
	private JButton btnSubmit;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnRefresh;
	private JButton btnClear;

	// Table
	private JTable tblComplaints;
	private DefaultTableModel tableModel;

	public ComplaintView() {

		super("Complaints", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(950, 650);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Complaint Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblCategory = new JLabel("Category");
		lblPriority = new JLabel("Status");
		lblDescription = new JLabel("Description");
		lblParentId = new JLabel("Parent Complaint ID");
		lblDateSubmitted = new JLabel("Date Submitted");
		lblResponse = new JLabel("Response");
		lblResponseDate = new JLabel("Response Date");

		cboCategory = new JComboBox<>();
		cboPriority = new JComboBox<>();

		cboCategory.addItem("GENERAL_HEALTH");
		cboCategory.addItem("MEDICATION_CONCERN");
		cboCategory.addItem("APPOINTMENT_ISSUE");

		cboPriority.addItem("SUBMITTED");
		cboPriority.addItem("ASSIGNED");
		cboPriority.addItem("IN_PROGRESS");
		cboPriority.addItem("RESOLVED");
		cboPriority.addItem("REOPENED");

		txtDescription = new JTextArea(5, 30);
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);

		txtParentId = new JTextField(15);
		txtDateSubmitted = new JTextField(15);
		txtResponse = new JTextArea(3, 30);
		txtResponseDate = new JTextField(15);

		txtDateSubmitted.setEditable(false);
		txtResponse.setEditable(false);
		txtResponse.setLineWrap(true);
		txtResponse.setWrapStyleWord(true);
		txtResponseDate.setEditable(false);
		cboPriority.setEnabled(false);

		lblCategory.setDisplayedMnemonic(KeyEvent.VK_G);
		lblCategory.setLabelFor(cboCategory);

		lblPriority.setDisplayedMnemonic(KeyEvent.VK_S);
		lblPriority.setLabelFor(cboPriority);

		lblDescription.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDescription.setLabelFor(txtDescription);

		lblParentId.setDisplayedMnemonic(KeyEvent.VK_P);
		lblParentId.setLabelFor(txtParentId);

		lblDateSubmitted.setDisplayedMnemonic(KeyEvent.VK_A);
		lblDateSubmitted.setLabelFor(txtDateSubmitted);

		lblResponse.setDisplayedMnemonic(KeyEvent.VK_O);
		lblResponse.setLabelFor(txtResponse);

		lblResponseDate.setDisplayedMnemonic(KeyEvent.VK_E);
		lblResponseDate.setLabelFor(txtResponseDate);

		cboCategory.setToolTipText("Select the complaint category. Shortcut: Alt+G.");
		cboPriority.setToolTipText("Displays the current complaint status.");
		txtDescription.setToolTipText("Enter the complaint description. Shortcut: Alt+D.");
		txtParentId.setToolTipText("Enter the original complaint ID when submitting a follow-up. Shortcut: Alt+P.");
		txtDateSubmitted.setToolTipText("Displays the date the complaint was submitted.");
		txtResponse.setToolTipText("Displays the response provided by hospital staff.");
		txtResponseDate.setToolTipText("Displays the date the response was submitted.");

		btnSubmit = new JButton("Submit");
		btnUpdate = new JButton("Follow Up");
		btnDelete = new JButton("Delete");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnSubmit.setMnemonic(KeyEvent.VK_S);
		btnUpdate.setMnemonic(KeyEvent.VK_F);
		btnDelete.setMnemonic(KeyEvent.VK_D);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_L);

		btnSubmit.setToolTipText("Submit the complaint. Shortcut: Alt+S or Ctrl+Enter.");
		btnUpdate.setToolTipText("Submit a follow-up to the selected complaint. Shortcut: Alt+F or Ctrl+F.");
		btnDelete.setToolTipText("Delete the selected complaint. Shortcut: Alt+D or Delete.");
		btnRefresh.setToolTipText("Reload complaint records. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the complaint form. Shortcut: Alt+L or Escape.");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(new Object[] {
				"Complaint ID",
				"Parent Complaint ID",
				"Category",
				"Description",
				"Date Submitted",
				"Response",
				"Response Date",
				"Status"
		});

		tblComplaints = new JTable(tableModel);
		tblComplaints.setRowHeight(25);
		tblComplaints.setToolTipText("Select a complaint to view, follow up, or delete.");

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
		formPanel.add(lblCategory, gbc);

		gbc.gridx = 1;
		formPanel.add(cboCategory, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(lblPriority, gbc);

		gbc.gridx = 1;
		formPanel.add(cboPriority, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(lblParentId, gbc);

		gbc.gridx = 1;
		formPanel.add(txtParentId, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(lblDateSubmitted, gbc);

		gbc.gridx = 1;
		formPanel.add(txtDateSubmitted, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formPanel.add(lblDescription, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtDescription), gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		formPanel.add(lblResponse, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtResponse), gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		formPanel.add(lblResponseDate, gbc);

		gbc.gridx = 1;
		formPanel.add(txtResponseDate, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(btnSubmit);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblComplaints), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

	}

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "submit");

		getRootPane().getActionMap().put("submit", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnSubmit.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "followUp");

		getRootPane().getActionMap().put("followUp", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnUpdate.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");

		getRootPane().getActionMap().put("delete", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnDelete.doClick();
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

	/*
	 * ===================== Helper Methods =====================
	 */

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addComplaint(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {

		cboCategory.setSelectedIndex(0);
		cboPriority.setSelectedIndex(0);
		txtDescription.setText("");
		txtParentId.setText("");
		txtDateSubmitted.setText("");
		txtResponse.setText("");
		txtResponseDate.setText("");

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/*
	 * ===================== Getters =====================
	 */

	public JComboBox<String> getCboCategory() {
		return cboCategory;
	}

	public JComboBox<String> getCboPriority() {
		return cboPriority;
	}

	public JTextArea getTxtDescription() {
		return txtDescription;
	}

	public JTextField getTxtParentId() {
		return txtParentId;
	}

	public JTextField getTxtDateSubmitted() {
		return txtDateSubmitted;
	}

	public JTextArea getTxtResponse() {
		return txtResponse;
	}

	public JTextField getTxtResponseDate() {
		return txtResponseDate;
	}

	public JTable getTblComplaints() {
		return tblComplaints;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnSubmit() {
		return btnSubmit;
	}

	public JButton getBtnUpdate() {
		return btnUpdate;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}

}