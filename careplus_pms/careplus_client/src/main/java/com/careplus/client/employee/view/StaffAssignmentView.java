package com.careplus.client.employee.view;

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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import com.careplus.client.employee.controller.StaffAssignmentController;

/**
 * Receptionist's staff assignment workspace: attach a complaint to an employee
 * and review existing assignments.
 *
 * Works with untyped Object array rows rather than a typed model, because
 * careplus_common has no StaffAssignment class to bind to. See the note on
 * StaffAssignmentController.
 *
 * The department combo is filled from a hardcoded list in that controller rather
 * than from the server, since department is free text on Employee rather than an
 * enum.
 */
public class StaffAssignmentView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;
	private JLabel lblComplaintId;
	private JLabel lblStaffId;
	private JLabel lblDepartment;
	private JLabel lblStatus;

	private JTextField txtComplaintId;
	private JTextField txtStaffId;
	private JComboBox<String> cboDepartment;
	private JComboBox<String> cboStatus;

	private JButton btnAssign;
	private JButton btnUpdate;
	private JButton btnRefresh;
	private JButton btnClear;

	private JTable tblAssignments;
	private DefaultTableModel tableModel;

	public StaffAssignmentView() {
		super("Complaint Staff Assignment", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(1000, 650);
		setVisible(true);
	}

	private void initializeComponents() {
		lblTitle = new JLabel("Complaint Staff Assignment");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblComplaintId = new JLabel("Complaint ID");
		lblStaffId = new JLabel("Employee ID");
		lblDepartment = new JLabel("Employee Department");
		lblStatus = new JLabel("Complaint Status");

		txtComplaintId = new JTextField(20);
		txtStaffId = new JTextField(20);

		cboDepartment = new JComboBox<>();
		cboStatus = new JComboBox<>();

		lblComplaintId.setDisplayedMnemonic(KeyEvent.VK_I);
		lblComplaintId.setLabelFor(txtComplaintId);

		lblStaffId.setDisplayedMnemonic(KeyEvent.VK_E);
		lblStaffId.setLabelFor(txtStaffId);

		lblDepartment.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDepartment.setLabelFor(cboDepartment);

		lblStatus.setDisplayedMnemonic(KeyEvent.VK_S);
		lblStatus.setLabelFor(cboStatus);

		txtComplaintId.setToolTipText("Enter the complaint ID to be assigned. Shortcut: Alt+I.");
		txtStaffId.setToolTipText("Enter the employee ID receiving the complaint. Shortcut: Alt+E.");
		cboDepartment.setToolTipText("Select the employee's department. Shortcut: Alt+D.");
		cboStatus.setToolTipText("Select the complaint status. Shortcut: Alt+S.");

		btnAssign = new JButton("Assign");
		btnUpdate = new JButton("Update");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnAssign.setMnemonic(KeyEvent.VK_A);
		btnUpdate.setMnemonic(KeyEvent.VK_U);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnAssign.setToolTipText("Assign the complaint to the employee. Shortcut: Alt+A or Ctrl+Enter.");
		btnUpdate.setToolTipText("Update the selected assignment. Shortcut: Alt+U or Ctrl+U.");
		btnRefresh.setToolTipText("Reload staff assignments. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the assignment fields. Shortcut: Alt+C or Escape.");

		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(
				new Object[] { "Complaint ID", "Employee ID", "Department", "Complaint Status" });

		tblAssignments = new JTable(tableModel);
		tblAssignments.setRowHeight(25);
		tblAssignments.setToolTipText("Select an assignment to view or update.");
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
		formPanel.add(lblStaffId, gbc);

		gbc.gridx = 1;
		formPanel.add(txtStaffId, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(lblDepartment, gbc);

		gbc.gridx = 1;
		formPanel.add(cboDepartment, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(lblStatus, gbc);

		gbc.gridx = 1;
		formPanel.add(cboStatus, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(btnAssign);
		buttonPanel.add(btnUpdate);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblAssignments), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "assign");

		getRootPane().getActionMap().put("assign", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnAssign.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), "update");

		getRootPane().getActionMap().put("update", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnUpdate.doClick();
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

	public void addAssignment(Object[] row) {
		tableModel.addRow(row);
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(StaffAssignmentController controller) {

		btnAssign.addActionListener(e -> controller.save());
		btnUpdate.addActionListener(e -> controller.save());
		btnRefresh.addActionListener(e -> controller.refresh());
		btnClear.addActionListener(e -> clearFields());

	}

	/*
	 * Clear resets the selections rather than calling removeAllItems: the combos
	 * are only filled once when the controller is built, so emptying them here
	 * would leave the screen with nothing to pick until it was reopened.
	 */
	public void clearFields() {
		txtComplaintId.setText("");
		txtStaffId.setText("");

		if (cboDepartment.getItemCount() > 0) {
			cboDepartment.setSelectedIndex(0);
		}

		if (cboStatus.getItemCount() > 0) {
			cboStatus.setSelectedIndex(0);
		}
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public JTextField getTxtComplaintId() {
		return txtComplaintId;
	}

	public JTextField getTxtStaffId() {
		return txtStaffId;
	}

	public JComboBox<String> getCboDepartment() {
		return cboDepartment;
	}

	public JComboBox<String> getCboStatus() {
		return cboStatus;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
}