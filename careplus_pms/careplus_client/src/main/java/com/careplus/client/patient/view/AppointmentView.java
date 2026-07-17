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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AppointmentView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblDoctor;
	private JLabel lblDepartment;
	private JLabel lblDate;
	private JLabel lblTime;

	// Fields
	private JComboBox<String> cboDoctor;
	private JComboBox<String> cboDepartment;
	private JTextField txtDate;
	private JTextField txtTime;

	// Buttons
	private JButton btnSchedule;
	private JButton btnUpdate;
	private JButton btnCancel;
	private JButton btnRefresh;
	private JButton btnClear;

	// Table
	private JTable tblAppointments;
	private DefaultTableModel tableModel;

	public AppointmentView() {

		super("Appointments", true, true, true, true);

		initializeComponents();
		buildGUI();

		setSize(900, 600);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Appointment Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblDoctor = new JLabel("Doctor");
		lblDepartment = new JLabel("Department");
		lblDate = new JLabel("Appointment Date");
		lblTime = new JLabel("Appointment Time");

		cboDoctor = new JComboBox<>();
		cboDepartment = new JComboBox<>();

		txtDate = new JTextField(15);
		txtTime = new JTextField(15);

		btnSchedule = new JButton("Schedule");
		btnUpdate = new JButton("Update");
		btnCancel = new JButton("Cancel");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(
				new Object[] { "Appointment ID", "Doctor", "Department", "Date", "Time", "Status" });

		tblAppointments = new JTable(tableModel);
		tblAppointments.setRowHeight(25);

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
		formPanel.add(lblDoctor, gbc);

		gbc.gridx = 1;
		formPanel.add(cboDoctor, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(lblDepartment, gbc);

		gbc.gridx = 1;
		formPanel.add(cboDepartment, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(lblDate, gbc);

		gbc.gridx = 1;
		formPanel.add(txtDate, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(lblTime, gbc);

		gbc.gridx = 1;
		formPanel.add(txtTime, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(btnSchedule);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnCancel);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblAppointments), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addAppointment(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {

		cboDoctor.removeAllItems();
		cboDepartment.removeAllItems();

		txtDate.setText("");
		txtTime.setText("");

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JComboBox<String> getCboDoctor() {
		return cboDoctor;
	}

	public JComboBox<String> getCboDepartment() {
		return cboDepartment;
	}

	public JTextField getTxtDate() {
		return txtDate;
	}

	public JTextField getTxtTime() {
		return txtTime;
	}

	public JTable getTblAppointments() {
		return tblAppointments;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnSchedule() {
		return btnSchedule;
	}

	public JButton getBtnUpdate() {
		return btnUpdate;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}

}