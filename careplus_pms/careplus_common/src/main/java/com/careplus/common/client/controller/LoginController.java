package com.careplus.common.client.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.DashboardFeature;
import com.careplus.common.client.view.Login;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.Person;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Login Controller
 * Authenticates patients and employees
 * Opens the dashboard with the correct features
 */
public class LoginController {
	private final Login view;
	private final List<DashboardFeature> features;
	private static final Logger logger = LogManager.getLogger(LoginController.class);

	public LoginController(Login view, List<DashboardFeature> features) {
		this.view = view;
		this.features = features;
		init();
	}

	/*
	 * Initialize Login Events
	 */
	private void init() {
		view.getBtnLogin().addActionListener(e -> login());
	}

	/*
	 * Authenticate User
	 */
	private void login() {
		
		String id = view.getTxtId().getText().trim();
		String password = String.valueOf(view.getTxtPassword().getPassword());

		if (id.isEmpty() || password.isEmpty()) {
			view.showMessage("ID and password are required.");
			logger.warn("Login rejected because the ID or password was empty");

			return;
		}

		// the server looks up the person by id, verifies the
		// password, and returns the concrete Patient/Doctor/Nurse/Receptionist
		// so the dashboard can show the right menus.
		Request request = new Request();

		request.setType(RequestType.LOGIN);
		request.putMap("id", id);
		request.putMap("password", password);

		try {

			logger.info("Login request submitted for user ID: {}", id);

			Response response = Client.send(request);

			if (response != null && response.getSuccess()) {
				
				Person user = (Person) response.getData();
				
				MainDashboard dashboard = new MainDashboard(user, features);
				dashboard.setVisible(true);
				view.dispose();
				
				//TODO log4j2
				logger.info(
						"Login successful for user ID: {} with role: {}",
						user.getPersonId(),
						user.getRole());
				
			} else {
				view.showMessage(response == null ? "No response from server." : response.getMessage());

			     //TODO log4j2
				if (response == null) {
					logger.error("No response received from server during login");
				} else {
					logger.warn(
							"Login failed for user ID: {}. Reason: {}",
							id,
							response.getMessage());
				}
			}

		} catch (Exception e) {

			// TODO
			logger.error("An error occurred while logging in user ID: " + id, e);
			view.showMessage("Unable to complete login: " + e.getMessage());
		}
	}
	
	
	
	
}