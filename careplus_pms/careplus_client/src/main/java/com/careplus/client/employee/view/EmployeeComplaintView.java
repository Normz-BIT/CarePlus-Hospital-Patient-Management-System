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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public class EmployeeComplaintView extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	// Labels
	private JLabel lblTitle;
	private JLabel lblComplaintId;
	private JLabel lblCategory;
	private JLabel lblStatus;
	private JLabel lblRemarks;
	private JLabel lblParentId;
	private JLabel lblDescription;
	private JLabel lblDateSubmitted;
	private JLabel lblResponseDate;

	// Components
	private JTextField txtComplaintId;
	private JComboBox<String> cboCategory;
	private JComboBox<String> cboPriority;
	private JComboBox<String> cboStatus;
	private JTextArea txtRemarks;
	private JTextField txtParentId;
	private JTextArea txtDescription;
	private JTextField txtDateSubmitted;
	private JTextField txtResponseDate;

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

	public EmployeeComplaintView() {

		super("Complaint Manager", true, true, true, true);

		initializeComponents();
		buildGUI();
		configureKeyboardShortcuts();

		setSize(1050, 700);
		setVisible(true);

	}

	private void initializeComponents() {

		lblTitle = new JLabel("Complaint Management");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

		lblComplaintId = new JLabel("Complaint ID");
		lblCategory = new JLabel("Category");
		lblStatus = new JLabel("Status");
		lblRemarks = new JLabel("Response");
		lblParentId = new JLabel("Parent Complaint ID");
		lblDescription = new JLabel("Description");
		lblDateSubmitted = new JLabel("Date Submitted");
		lblResponseDate = new JLabel("Response Date");

		txtComplaintId = new JTextField(20);
		txtComplaintId.setEditable(false);

		cboCategory = new JComboBox<>();
		cboPriority = new JComboBox<>();
		cboStatus = new JComboBox<>();

		txtRemarks = new JTextArea(4, 30);
		txtRemarks.setLineWrap(true);
		txtRemarks.setWrapStyleWord(true);

		txtParentId = new JTextField(20);
		txtParentId.setEditable(false);

		txtDescription = new JTextArea(3, 30);
		txtDescription.setEditable(false);
		txtDescription.setLineWrap(true);
		txtDescription.setWrapStyleWord(true);

		txtDateSubmitted = new JTextField(20);
		txtDateSubmitted.setEditable(false);

		txtResponseDate = new JTextField(20);
		txtResponseDate.setEditable(false);

		lblComplaintId.setDisplayedMnemonic(KeyEvent.VK_I);
		lblComplaintId.setLabelFor(txtComplaintId);

		lblCategory.setDisplayedMnemonic(KeyEvent.VK_G);
		lblCategory.setLabelFor(cboCategory);

		lblStatus.setDisplayedMnemonic(KeyEvent.VK_S);
		lblStatus.setLabelFor(cboStatus);

		lblRemarks.setDisplayedMnemonic(KeyEvent.VK_O);
		lblRemarks.setLabelFor(txtRemarks);

		lblParentId.setDisplayedMnemonic(KeyEvent.VK_P);
		lblParentId.setLabelFor(txtParentId);

		lblDescription.setDisplayedMnemonic(KeyEvent.VK_D);
		lblDescription.setLabelFor(txtDescription);

		lblDateSubmitted.setDisplayedMnemonic(KeyEvent.VK_A);
		lblDateSubmitted.setLabelFor(txtDateSubmitted);

		lblResponseDate.setDisplayedMnemonic(KeyEvent.VK_E);
		lblResponseDate.setLabelFor(txtResponseDate);

		txtComplaintId.setToolTipText("Displays the selected complaint ID.");
		cboCategory.setToolTipText("Displays the complaint category. Shortcut: Alt+G.");
		cboStatus.setToolTipText("Select the complaint status. Shortcut: Alt+S.");
		txtRemarks.setToolTipText("Enter the employee response. Shortcut: Alt+O.");
		txtParentId.setToolTipText("Displays the ID of the original complaint.");
		txtDescription.setToolTipText("Displays the patient's complaint description.");
		txtDateSubmitted.setToolTipText("Displays the date the complaint was submitted.");
		txtResponseDate.setToolTipText("Displays the date the response was submitted.");

		btnAssign = new JButton("Assign");
		btnResolve = new JButton("Resolve");
		btnRefresh = new JButton("Refresh");
		btnClear = new JButton("Clear");

		btnAssign.setMnemonic(KeyEvent.VK_A);
		btnResolve.setMnemonic(KeyEvent.VK_V);
		btnRefresh.setMnemonic(KeyEvent.VK_R);
		btnClear.setMnemonic(KeyEvent.VK_C);

		btnAssign.setToolTipText("Assign the complaint. Shortcut: Alt+A or Ctrl+A.");
		btnResolve.setToolTipText("Resolve the selected complaint. Shortcut: Alt+V or Ctrl+Enter.");
		btnRefresh.setToolTipText("Reload complaints. Shortcut: Alt+R or F5.");
		btnClear.setToolTipText("Clear the complaint fields. Shortcut: Alt+C or Escape.");

		tableModel = new DefaultTableModel();

		tableModel.setColumnIdentifiers(
				new Object[] {
						"Complaint ID",
						"Parent Complaint ID",
						"Category",
						"Description",
						"Date Submitted",
						"Response",
						"Response Date",
						"Status",
						"Patient"
				});

		tblComplaints = new JTable(tableModel);
		tblComplaints.setRowHeight(25);
		tblComplaints.setToolTipText("Select a complaint to assign or resolve.");

		lblSummary = new JLabel(" ");
		cboFilter = new JComboBox<>();
		btnSearch = new JButton("Search");

		cboFilter.setToolTipText("Select a complaint category to filter.");
		btnSearch.setMnemonic(KeyEvent.VK_H);
		btnSearch.setToolTipText("Search complaints by category. Shortcut: Alt+H or Ctrl+F.");

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
		formPanel.add(lblParentId, gbc);

		gbc.gridx = 1;
		formPanel.add(txtParentId, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(lblCategory, gbc);

		gbc.gridx = 1;
		formPanel.add(cboCategory, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(lblStatus, gbc);

		gbc.gridx = 1;
		formPanel.add(cboStatus, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formPanel.add(lblDescription, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtDescription), gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		formPanel.add(lblDateSubmitted, gbc);

		gbc.gridx = 1;
		formPanel.add(txtDateSubmitted, gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		formPanel.add(lblRemarks, gbc);

		gbc.gridx = 1;
		formPanel.add(new JScrollPane(txtRemarks), gbc);

		gbc.gridx = 0;
		gbc.gridy = 8;
		formPanel.add(lblResponseDate, gbc);

		gbc.gridx = 1;
		formPanel.add(txtResponseDate, gbc);

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

	private void configureKeyboardShortcuts() {

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "assign");

		getRootPane().getActionMap().put("assign", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnAssign.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "resolve");

		getRootPane().getActionMap().put("resolve", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnResolve.doClick();
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "search");

		getRootPane().getActionMap().put("search", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btnSearch.doClick();
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

	public void addComplaint(Object[] row) {
		tableModel.addRow(row);
	}

	public void clearFields() {

		txtComplaintId.setText("");
		txtRemarks.setText("");
		txtParentId.setText("");
		txtDescription.setText("");
		txtDateSubmitted.setText("");
		txtResponseDate.setText("");

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

	public JTextField getTxtParentId() {
		return txtParentId;
	}

	public JTextArea getTxtDescription() {
		return txtDescription;
	}

	public JTextField getTxtDateSubmitted() {
		return txtDateSubmitted;
	}

	public JTextField getTxtResponseDate() {
		return txtResponseDate;
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