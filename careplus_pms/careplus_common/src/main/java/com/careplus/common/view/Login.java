package com.careplus.common.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel lblTitle;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JLabel lblRole;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cboRole;

    private JButton btnLogin;
    private JButton btnClear;
    private JButton btnExit;

    public Login() {

        super("CarePlus Hospital Login");

        initializeComponents();
        buildGUI();
        registerEvents();

        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void initializeComponents() {

        lblTitle = new JLabel("CarePlus Hospital Management System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        lblUsername = new JLabel("Username");
        lblPassword = new JLabel("Password");
        lblRole = new JLabel("Role");

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);

        cboRole = new JComboBox<>();
        cboRole.addItem("Patient");
        cboRole.addItem("Employee");

        btnLogin = new JButton("Login");
        btnClear = new JButton("Clear");
        btnExit = new JButton("Exit");

        btnLogin.setPreferredSize(new Dimension(100, 35));
        btnClear.setPreferredSize(new Dimension(100, 35));
        btnExit.setPreferredSize(new Dimension(100, 35));

    }

    private void buildGUI() {

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblRole, gbc);

        gbc.gridx = 1;
        formPanel.add(cboRole, gbc);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExit);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(new Color(240, 248, 255));

    }

    private void registerEvents() {

        btnClear.addActionListener(e -> {

            txtUsername.setText("");
            txtPassword.setText("");
            cboRole.setSelectedIndex(0);

        });

        btnExit.addActionListener(e -> System.exit(0));

        btnLogin.addActionListener(e -> {

          

            MainDashboard dashboard = new MainDashboard();
            dashboard.setVisible(true);

            dispose();

        });

    }

  

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JComboBox<String> getCboRole() {
        return cboRole;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnClear() {
        return btnClear;
    }

    public JButton getBtnExit() {
        return btnExit;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new Login().setVisible(true);

        });

    }

}