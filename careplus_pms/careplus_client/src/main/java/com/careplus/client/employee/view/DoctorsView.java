package com.careplus.client.employee.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Read-only directory of doctors (used by receptionists when assigning staff
 * and by doctors for reference).
 */
public class DoctorsView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;
	private JButton btnRefresh;

	private JTable tblDoctors;
	private DefaultTableModel tableModel;

	public DoctorsView() {

		super("Doctors", true, true, true, true);

		initializeComponents();
		buildGUI();

		setSize(900, 550);
		setVisible(true);
	}

	private void initializeComponents() {

		lblTitle = new JLabel("Doctors Directory");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		btnRefresh = new JButton("Refresh");

		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(new Object[] { "Doctor ID", "Name", "Specialization", "Department" });

		tblDoctors = new JTable(tableModel);
		tblDoctors.setRowHeight(25);
	}

	private void buildGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(lblTitle);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(btnRefresh);

		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblDoctors), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addDoctor(Object[] row) {
		tableModel.addRow(row);
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JTable getTblDoctors() {
		return tblDoctors;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}
}
