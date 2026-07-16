package com.careplus.client.employee.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Doctor's assigned-patients directory: lists patient id, name, contact,
 * complaint and history, and lets the doctor schedule a follow-up.
 */
public class Patients extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;
	private JLabel lblPatientId;
	private JLabel lblDate;
	private JLabel lblTime;

	private JTextField txtPatientId;
	private JTextField txtDate;
	private JTextField txtTime;

	private JButton btnFollowUp;
	private JButton btnRefresh;
	private JButton btnClear;

	private JTable tblPatients;
	private DefaultTableModel tableModel;

	public Patients() {

		super("My Patients", true, true, true, true);

		initializeComponents();
		buildGUI();

		setSize(1050, 650);
		setVisible(true);
	}

	private void initializeComponents() {

		lblTitle = new JLabel("Assigned Patients");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblPatientId = new JLabel("Patient ID");
		lblDate = new JLabel("Follow-up Date");
		lblTime = new JLabel("Follow-up Time");

		txtPatientId = new JTextField(20);
		txtDate = new JTextField(20);
		txtTime = new JTextField(20);

		btnFollowUp = new JButton("Schedule Follow-up");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		tableModel = new DefaultTableModel();
		tableModel
				.setColumnIdentifiers(new Object[] { "Patient ID", "Name", "Contact", "Complaint", "Medical History" });

		tblPatients = new JTable(tableModel);
		tblPatients.setRowHeight(25);
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
		formPanel.add(lblPatientId, gbc);
		gbc.gridx = 1;
		formPanel.add(txtPatientId, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(lblDate, gbc);
		gbc.gridx = 1;
		formPanel.add(txtDate, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(lblTime, gbc);
		gbc.gridx = 1;
		formPanel.add(txtTime, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(btnFollowUp);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblPatients), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addPatient(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {
		txtPatientId.setText("");
		txtDate.setText("");
		txtTime.setText("");
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JTextField getTxtPatientId() {
		return txtPatientId;
	}

	public JTextField getTxtDate() {
		return txtDate;
	}

	public JTextField getTxtTime() {
		return txtTime;
	}

	public JTable getTblPatients() {
		return tblPatients;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnFollowUp() {
		return btnFollowUp;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}
}
