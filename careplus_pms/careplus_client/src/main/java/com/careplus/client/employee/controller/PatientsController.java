package com.careplus.client.employee.controller;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.PatientsView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.enums.AppointmentStatus;
import com.careplus.common.model.Appointment;
import com.careplus.common.model.Patient;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.common.util.DateDisplay;

/*
 * Patients Controller
 * Shows the patients assigned to a doctor
 * Books follow-up appointments for them
 *
 * "Assigned" here means either a complaint a receptionist gave this doctor or an
 * appointment booked with them. See getAssignedPatients in AppointmentService,
 * we never built a separate assignment table.
 */
public class PatientsController {
	private final PatientsView view;
	private static final Logger logger = LogManager.getLogger(PatientsController.class);

	public PatientsController(PatientsView view) {
		this.view = view;
		refresh();
	}

	/*
	 * Schedule Patient Follow-up
	 */
	public void scheduleFollowUp() {
		String patientId = view.getTxtPatientId().getText().trim();

		/*
		 * The typed ID wins, with the table selection used only as a fallback. This is
		 * the one screen offering both routes, which spares the doctor retyping an ID
		 * for a patient already highlighted while still allowing a direct entry.
		 */
		if (patientId.isEmpty()) {
			patientId = selectedPatientId();
		}

		/*
		 * Only the patient needs checking now. The date spinners always hold a real
		 * date, so there's nothing left that can be blank.
		 */
		if (patientId.isEmpty()) {
			view.showMessage("Select a patient or enter a patient ID.");
			logger.warn("Follow-up rejected because no patient was given");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.SCHEDULE_FOLLOWUP);

		Appointment appointment = new Appointment();

		try {

			/*
			 * Straight off the Day/Month/Year/Hour/Min spinners. This used to be a copy of
			 * AppointmentController's parsing, pattern string and all, which meant two
			 * unrelated bits of code had to be kept in step by hand. Both screens share
			 * the one DateTimePicker now.
			 */
			appointment.setAppointmentDate(view.getPickerDate().getDateTime());

			appointment.setReason(view.getTxtReason().getText().trim());

			/*
			 * A follow up is an ordinary appointment as far as the model is concerned, so
			 * it enters at SCHEDULED like any other booking. Only the request type
			 * distinguishes it from a patient initiated one.
			 */
			appointment.setStatus(AppointmentStatus.SCHEDULED);

			logger.info(
					"Follow-up appointment created for patient ID: {}",
					patientId);

			req.putMap("appointment", appointment);
			req.putMap("patientId", patientId);
			req.putMap(
					"doctorId",
					MainDashboard.getCurrentUser().getPersonId());

			Response res = Client.send(req);

			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while scheduling follow-up");
			} else {
				logger.info("Server follow-up response: {}", res.getMessage());
			}

		} catch (Exception e) {

			logger.error("An error occurred while scheduling follow-up", e);
			view.showMessage("Unable to schedule the follow-up: " + e.getMessage());
		}

		refresh();

	}

	/*
	 * Get Selected Patient ID
	 */
	private String selectedPatientId() {
		int row = view.getTblPatients().getSelectedRow();

		if (row < 0) {

			return "";
		}

		Object value = view.getTableModel().getValueAt(row, 0);

		return value == null ? "" : String.valueOf(value);
	}

	/*
	 * View Assigned Patients
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_ASSIGNED_PATIENTS,
						"doctorId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Assigned patients could not be retrieved");
			return;
		}

		view.clearTable();

		for (Patient row : (List<Patient>) res.getData()) {

			Object[] viewRow = new Object[] {
					row.getPersonId(),
					row.getFirstName(),
					row.getLastName(),
					row.getEmail(),
					row.getPhone(),
					DateDisplay.dateOnly(row.getDateOfBirth()),
					row.getGender(),
					row.getAddress(),
					row.getMedicalHistory()
			};

			view.addPatient(viewRow);
		}

		logger.info("Assigned patients refreshed successfully");

	}
}
