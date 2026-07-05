package com.careplus.client.patient.controller;

import java.io.IOException;
import com.careplus.common.client.net.ServerConnection;
import com.careplus.common.model.Complaint;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Execution Start for the client Application 
 *
 *	can Login
 *  can view Payments
 * 	can make/view Complaints
 * 	can set/view Appointments(follow-ups)
 *	live chat with Receptionist,Doctors, Nurses (8:00 a.m. – 7:00 p.m.) 
 */

public class PatientController {

	// private PatientView view;
	// private Patient patient;
	private ServerConnection connection;

	private PatientController() {
		//
		connection = new ServerConnection();
	}

	public void onLogin() {
		

	}

	// view the payments from the database
	// maybe add function to make a payment later on
	public void onViewPayments() {

	}

	/*
	 * Submit Medical Request
	 */
	public void OnSubmitComplaint() {

		//TODO remove after testing
		// create a complaint
		Complaint complaint = new Complaint();

		// create a request
		Request req = new Request(RequestType.SUBMIT_COMPLAINT, "complaint", complaint);

		// send to server and get response
		Response response = connection.send(req);

		if (response.getSuccess()) {

			System.out.println("Data from Server" + response.getData());
			
			//view.show("Success");
		}

	}

	// create a new complaint that is a child of the original resolved complaint
	public void OnRequestComplaintFollowup() {

	}

	// view all complaints linked to this patient
	public void onViewComplaints() {

	}

	/*
	 * Consultation
	 */
	// book appointment
	public void onSubmitAppointmentConsultation() {

	}

	// view Upcoming appointments
	public void onSubmitAppointmentInquiry() {

	}

	// bonus marks
	// maybe later
	public void onVideoConsultation() {

	}

	// TODO add a retry button to UI to reconnect on disconnect
	public void connect() {

		try {
			connection.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		// create controller
		//PatientController pt = new PatientController();

		// Open Connection to server
		// pt.connect();

	}

}
