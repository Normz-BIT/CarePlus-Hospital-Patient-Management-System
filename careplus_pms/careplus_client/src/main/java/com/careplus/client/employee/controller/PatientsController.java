package com.careplus.client.employee.controller;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

/*
 * Patients Controller
 * Views patients assigned to a doctor
 * Schedules patient follow-up appointments
 *
 * GET_ASSIGNED_PATIENTS and SCHEDULE_FOLLOWUP are both unrouted on the server, so
 * the patient list is currently empty and follow ups are not persisted.
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

		if (patientId.isEmpty() || view.getTxtDate().getText().trim().isEmpty()) {
			view.showMessage("Patient ID and follow-up date are required.");
			logger.warn("Follow-up rejected because the patient ID or date was empty");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.SCHEDULE_FOLLOWUP);

		Appointment appointment = new Appointment();

		try {

			/*
			 * This date and time parsing duplicates AppointmentController exactly, pattern
			 * string included. The two are unrelated code that must nonetheless stay in
			 * step, since both feed the same Appointment model. A shared formatter constant
			 * would remove the duplication, and note DiagnosisController uses a third,
			 * date only convention.
			 */
			String appointmentDateTime = view.getTxtDate().getText().trim()
					+ " " + view.getTxtTime().getText().trim();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.parse(appointmentDateTime, formatter);

			appointment.setAppointmentDate(localDateTime);

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
			view.showMessage("Use the date and time format: yyyy-MM-dd HH:mm:ss");
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
					row.getDateOfBirth(),
					row.getGender(),
					row.getAddress(),
					row.getMedicalHistory()
			};

			view.addPatient(viewRow);
		}

		logger.info("Assigned patients refreshed successfully");

	}
}
