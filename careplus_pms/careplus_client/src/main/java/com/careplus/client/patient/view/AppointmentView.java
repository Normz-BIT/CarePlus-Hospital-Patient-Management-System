package com.careplus.client.patient.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
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

import com.careplus.client.patient.controller.AppointmentController;
import com.careplus.common.client.view.DateTimePicker;

/**
 * Patient appointment workspace: book a consultation, cancel one, and review
 * upcoming appointments with their date, assigned doctor and status.
 *
 * An MDI child window like every feature screen. The view owns the widgets and
 * exposes helpers such as clearTable and addAppointment, while every decision and
 * server call lives in AppointmentController.
 *
 * The doctor and department combos are filled from the server rather than from
 * local enums, so they are empty until that lookup succeeds. Date and time are
 * plain text fields, which is why the controller has to parse and validate them
 * by hand against a fixed yyyy-MM-dd HH:mm:ss pattern.
 */
public class AppointmentView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblDoctor;
	private JLabel lblDepartment;
	private JLabel lblDate;
	private JLabel lblReason;

	// Fields
	private JComboBox<String> cboDoctor;
	private JComboBox<String> cboDepartment;
	/* Day/Month/Year/Hour/Min spinners, replacing the old typed date and time boxes. */
	private DateTimePicker pickerDate;
	private JTextField txtReason;

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
		configureKeyboardShortcuts();

		setSize(900, 600);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Appointment Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblDoctor = new JLabel("Doctor");
		lblDepartment = new JLabel("Department");
		lblDate = new JLabel("Appointment Date");
		lblReason = new JLabel("Reason");

		cboDoctor = new JComboBox<>();
		cboDepartment = new JComboBox<>();

		// true because appointment_date is a DATETIME, so the time is really saved.
		pickerDate = new DateTimePicker(true);
		txtReason = new JTextField(15);

		lblDoctor.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDoctor.setLabelFor(cboDoctor);

		lblDepartment.setDisplayedMnemonic(KeyEvent.VK_P);
		lblDepartment.setLabelFor(cboDepartment);

		lblDate.setDisplayedMnemonic(KeyEvent.VK_A);
		lblDate.setLabelFor(pickerDate);

		lblReason.setDisplayedMnemonic(KeyEvent.VK_E);
		lblReason.setLabelFor(txtReason);

		cboDoctor.setToolTipText("Select the doctor for the appointment. Shortcut: Alt+D.");
		cboDepartment.setToolTipText("Select the hospital department. Shortcut: Alt+P.");
		pickerDate.setToolTipText("Pick the appointment day, month, year and time. Shortcut: Alt+A.");
		txtReason.setToolTipText("Enter the reason for the appointment. Shortcut: Alt+E.");

		btnSchedule = new JButton("Schedule");
		btnUpdate = new JButton("Update");
		btnCancel = new JButton("Cancel");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnSchedule.setMnemonic(KeyEvent.VK_S);
		btnUpdate.setMnemonic(KeyEvent.VK_U);
		btnCancel.setMnemonic(KeyEvent.VK_C);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_L);

		btnSchedule.setToolTipText("Schedule the appointment. Shortcut: Alt+S or Ctrl+Enter.");
		btnUpdate.setToolTipText("Update the selected appointment. Shortcut: Alt+U or Ctrl+U.");
		btnCancel.setToolTipText("Cancel the selected appointment. Shortcut: Alt+C or Delete.");
		btnRefresh.setToolTipText("Reload appointments. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the appointment fields. Shortcut: Alt+L or Escape.");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(
				new Object[] { "Appointment ID", "Date", "Reason", "Status" });

		tblAppointments = new JTable(tableModel);
		tblAppointments.setRowHeight(25);
		tblAppointments.setToolTipText("Select an appointment to update or cancel.");

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

		/*
		 * One row now instead of two, since the picker carries its own Day/Month/Year/
		 * Hour/Min labels and covers what the separate time box used to do.
		 */
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(lblDate, gbc);

		gbc.gridx = 1;
		formPanel.add(pickerDate, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		formPanel.add(lblReason, gbc);

		gbc.gridx = 1;
		formPanel.add(txtReason, gbc);

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

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "schedule");

		getRootPane().getActionMap().put("schedule", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnSchedule.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), "update");

		getRootPane().getActionMap().put("update", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnUpdate.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "cancel");

		getRootPane().getActionMap().put("cancel", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnCancel.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "refresh");

		getRootPane().getActionMap().put("refresh", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnRefresh.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear");

		getRootPane().getActionMap().put("clear", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnClear.doClick();
			}
		});

	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addAppointment(Object[] row) {
		tableModel.addRow(row);
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(AppointmentController controller) {

		btnSchedule.addActionListener(e -> controller.schedule());
		btnUpdate.addActionListener(e -> controller.schedule());
		btnCancel.addActionListener(e -> controller.cancel());
		btnRefresh.addActionListener(e -> controller.refresh());
		btnClear.addActionListener(e -> clearFields());

	}

	public void clearFields() {

		cboDoctor.removeAllItems();
		cboDepartment.removeAllItems();

		pickerDate.reset();
		txtReason.setText("");

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

	public DateTimePicker getPickerDate() {
		return pickerDate;
	}

	public JTextField getTxtReason() {
		return txtReason;
	}

	public JTable getTblAppointments() {
		return tblAppointments;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}