package com.careplus.common.client.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.DashboardFeature;
import com.careplus.common.client.view.LoginView;
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
	private final LoginView view;
	private final List<DashboardFeature> features;
	private static final Logger logger = LogManager.getLogger(LoginController.class);

	public LoginController(LoginView view, List<DashboardFeature> features) {
		this.view = view;
		this.features = features;
	}

	/*
	 * Authenticate User
	 */
	public void login() {

		/*
		 * We trim the ID because people type them by hand and a stray space would go to
		 * the server and fail the lookup. We deliberately don't trim the password.
		 *
		 * JPasswordField hands back a char[] on purpose so the password doesn't sit
		 * around in the String pool. Turning it into a String here throws that away,
		 * though it goes to the server as plain text anyway.
		 */
		String id = view.getTxtId().getText().trim();
		String password = String.valueOf(view.getTxtPassword().getPassword());

		/*
		 * Caught here so an obviously empty form doesn't cost a round trip to the
		 * server.
		 * the server checks properly on its own regardless.
		 */
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

			/*
			 * This blocks on the Swing thread, so the login window freezes until the
			 * server answers. Fine for one request at startup, but see the note on
			 * Client.send.
			 */
			Response response = Client.send(request);

		
			if (response != null && Boolean.TRUE.equals(response.getSuccess())) {

				/*
				 * The concrete subclass arrives here, not a bare Person, and its role is what
				 * MainDashboard filters the menu by. Person is abstract, so a successful
				 * response always carries a Patient, Doctor, Nurse or Receptionist.
				 */
				Person user = (Person) response.getData();

				/*
				 * The dashboard is shown before the login window is disposed. Disposing first
				 * would briefly leave no displayable window.
				 */
				MainDashboard dashboard = new MainDashboard(user, features);
				
				
				//pass the icons from the login to the all dashboard view
				dashboard.setIcons(view.getIconImages());
				
				dashboard.setVisible(true);
				view.dispose();
				
				logger.info(
						"Login successful for user ID: {} with role: {}",
						user.getPersonId(),
						user.getRole());
				
			} else {
				/*
				 * The server returns one identical message for a wrong password and for an
				 * unknown ID.
				 */
				view.showMessage(response == null ? "No response from server." : response.getMessage());

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

		
			logger.error("An error occurred while logging in user ID: " + id, e);
			
			view.showMessage("Unable to complete login: " + e.getMessage());
		}
	}
	
	
	
	
}