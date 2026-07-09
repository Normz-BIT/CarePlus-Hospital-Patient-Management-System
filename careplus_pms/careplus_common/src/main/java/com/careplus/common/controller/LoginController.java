package com.careplus.common.controller;

import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.common.view.Login;
import com.careplus.common.view.MainDashboard;

public class LoginController {
    private final Login view;

    public LoginController(Login view) {
        this.view = view;
        init();
    }

    private void init() {
        view.getBtnLogin().addActionListener(e -> login());
    }

    private void login() {
        String username = view.getTxtUsername().getText().trim();
        String password = String.valueOf(view.getTxtPassword().getPassword());
        String role = String.valueOf(view.getCboRole().getSelectedItem());

        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("Username and password are required.");
            return;
        }

        Request request = new Request();
        request.setType(role.equalsIgnoreCase("Patient") ? RequestType.LOGIN_PATIENT : RequestType.LOGIN_EMPLOYEE);
        request.putMap("username", username);
        request.putMap("password", password);
        request.putMap("role", role);

        Response response = new Client().send(request);

        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            MainDashboard dashboard = new MainDashboard(role);
            dashboard.setVisible(true);
            view.dispose();
        } else {
            view.showMessage(response == null ? "No response from server." : response.getMessage());
        }
    }
}

