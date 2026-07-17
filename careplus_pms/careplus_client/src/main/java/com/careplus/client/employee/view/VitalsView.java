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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Nurse workspace: view assigned cases and record vital signs, patient
 * observations, and nursing notes.
 */
public class VitalsView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;
	private JLabel lblPatientId;
	private JLabel lblTemperature;
	private JLabel lblBloodPressure;
	private JLabel lblPulse;
	private JLabel lblRespiratory;
	private JLabel lblObservations;
	private JLabel lblNursingNotes;

	private JTextField txtPatientId;
	private JTextField txtTemperature;
	private JTextField txtBloodPressure;
	private JTextField txtPulse;
	private JTextField txtRespiratory;
	private JTextArea txtObservations;
	private JTextArea txtNursingNotes;

	private JButton btnRecord;
	private JButton btnRefresh;
	private JButton btnClear;

	private JTable tblCases;
	private DefaultTableModel tableModel;

	public VitalsView() {

		super("Nurse Station", true, true, true, true);

		initializeComponents();
		buildGUI();

		setSize(1000, 700);
		setVisible(true);
	}

	private void initializeComponents() {

		lblTitle = new JLabel("Vital Signs & Nursing Notes");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblPatientId = new JLabel("Patient ID");
		lblTemperature = new JLabel("Temperature (°C)");
		lblBloodPressure = new JLabel("Blood Pressure");
		lblPulse = new JLabel("Pulse (bpm)");
		lblRespiratory = new JLabel("Respiratory Rate");
		lblObservations = new JLabel("Observations");
		lblNursingNotes = new JLabel("Nursing Notes");

		txtPatientId = new JTextField(20);
		txtTemperature = new JTextField(20);
		txtBloodPressure = new JTextField(20);
		txtPulse = new JTextField(20);
		txtRespiratory = new JTextField(20);

		txtObservations = new JTextArea(3, 30);
		txtObservations.setLineWrap(true);
		txtObservations.setWrapStyleWord(true);

		txtNursingNotes = new JTextArea(3, 30);
		txtNursingNotes.setLineWrap(true);
		txtNursingNotes.setWrapStyleWord(true);

		btnRecord = new JButton("Record");
		btnRefresh = new JButton("Refresh Cases");
		btnClear = new JButton("Clear");

		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(new Object[] { "Patient ID", "Name", "Ward", "Complaint", "Status" });

		tblCases = new JTable(tableModel);
		tblCases.setRowHeight(25);
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

		addRow(formPanel, gbc, 1, lblPatientId, txtPatientId);
		addRow(formPanel, gbc, 2, lblTemperature, txtTemperature);
		addRow(formPanel, gbc, 3, lblBloodPressure, txtBloodPressure);
		addRow(formPanel, gbc, 4, lblPulse, txtPulse);
		addRow(formPanel, gbc, 5, lblRespiratory, txtRespiratory);

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formPanel.add(lblObservations, gbc);
		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtObservations), gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		formPanel.add(lblNursingNotes, gbc);
		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtNursingNotes), gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(btnRecord);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblCases), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private void addRow(JPanel panel, GridBagConstraints gbc, int y, JLabel label, JTextField field) {
		gbc.gridx = 0;
		gbc.gridy = y;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label, gbc);
		gbc.gridx = 1;
		panel.add(field, gbc);
	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addCase(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {
		txtPatientId.setText("");
		txtTemperature.setText("");
		txtBloodPressure.setText("");
		txtPulse.setText("");
		txtRespiratory.setText("");
		txtObservations.setText("");
		txtNursingNotes.setText("");
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JTextField getTxtPatientId() {
		return txtPatientId;
	}

	public JTextField getTxtTemperature() {
		return txtTemperature;
	}

	public JTextField getTxtBloodPressure() {
		return txtBloodPressure;
	}

	public JTextField getTxtPulse() {
		return txtPulse;
	}

	public JTextField getTxtRespiratory() {
		return txtRespiratory;
	}

	public JTextArea getTxtObservations() {
		return txtObservations;
	}

	public JTextArea getTxtNursingNotes() {
		return txtNursingNotes;
	}

	public JTable getTblCases() {
		return tblCases;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnRecord() {
		return btnRecord;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}
}
