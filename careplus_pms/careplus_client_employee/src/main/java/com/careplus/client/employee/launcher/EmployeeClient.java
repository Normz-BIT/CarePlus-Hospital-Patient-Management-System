package com.careplus.client.employee.launcher;

import javax.swing.SwingUtilities;

import com.careplus.common.controller.LoginController;
import com.careplus.common.view.Login;

public class EmployeeClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.getCboRole().setSelectedItem("Employee");
            new LoginController(login);
            login.setVisible(true);
        });
    }
}
