package com.careplus.client.patient.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Payment extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    // Labels
    private JLabel lblTitle;
    private JLabel lblPaymentId;
    private JLabel lblAmount;
    private JLabel lblMethod;
    private JLabel lblStatus;

    // Fields
    private JTextField txtPaymentId;
    private JTextField txtAmount;
    private JTextField txtMethod;
    private JTextField txtStatus;

    // Buttons
    private JButton btnPay;
    private JButton btnRefresh;
    private JButton btnClear;

    // Table
    private JTable tblPayments;
    private DefaultTableModel tableModel;

    public Payment() {

        super("Payments", true, true, true, true);

        initializeComponents();
        buildGUI();

        setSize(900, 600);
        setVisible(true);

    }

    private void initializeComponents() {

        lblTitle = new JLabel("Payment Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        lblPaymentId = new JLabel("Payment ID");
        lblAmount = new JLabel("Amount");
        lblMethod = new JLabel("Payment Method");
        lblStatus = new JLabel("Status");

        txtPaymentId = new JTextField(20);
        txtAmount = new JTextField(20);
        txtMethod = new JTextField(20);
        txtStatus = new JTextField(20);

        btnPay = new JButton("Make Payment");
        btnRefresh = new JButton("Refresh");
        btnClear = new JButton("Clear");

        tableModel = new DefaultTableModel();

        tableModel.setColumnIdentifiers(new Object[]{
                "Payment ID",
                "Amount",
                "Method",
                "Status",
                "Date"
        });

        tblPayments = new JTable(tableModel);
        tblPayments.setRowHeight(25);

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
        formPanel.add(lblPaymentId, gbc);

        gbc.gridx = 1;
        formPanel.add(txtPaymentId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblAmount, gbc);

        gbc.gridx = 1;
        formPanel.add(txtAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblMethod, gbc);

        gbc.gridx = 1;
        formPanel.add(txtMethod, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblStatus, gbc);

        gbc.gridx = 1;
        formPanel.add(txtStatus, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(btnPay);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClear);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tblPayments), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

    }

  

    public void clearTable() {
        tableModel.setRowCount(0);
    }

    public void addPayment(Object[] row) {
        tableModel.addRow(row);
    }

    public void clearFields() {

        txtPaymentId.setText("");
        txtAmount.setText("");
        txtMethod.setText("");
        txtStatus.setText("");

    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

 

    public JTextField getTxtPaymentId() {
        return txtPaymentId;
    }

    public JTextField getTxtAmount() {
        return txtAmount;
    }

    public JTextField getTxtMethod() {
        return txtMethod;
    }

    public JTextField getTxtStatus() {
        return txtStatus;
    }

    public JTable getTblPayments() {
        return tblPayments;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnPay() {
        return btnPay;
    }

    public JButton getBtnRefresh() {
        return btnRefresh;
    }

    public JButton getBtnClear() {
        return btnClear;
    }

}
