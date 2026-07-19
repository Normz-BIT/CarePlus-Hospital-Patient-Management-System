package com.careplus.client.patient.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

import com.careplus.client.patient.controller.MedicalRecordController;

public class MedicalRecordView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblDiagnosis;
	private JLabel lblTreatment;
	private JLabel lblFollowUpDate;
	private JLabel lblCreatedDate;

	// Fields
	private JTextField txtDiagnosis;
	private JTextArea txtTreatment;
	private JTextField txtFollowUpDate;
	private JTextField txtCreatedDate;

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
		configureKeyboardShortcuts();

		setSize(950, 600);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Medical Records");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblDiagnosis = new JLabel("Diagnosis");
		lblTreatment = new JLabel("Treatment Note");
		lblFollowUpDate = new JLabel("Follow-up Date");
		lblCreatedDate = new JLabel("Created Date");

		txtDiagnosis = new JTextField(30);
		txtDiagnosis.setEditable(false);

		txtTreatment = new JTextArea(4, 30);
		txtTreatment.setEditable(false);
		txtTreatment.setLineWrap(true);
		txtTreatment.setWrapStyleWord(true);

		txtFollowUpDate = new JTextField(20);
		txtFollowUpDate.setEditable(false);

		txtCreatedDate = new JTextField(30);
		txtCreatedDate.setEditable(false);

		lblDiagnosis.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDiagnosis.setLabelFor(txtDiagnosis);

		lblTreatment.setDisplayedMnemonic(KeyEvent.VK_T);
		lblTreatment.setLabelFor(txtTreatment);

		lblFollowUpDate.setDisplayedMnemonic(KeyEvent.VK_F);
		lblFollowUpDate.setLabelFor(txtFollowUpDate);

		lblCreatedDate.setDisplayedMnemonic(KeyEvent.VK_A);
		lblCreatedDate.setLabelFor(txtCreatedDate);

		txtDiagnosis.setToolTipText("Displays the medical diagnosis. Shortcut: Alt+D.");
		txtTreatment.setToolTipText("Displays the doctor's treatment note. Shortcut: Alt+T.");
		txtFollowUpDate.setToolTipText("Displays the scheduled follow-up date. Shortcut: Alt+F.");
		txtCreatedDate.setToolTipText("Displays the date the medical record was created.");

		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnRefresh.setToolTipText("Reload medical records. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the displayed medical-record fields. Shortcut: Alt+C or Escape.");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(
				new Object[] { "Record ID", "Diagnosis", "Treatment Note", "Follow-up Date", "Created Date" });

		tblMedicalRecords = new JTable(tableModel);
		tblMedicalRecords.setRowHeight(25);
		tblMedicalRecords.setToolTipText("Displays the patient's medical-record history.");

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
		formPanel.add(lblFollowUpDate, gbc);

		gbc.gridx = 1;
		formPanel.add(txtFollowUpDate, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(lblCreatedDate, gbc);

		gbc.gridx = 1;
		formPanel.add(txtCreatedDate, gbc);

		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(btnRefresh);
		buttonPanel.add(btnClear);

		mainPanel.add(formPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(tblMedicalRecords), BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(mainPanel);

	}

	private void configureKeyboardShortcuts() {

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

	public void addMedicalRecord(Object[] row) {
		tableModel.addRow(row);
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(MedicalRecordController controller) {

		btnRefresh.addActionListener(e -> controller.refresh());
		btnClear.addActionListener(e -> clearFields());

		tblMedicalRecords.getSelectionModel()
				.addListSelectionListener(e -> controller.displaySelectedRecord());

	}

	public void clearFields() {

		txtDiagnosis.setText("");
		txtTreatment.setText("");
		txtFollowUpDate.setText("");
		txtCreatedDate.setText("");

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

	public JTextField getTxtFollowUpDate() {
		return txtFollowUpDate;
	}

	public JTextField getTxtCreatedDate() {
		return txtCreatedDate;
	}

	public JTable getTblMedicalRecords() {
		return tblMedicalRecords;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}
