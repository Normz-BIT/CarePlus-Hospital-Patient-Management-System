package com.careplus.client.employee.controller;


import com.careplus.common.client.net.Client;
import com.careplus.common.enums.UserRole;
import com.careplus.common.model.Doctor;
import com.careplus.common.model.Employee;
import com.careplus.common.model.Nurse;
import com.careplus.common.model.Receptionist;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class MainController {

	private Client client;
	private EmployeeController current; // active role controller
	private UserRole role;

	
	 public MainController() {

	
		client = new Client();
		this.login();
		// mainVeiw.showLogin

	}

	public void login() {

		current = null;

	}

	public void logout() {

		login();
		// mainVeiw.showLogin
	}

	public void handleLogin(String email, String password) {

		if (client == null || !client.isConnected()) {
			// mainView.showError("Not connected to server.");
			return;
		}

		// create employee login request
		Request req = new Request();
		req.putMap("email", email);
		req.putMap("password", password);
		;
		req.setType(RequestType.LOGIN_EMPLOYEE);

		Response response = client.send(req);

		if (response == null || !(response.getSuccess())) {
			// mainView.showError(response == null ? "No response from server." :
			// response.getMessage());
			return;
		}

		// cast Person response as an employee
		Employee emp = (Employee) response.getData();

		current = createController(emp); // route by concrete type

		if (current == null) {
			// mainView.showError("Unknown employee role.");
			return;
		}

		// MainView.setContent
		// frame.setContentPane(panel);
		// frame.revalidate();
		// frame.repaint();
	}

	/**
	 * Branch on the userRole.
	 */
	private EmployeeController createController(Employee emp) {

		role = emp.getRole();

		switch (role) {
		case DOCTOR:
			return new DoctorController(this, client, (Doctor) emp);
		case NURSE:
			return new NurseController(this, client, (Nurse) emp);
		case RECEPTIONIST:
			return new ReceptionistController(this, client, (Receptionist) emp);
		default:
			return null;
		}

	}


	public static void main(String[] args) {
		

	}

}
