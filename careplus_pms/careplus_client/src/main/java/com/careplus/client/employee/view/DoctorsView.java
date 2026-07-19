package com.careplus.client.employee.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import com.careplus.client.employee.controller.DoctorsController;
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
		configureKeyboardShortcuts();

		setSize(1000, 550);
		setVisible(true);
	}

	private void initializeComponents() {

		lblTitle = new JLabel("Doctors Directory");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		btnRefresh = new JButton("Refresh");
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnRefresh.setToolTipText("Reload the doctors directory. Shortcut: Alt+R or F5.");

		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(
				new Object[] {
						"Doctor ID",
						"First Name",
						"Last Name",
						"Email",
						"Phone",
						"Department",
						"Hire Date",
						"Specialization",
						"License Number"
				});

		tblDoctors = new JTable(tableModel);
		tblDoctors.setRowHeight(25);
		tblDoctors.setToolTipText("Displays doctor information from the CarePlus system.");
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

	/*
	 * Configure Keyboard Shortcuts
	 */
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

	}

	public void clearTable() {
		tableModel.setRowCount(0);
	}

	public void addDoctor(Object[] row) {
		tableModel.addRow(row);
	}

	/*
	 * Attaches this view's controls to the controller that handles them.
	 */
	public void registerActionListener(DoctorsController controller) {

		btnRefresh.addActionListener(e -> controller.refresh());

	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}
}
