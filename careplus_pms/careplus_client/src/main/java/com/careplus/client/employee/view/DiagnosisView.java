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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import com.careplus.client.employee.controller.DiagnosisController;

/**
 * Doctor's clinical workspace: record a diagnosis and treatment note against a
 * patient, and schedule a follow up.
 *
 * Offers both a create and an update action, which is why the table selection
 * matters here: an update needs the record ID of the highlighted row, while a
 * create does not.
 *
 * The follow up date field takes a date only value, unlike the appointment
 * screens which expect a full date and time. That difference lives in
 * DiagnosisController's parsing and is not signalled by the form itself.
 */
public class DiagnosisView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblPatientId;
	private JLabel lblDiagnosis;
	private JLabel lblTreatment;
	private JLabel lblFollowUpDate;

	// Components
	private JTextField txtPatientId;
	private JTextField txtDiagnosis;
	private JTextArea txtTreatment;
	private JTextField txtFollowUpDate;

	// Buttons
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnRefresh;
	private JButton btnClear;

	// Table
	private JTable tblDiagnosis;
	private DefaultTableModel tableModel;

	public DiagnosisView() {

		super("Medical Record Management", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(1000, 650);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Medical Record Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblPatientId = new JLabel("Patient ID");
		lblDiagnosis = new JLabel("Diagnosis");
		lblTreatment = new JLabel("Treatment Note");
		lblFollowUpDate = new JLabel("Follow-up Date (yyyy-MM-dd)");

		txtPatientId = new JTextField(20);
		txtDiagnosis = new JTextField(20);

		txtTreatment = new JTextArea(4, 30);
		txtTreatment.setLineWrap(true);
		txtTreatment.setWrapStyleWord(true);

		txtFollowUpDate = new JTextField(20);

		lblPatientId.setDisplayedMnemonic(KeyEvent.VK_P);
		lblPatientId.setLabelFor(txtPatientId);

		lblDiagnosis.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDiagnosis.setLabelFor(txtDiagnosis);

		lblTreatment.setDisplayedMnemonic(KeyEvent.VK_T);
		lblTreatment.setLabelFor(txtTreatment);

		lblFollowUpDate.setDisplayedMnemonic(KeyEvent.VK_F);
		lblFollowUpDate.setLabelFor(txtFollowUpDate);

		txtPatientId.setToolTipText("Enter the patient's unique ID. Shortcut: Alt+P.");
		txtDiagnosis.setToolTipText("Enter the patient's diagnosis. Shortcut: Alt+D.");
		txtTreatment.setToolTipText("Enter the doctor's treatment note. Shortcut: Alt+T.");
		txtFollowUpDate.setToolTipText("Enter the follow-up date using yyyy-MM-dd. Shortcut: Alt+F.");

		btnSave = new JButton("Save");
		btnUpdate = new JButton("Update");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnSave.setMnemonic(KeyEvent.VK_S);
		btnUpdate.setMnemonic(KeyEvent.VK_U);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnSave.setToolTipText("Save the medical record. Shortcut: Alt+S or Ctrl+Enter.");
		btnUpdate.setToolTipText("Update the selected medical record. Shortcut: Alt+U or Ctrl+U.");
		btnRefresh.setToolTipText("Reload medical records. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the medical-record fields. Shortcut: Alt+C or Escape.");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(
				new Object[] {
						"Record ID",
						"Diagnosis",
						"Treatment Note",
						"Follow-up Date",
						"Created Date"
				});

		tblDiagnosis = new JTable(tableModel);
		tblDiagnosis.setRowHeight(25);
		tblDiagnosis.setToolTipText("Select a medical record to view or update.");

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
		formPanel.add(lblFollowUpDate, gbc);

		gbc.gridx = 1;
		formPanel.add(txtFollowUpDate, gbc);

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

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "save");

		getRootPane().getActionMap().put("save", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnSave.doClick();
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

	public void addDiagnosis(Object[] row) {
		tableModel.addRow(row);
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(DiagnosisController controller) {

		btnSave.addActionListener(e -> controller.saveNew());
		btnUpdate.addActionListener(e -> controller.saveUpdate());
		btnRefresh.addActionListener(e -> controller.refresh());
		btnClear.addActionListener(e -> clearFields());

	}

	public void clearFields() {

		txtPatientId.setText("");
		txtDiagnosis.setText("");
		txtTreatment.setText("");
		txtFollowUpDate.setText("");

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

	public JTextField getTxtFollowUpDate() {
		return txtFollowUpDate;
	}

	public JTable getTblDiagnosis() {
		return tblDiagnosis;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}
