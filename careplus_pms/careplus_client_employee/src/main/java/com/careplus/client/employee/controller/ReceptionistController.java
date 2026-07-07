package com.careplus.client.employee.controller;

import com.careplus.common.client.net.Client;
import com.careplus.common.model.Receptionist;

public class ReceptionistController extends EmployeeController {

	// private final DoctorView view;

	public ReceptionistController(MainController main, Client connection, Receptionist receptionist) {
		super(main, connection, receptionist);
		// this.view = new ReceptionistView(receptionist);
		// this.view.setLogoutHandler(this::logout);
		// TODO: wire actions here
		/*
		 * view total Patient Requests (Appointments), view resolved Complaints
		 * view outstanding Complaints, view all Complaints 
		 * search Complaints by Category
		 * respond to patients 
		 * assign doctors/nurses
		 */
	}

	/*
	 * @Override public getView() { return view; }
	 */

}
