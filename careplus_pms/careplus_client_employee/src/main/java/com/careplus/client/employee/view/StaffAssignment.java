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

public class StaffAssignment extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JLabel lblTitle;
    private JLabel lblComplaintId;
    private JLabel lblStaffId;
    private JLabel lblDepartment;
    private JLabel lblPriority;
    private JLabel lblNotes;

    private JTextField txtComplaintId;
    private JTextField txtStaffId;
    private JComboBox<String> cboDepartment;
    private JComboBox<String> cboPriority;
    private JTextArea txtNotes;

    private JButton btnAssign;
    private JButton btnUpdate;
    private JButton btnRefresh;
    private JButton btnClear;

    private JTable tblAssignments;
    private DefaultTableModel tableModel;

    public StaffAssignment() {
        super("Staff Assignment", true, true, true, true);

        initializeComponents();
        buildGUI();

        setSize(1000, 650);
        setVisible(true);
    }

    private void initializeComponents() {
        lblTitle = new JLabel("Staff Assignment");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        lblComplaintId = new JLabel("Complaint ID");
        lblStaffId = new JLabel("Staff ID");
        lblDepartment = new JLabel("Department");
        lblPriority = new JLabel("Priority");
        lblNotes = new JLabel("Notes");

        txtComplaintId = new JTextField(20);
        txtStaffId = new JTextField(20);

        cboDepartment = new JComboBox<>();
        cboPriority = new JComboBox<>();

        txtNotes = new JTextArea(4, 30);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);

        btnAssign = new JButton("Assign");
        btnUpdate = new JButton("Update");
        btnRefresh = new JButton("Refresh");
        btnClear = new JButton("Clear");

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{
                "Assignment ID",
                "Complaint ID",
                "Staff ID",
                "Department",
                "Priority",
                "Status"
        });

        tblAssignments = new JTable(tableModel);
        tblAssignments.setRowHeight(25);
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
        formPanel.add(lblPriority, gbc);

        gbc.gridx = 1;
        formPanel.add(cboPriority, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(lblNotes, gbc);

        gbc.gridx = 1;
        formPanel.add(new JScrollPane(txtNotes), gbc);

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

    public void clearTable() {
        tableModel.setRowCount(0);
    }

    public void addAssignment(Object[] row) {
        tableModel.addRow(row);
    }

    public void clearFields() {
        txtComplaintId.setText("");
        txtStaffId.setText("");
        txtNotes.setText("");

        cboDepartment.removeAllItems();
        cboPriority.removeAllItems();
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

    public JComboBox<String> getCboPriority() {
        return cboPriority;
    }

    public JTextArea getTxtNotes() {
        return txtNotes;
    }

    public JTable getTblAssignments() {
        return tblAssignments;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnAssign() {
        return btnAssign;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnRefresh() {
        return btnRefresh;
    }

    public JButton getBtnClear() {
        return btnClear;
    }
}