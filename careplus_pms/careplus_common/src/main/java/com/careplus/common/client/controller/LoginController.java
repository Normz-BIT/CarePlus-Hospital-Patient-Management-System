package com.careplus.common.client.controller;

import java.util.List;

import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.DashboardFeature;
import com.careplus.common.client.view.Login;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.Person;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class LoginController {
	private final Login view;
	private final List<DashboardFeature> features;

	public LoginController(Login view, List<DashboardFeature> features) {
		this.view = view;
		this.features = features;
		init();
	}

	private void init() {
		view.getBtnLogin().addActionListener(e -> login());
	}

	private void login() {
		
		String id = view.getTxtId().getText().trim();
		String password = String.valueOf(view.getTxtPassword().getPassword());

		if (id.isEmpty() || password.isEmpty()) {
			view.showMessage("ID and password are required.");
			return;
		}

		// the server looks up the person by id, verifies the
		// password, and returns the concrete Patient/Doctor/Nurse/Receptionist
		// so the dashboard can show the right menus.
		Request request = new Request();

		request.setType(RequestType.LOGIN);
		request.putMap("id", id);
		request.putMap("password", password);

		Response response =  Client.send(request);

		if (response != null && response.getSuccess()) {
			
			Person user = (Person) response.getData();
			
			MainDashboard dashboard = new MainDashboard(user, features);
			dashboard.setVisible(true);
			view.dispose();
			
			//TODO log4j2
			
		} else {
			view.showMessage(response == null ? "No response from server." : response.getMessage());
		     //TODO log4j2
		}
	}
	
	
	
	
}
