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

import com.careplus.client.employee.controller.VitalsController;
/**
 * Nurse workspace: view assigned cases and record vital signs, patient
 * observations, and nursing notes.
 *
 * Observations and nursing notes are kept as two separate fields rather than one
 * free text box. Observations record what the nurse saw, nursing notes record
 * the care that was given, and keeping them apart preserves that distinction for
 * whoever reads the record later.
 *
 * The form sits above the table so a nurse enters a reading and immediately sees
 * it join the history below, which is the order the task is actually performed
 * in.
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
		configureKeyboardShortcuts();

		setSize(1000, 700);
		setVisible(true);
	}

	private void initializeComponents() {

		lblTitle = new JLabel("Vital Signs & Nursing Notes");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblPatientId = new JLabel("Patient ID");
		lblTemperature = new JLabel("Temperature (°C)");
		lblBloodPressure = new JLabel("Blood Pressure");
		lblPulse = new JLabel("Heart Rate (bpm)");
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

		lblPatientId.setDisplayedMnemonic(KeyEvent.VK_P);
		lblPatientId.setLabelFor(txtPatientId);

		lblTemperature.setDisplayedMnemonic(KeyEvent.VK_T);
		lblTemperature.setLabelFor(txtTemperature);

		lblBloodPressure.setDisplayedMnemonic(KeyEvent.VK_B);
		lblBloodPressure.setLabelFor(txtBloodPressure);

		lblPulse.setDisplayedMnemonic(KeyEvent.VK_H);
		lblPulse.setLabelFor(txtPulse);

		lblRespiratory.setDisplayedMnemonic(KeyEvent.VK_R);
		lblRespiratory.setLabelFor(txtRespiratory);

		lblObservations.setDisplayedMnemonic(KeyEvent.VK_O);
		lblObservations.setLabelFor(txtObservations);

		lblNursingNotes.setDisplayedMnemonic(KeyEvent.VK_N);
		lblNursingNotes.setLabelFor(txtNursingNotes);

		txtPatientId.setToolTipText("Enter the patient's ID. Shortcut: Alt+P.");
		txtTemperature.setToolTipText("Enter the patient's temperature in degrees Celsius. Shortcut: Alt+T.");
		txtBloodPressure.setToolTipText("Enter the blood pressure, for example 120/80. Shortcut: Alt+B.");
		txtPulse.setToolTipText("Enter the patient's heart rate in beats per minute. Shortcut: Alt+H.");
		txtRespiratory.setToolTipText("Enter the respiratory rate. Shortcut: Alt+R.");
		txtObservations.setToolTipText("Enter observations about the patient's condition. Shortcut: Alt+O.");
		txtNursingNotes.setToolTipText("Enter additional nursing notes. Shortcut: Alt+N.");

		btnRecord = new JButton("Record");
		btnRefresh = new JButton("Refresh Cases");
		btnClear = new JButton("Clear");

		btnRecord.setMnemonic(KeyEvent.VK_E);
		btnRefresh.setMnemonic(KeyEvent.VK_F);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnRecord.setToolTipText("Record the patient's vital signs. Shortcut: Alt+E or Ctrl+Enter.");
		btnRefresh.setToolTipText("Reload vital-sign records. Shortcut: Alt+F or F5.");
		btnClear.setToolTipText("Clear the vital-sign fields. Shortcut: Alt+C or Escape.");

		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(
				new Object[] {
						"Vital ID",
						"Temperature",
						"Blood Pressure",
						"Heart Rate",
						"Respiratory Rate",
						"Observations",
						"Nursing Notes",
						"Recorded At"
				});

		tblCases = new JTable(tableModel);
		tblCases.setRowHeight(25);
		tblCases.setToolTipText("Displays recorded patient vital signs.");
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

	/*
	 * Configure Keyboard Shortcuts
	 */
	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "record");

		getRootPane().getActionMap().put("record", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnRecord.doClick();
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

	public void addCase(Object[] row) {
		tableModel.addRow(row);
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(VitalsController controller) {

		btnRecord.addActionListener(e -> controller.record());
		btnRefresh.addActionListener(e -> controller.refresh());
		btnClear.addActionListener(e -> clearFields());

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

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
}
