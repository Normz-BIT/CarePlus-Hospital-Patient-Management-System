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

import com.careplus.client.employee.controller.PatientsController;
/**
 * Doctor's assigned-patients directory: lists patient id, name, contact,
 * complaint and history, and lets the doctor schedule a follow-up.
 *
 * The only screen offering two ways to identify a patient: a typed ID field and
 * the table selection. The typed value wins, with the selection used as a
 * fallback, so a doctor need not retype an ID for a patient already highlighted.
 *
 * Follow up date and time are free text fields parsed against a fixed
 * yyyy-MM-dd HH:mm:ss pattern, the same convention AppointmentView uses.
 */
public class PatientsView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblTitle;
	private JLabel lblPatientId;
	private JLabel lblDate;
	private JLabel lblTime;
	private JLabel lblReason;

	private JTextField txtPatientId;
	private JTextField txtDate;
	private JTextField txtTime;
	private JTextField txtReason;

	private JButton btnFollowUp;
	private JButton btnRefresh;
	private JButton btnClear;

	private JTable tblPatients;
	private DefaultTableModel tableModel;

	public PatientsView() {

		super("My Patients", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(1050, 650);
		setVisible(true);
	}

	private void initializeComponents() {

		lblTitle = new JLabel("Assigned Patients");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblPatientId = new JLabel("Patient ID");
		lblDate = new JLabel("Follow-up Date");
		lblTime = new JLabel("Follow-up Time");
		lblReason = new JLabel("Follow-up Reason");

		txtPatientId = new JTextField(20);
		txtDate = new JTextField(20);
		txtTime = new JTextField(20);
		txtReason = new JTextField(20);

		lblPatientId.setDisplayedMnemonic(KeyEvent.VK_P);
		lblPatientId.setLabelFor(txtPatientId);

		lblDate.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDate.setLabelFor(txtDate);

		lblTime.setDisplayedMnemonic(KeyEvent.VK_T);
		lblTime.setLabelFor(txtTime);

		lblReason.setDisplayedMnemonic(KeyEvent.VK_E);
		lblReason.setLabelFor(txtReason);

		txtPatientId.setToolTipText("Enter or select the patient's ID. Shortcut: Alt+P.");
		txtDate.setToolTipText("Enter the follow-up date using yyyy-MM-dd. Shortcut: Alt+D.");
		txtTime.setToolTipText("Enter the follow-up time using HH:mm. Shortcut: Alt+T.");
		txtReason.setToolTipText("Enter the reason for the follow-up appointment. Shortcut: Alt+E.");

		btnFollowUp = new JButton("Schedule Follow-up");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnFollowUp.setMnemonic(KeyEvent.VK_F);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnFollowUp.setToolTipText("Schedule a follow-up appointment. Shortcut: Alt+F or Ctrl+Enter.");
		btnRefresh.setToolTipText("Reload assigned patients. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the follow-up fields. Shortcut: Alt+C or Escape.");

		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(
				new Object[] {
						"Patient ID",
						"First Name",
						"Last Name",
						"Email",
						"Phone",
						"Date of Birth",
						"Gender",
						"Address",
						"Medical History"
				});

		tblPatients = new JTable(tableModel);
		tblPatients.setRowHeight(25);
		tblPatients.setToolTipText("Select a patient to schedule a follow-up appointment.");
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

		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(lblReason, gbc);
		gbc.gridx = 1;
		formPanel.add(txtReason, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(btnFollowUp);
		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblPatients), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "followUp");

		getRootPane().getActionMap().put("followUp", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnFollowUp.doClick();
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

	public void addPatient(Object[] row) {
		tableModel.addRow(row);
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(PatientsController controller) {

		btnFollowUp.addActionListener(e -> controller.scheduleFollowUp());
		btnRefresh.addActionListener(e -> controller.refresh());
		btnClear.addActionListener(e -> clearFields());

	}

	public void clearFields() {
		txtPatientId.setText("");
		txtDate.setText("");
		txtTime.setText("");
		txtReason.setText("");
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

	public JTextField getTxtReason() {
		return txtReason;
	}

	public JTable getTblPatients() {
		return tblPatients;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
}
