package com.careplus.client.employee.controller;

import java.text.SimpleDateFormat;
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
 */
public class PatientsController {
	private final PatientsView view;
	private static final Logger logger = LogManager.getLogger(PatientsController.class);

	public PatientsController(PatientsView view) {
		this.view = view;
		init();
		refresh();
	}

	/*
	 * Initialize Patient Events
	 */
	private void init() {
		view.getBtnFollowUp().addActionListener(e -> scheduleFollowUp());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}

	/*
	 * Schedule Patient Follow-up
	 */
	private void scheduleFollowUp() {
		String patientId = view.getTxtPatientId().getText().trim();

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

			String appointmentDateTime = view.getTxtDate().getText().trim()
					+ " " + view.getTxtTime().getText().trim();

			appointment.setAppointmentDate(
					new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(appointmentDateTime));

			appointment.setReason(view.getTxtReason().getText().trim());

			appointment.setStatus(AppointmentStatus.SCHEDULED);

			//TODO log4j2
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

			// TODO
			logger.error("An error occurred while scheduling follow-up", e);
			view.showMessage("Use the date and time format: yyyy-MM-dd HH:mm");
		}

		//refresh();

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
	private void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_ASSIGNED_PATIENTS,
						"doctorId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !res.getSuccess()) {

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
					row.getDateofBrith(),
					row.getGender(),
					row.getAddress(),
					row.getMedicalHistory()
			};

			view.addPatient(viewRow);
		}

		logger.info("Assigned patients refreshed successfully");

	}
}
