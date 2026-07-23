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
		 * Trimmed because IDs are typed by hand and trailing whitespace would otherwise
		 * reach the server and fail the lookup. The password is deliberately not
		 * trimmed, since leading or trailing spaces are legitimate characters in one.
		 *
		 * JPasswordField returns char[] specifically so a password need not linger in
		 * the String pool. Converting it here gives that protection up, and the value
		 * then travels to the server as plaintext anyway.
		 */
		String id = view.getTxtId().getText().trim();
		String password = String.valueOf(view.getTxtPassword().getPassword());

		/*
		 * Rejected locally so an obviously incomplete form never costs a network round
		 * trip. This is a convenience check, not a security boundary: the server
		 * validates independently.
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
			 * Blocking call on the Event Dispatch Thread, so the login window is frozen
			 * until the server answers. Acceptable for a single request at startup, but
			 * see the threading note on Client.send.
			 */
			Response response = Client.send(request);

			/*
			 * getSuccess returns a boxed Boolean that is null for any request the server
			 * does not handle, so this unboxing throws a NullPointerException rather than
			 * evaluating false. The generic catch below turns that into a user facing
			 * message, which is why an unhandled response still fails safely instead of
			 * letting anyone in.
			 */
			if (response != null && response.getSuccess()) {

				/*
				 * The concrete subclass arrives here, not a bare Person, and its role is what
				 * MainDashboard filters the menu by. Person is abstract, so a successful
				 * response always carries a Patient, Doctor, Nurse or Receptionist.
				 */
				Person user = (Person) response.getData();

				/*
				 * The dashboard is shown before the login window is disposed. Disposing first
				 * would briefly leave no displayable window, which lets Swing shut the JVM
				 * down when the last one closes.
				 */
				MainDashboard dashboard = new MainDashboard(user, features);
				
				
				//pass the icons from the login to the all dashboard view
				dashboard.setIcons(view.getIconImages());
				
				dashboard.setVisible(true);
				view.dispose();
				
				//TODO log4j2
				logger.info(
						"Login successful for user ID: {} with role: {}",
						user.getPersonId(),
						user.getRole());
				
			} else {
				/*
				 * The server returns one identical message for a wrong password and for an
				 * unknown ID, so relaying it verbatim preserves that deliberate ambiguity and
				 * avoids revealing which patient or staff IDs exist.
				 */
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