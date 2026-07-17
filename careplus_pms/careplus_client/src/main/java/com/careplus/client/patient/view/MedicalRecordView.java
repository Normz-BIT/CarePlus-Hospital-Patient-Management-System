package com.careplus.client.patient.view;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MedicalRecordView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblDiagnosis;
	private JLabel lblTreatment;
	private JLabel lblPrescription;

	// Fields
	private JTextField txtDiagnosis;
	private JTextArea txtTreatment;
	private JTextArea txtPrescription;

	// Buttons
	private JButton btnRefresh;
	private JButton btnClear;

	// Table
	private JTable tblMedicalRecords;
	private DefaultTableModel tableModel;

	public MedicalRecordView() {

		super("Medical Records", true, true, true, true);

		initializeComponents();
		buildGUI();

		setSize(950, 600);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Medical Records");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblDiagnosis = new JLabel("Diagnosis");
		lblTreatment = new JLabel("Treatment");
		lblPrescription = new JLabel("Prescription");

		txtDiagnosis = new JTextField(30);

		txtTreatment = new JTextArea(4, 30);
		txtTreatment.setLineWrap(true);
		txtTreatment.setWrapStyleWord(true);

		txtPrescription = new JTextArea(4, 30);
		txtPrescription.setLineWrap(true);
		txtPrescription.setWrapStyleWord(true);

		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(new Object[] { "Record ID", "Diagnosis", "Treatment", "Prescription", "Date" });

		tblMedicalRecords = new JTable(tableModel);
		tblMedicalRecords.setRowHeight(25);

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
		formPanel.add(lblDiagnosis, gbc);

		gbc.gridx = 1;
		formPanel.add(txtDiagnosis, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formPanel.add(lblTreatment, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtTreatment), gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(lblPrescription, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtPrescription), gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblMedicalRecords), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addMedicalRecord(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {

		txtDiagnosis.setText("");
		txtTreatment.setText("");
		txtPrescription.setText("");

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
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

	public JTable getTblMedicalRecords() {
		return tblMedicalRecords;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}

}
