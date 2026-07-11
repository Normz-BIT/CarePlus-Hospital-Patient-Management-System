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

public class EmployeeComplaint extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblComplaintId;
	private JLabel lblCategory;
	private JLabel lblPriority;
	private JLabel lblStatus;
	private JLabel lblRemarks;

	// Components
	private JTextField txtComplaintId;
	private JComboBox<String> cboCategory;
	private JComboBox<String> cboPriority;
	private JComboBox<String> cboStatus;
	private JTextArea txtRemarks;

	// Buttons
	private JButton btnAssign;
	private JButton btnResolve;
	private JButton btnRefresh;
	private JButton btnClear;

	// Table
	private JTable tblComplaints;
	private DefaultTableModel tableModel;

	// Summary + category filter
	private JLabel lblSummary;
	private JComboBox<String> cboFilter;
	private JButton btnSearch;

	public EmployeeComplaint() {

		super("Complaint Manager", true, true, true, true);

		initializeComponents();
		buildGUI();

		setSize(1000, 650);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Complaint Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblComplaintId = new JLabel("Complaint ID");
		lblCategory = new JLabel("Category");
		lblPriority = new JLabel("Priority");
		lblStatus = new JLabel("Status");
		lblRemarks = new JLabel("Remarks");

		txtComplaintId = new JTextField(20);

		cboCategory = new JComboBox<>();
		cboPriority = new JComboBox<>();
		cboStatus = new JComboBox<>();

		txtRemarks = new JTextArea(4, 30);
		txtRemarks.setLineWrap(true);
		txtRemarks.setWrapStyleWord(true);

		btnAssign = new JButton("Assign");
		btnResolve = new JButton("Resolve");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(
				new Object[] { "Complaint ID", "Category", "Priority", "Status", "Patient", "Date" });

		tblComplaints = new JTable(tableModel);
		tblComplaints.setRowHeight(25);

		lblSummary = new JLabel(" ");
		cboFilter = new JComboBox<>();
		btnSearch = new JButton("Search");

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
		formPanel.add(lblComplaintId, gbc);

		gbc.gridx = 1;
		formPanel.add(txtComplaintId, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(lblCategory, gbc);

		gbc.gridx = 1;
		formPanel.add(cboCategory, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(lblPriority, gbc);

		gbc.gridx = 1;
		formPanel.add(cboPriority, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(lblStatus, gbc);

		gbc.gridx = 1;
		formPanel.add(cboStatus, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formPanel.add(lblRemarks, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtRemarks), gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(btnAssign);
		buttonPanel.add(btnResolve);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterRow.add(new JLabel("Filter Category"));
		filterRow.add(cboFilter);
		filterRow.add(btnSearch);

		JPanel summaryPanel = new JPanel(new BorderLayout());
		summaryPanel.add(lblSummary, BorderLayout.NORTH);
		summaryPanel.add(filterRow, BorderLayout.SOUTH);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(formPanel, BorderLayout.NORTH);
		northPanel.add(summaryPanel, BorderLayout.SOUTH);

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblComplaints), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addComplaint(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {

		txtComplaintId.setText("");
		txtRemarks.setText("");

		cboCategory.removeAllItems();
		cboPriority.removeAllItems();
		cboStatus.removeAllItems();

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/** Sets the plain-text summary line. */
	public void setSummary(String text) {
		lblSummary.setText(text);
	}

	public JLabel getLblSummary() {
		return lblSummary;
	}

	public JComboBox<String> getCboFilter() {
		return cboFilter;
	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

	public JTextField getTxtComplaintId() {
		return txtComplaintId;
	}

	public JComboBox<String> getCboCategory() {
		return cboCategory;
	}

	public JComboBox<String> getCboPriority() {
		return cboPriority;
	}

	public JComboBox<String> getCboStatus() {
		return cboStatus;
	}

	public JTextArea getTxtRemarks() {
		return txtRemarks;
	}

	public JTable getTblComplaints() {
		return tblComplaints;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JButton getBtnAssign() {
		return btnAssign;
	}

	public JButton getBtnResolve() {
		return btnResolve;
	}

	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	public JButton getBtnClear() {
		return btnClear;
	}

}
