package com.careplus.client.patient.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.client.net.Client;
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
	private Client client;

	
	private static final Logger logger = LogManager.getLogger(PatientController.class);
	
	private PatientController() {
		//
		client = new Client();
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

		// TODO remove after testing
		// create a complaint
		Complaint complaint = new Complaint();

		complaint.setDescription("Test for the server");

		// create a request

		Request req = new Request(RequestType.SUBMIT_COMPLAINT, "complaint", complaint);

		// send to server and get response
		Response response = client.send(req);

		complaint = (Complaint) response.getData();
		if (response.getSuccess()) {

		
			logger.info("Data from Server: " + complaint.getDescription());
			// view.show("Success");
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

	public static void main(String[] args) {

		// create controller
		PatientController pt = new PatientController();

		// TODO for testing remove later

		pt.OnSubmitComplaint();
	}

}
