package com.careplus.client.employee.view;

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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DiagnosisView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblPatientId;
	private JLabel lblDiagnosis;
	private JLabel lblTreatment;
	private JLabel lblPrescription;
	private JLabel lblStatus;

	// Components
	private JTextField txtPatientId;
	private JTextField txtDiagnosis;
	private JTextArea txtTreatment;
	private JTextArea txtPrescription;
	private JComboBox<String> cboStatus;

	// Buttons
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnRefresh;
	private JButton btnClear;

	// Table
	private JTable tblDiagnosis;
	private DefaultTableModel tableModel;

	public DiagnosisView() {

		super("Diagnosis Management", true, true, true, true);

		initializeComponents();
		buildGUI();

		setSize(1000, 650);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Diagnosis Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblPatientId = new JLabel("Patient ID");
		lblDiagnosis = new JLabel("Diagnosis");
		lblTreatment = new JLabel("Treatment");
		lblPrescription = new JLabel("Prescription");
		lblStatus = new JLabel("Status");

		txtPatientId = new JTextField(20);
		txtDiagnosis = new JTextField(20);

		txtTreatment = new JTextArea(4, 30);
		txtTreatment.setLineWrap(true);
		txtTreatment.setWrapStyleWord(true);

		txtPrescription = new JTextArea(4, 30);
		txtPrescription.setLineWrap(true);
		txtPrescription.setWrapStyleWord(true);

		cboStatus = new JComboBox<>();

		btnSave = new JButton("Save");
		btnUpdate = new JButton("Update");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(
				new Object[] { "Record ID", "Patient ID", "Diagnosis", "Treatment", "Prescription", "Status", "Date" });

		tblDiagnosis = new JTable(tableModel);
		tblDiagnosis.setRowHeight(25);

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
		formPanel.add(lblDiagnosis, gbc);

		gbc.gridx = 1;
		formPanel.add(txtDiagnosis, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formPanel.add(lblTreatment, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtTreatment), gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(lblPrescription, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtPrescription), gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(lblStatus, gbc);

		gbc.gridx = 1;
		formPanel.add(cboStatus, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(btnSave);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblDiagnosis), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addDiagnosis(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {

		txtPatientId.setText("");
		txtDiagnosis.setText("");
		txtTreatment.setText("");
		txtPrescription.setText("");

		cboStatus.removeAllItems();

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JTextField getTxtPatientId() {
		return txtPatientId;
	}

	public JTextField getTxtDiagnosis() {
		return txtDiagnosis;
	}

	public JTextArea getTxtTreatment() {
		return txtTreatment;
	}

	public JTextArea getTxtPrescription() {
		return txtPrescription;
	}

	public JComboBox<String> getCboStatus() {
		return cboStatus;
	}

	public JTable getTblDiagnosis() {
		return tblDiagnosis;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public JButton getBtnUpdate() {
		return btnUpdate;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}

}
