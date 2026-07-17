package com.careplus.client.patient.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class ComplaintView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblCategory;
	private JLabel lblPriority;
	private JLabel lblDescription;

	// Components
	private JComboBox<String> cboCategory;
	private JComboBox<String> cboPriority;
	private JTextArea txtDescription;

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

		setSize(950, 600);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Complaint Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblCategory = new JLabel("Category");
		lblPriority = new JLabel("Priority");
		lblDescription = new JLabel("Description");

		cboCategory = new JComboBox<>();
		cboPriority = new JComboBox<>();

		cboCategory.addItem("Medical");
		cboCategory.addItem("Billing");
		cboCategory.addItem("Appointment");
		cboCategory.addItem("Staff");
		cboCategory.addItem("Other");

		cboPriority.addItem("Low");
		cboPriority.addItem("Medium");
		cboPriority.addItem("High");

		txtDescription = new JTextArea(5, 30);
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);

		btnSubmit = new JButton("Submit");
		btnUpdate = new JButton("Update");
		btnDelete = new JButton("Delete");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(new Object[] { "Complaint ID", "Category", "Priority", "Date", "Status" });

		tblComplaints = new JTable(tableModel);
		tblComplaints.setRowHeight(25);

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
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formPanel.add(lblDescription, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtDescription), gbc);

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