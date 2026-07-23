package com.careplus.client.employee.controller;

<<<<<<< HEAD
<<<<<<< HEAD
=======

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
>>>>>>> stash
=======

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
import java.util.List;

import com.careplus.client.employee.view.Patients;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

<<<<<<< HEAD
=======
/*
 * Patients Controller
 * Views patients assigned to a doctor
 * Schedules patient follow-up appointments
 *
 * GET_ASSIGNED_PATIENTS and SCHEDULE_FOLLOWUP are both unrouted on the server, so
 * the patient list is currently empty and follow ups are not persisted.
 */
>>>>>>> stash
public class PatientsController {
	private final Patients view;

	public PatientsController(Patients view) {
		this.view = view;
		refresh();
	}

<<<<<<< HEAD
<<<<<<< HEAD
	private void init() {
		view.getBtnFollowUp().addActionListener(e -> scheduleFollowUp());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}

	private Response send(Request request) {
		return new Client().send(request);
	}

	private void scheduleFollowUp() {
=======
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	/*
	 * Schedule Patient Follow-up
	 */
	public void scheduleFollowUp() {
<<<<<<< HEAD
>>>>>>> stash
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
		String patientId = view.getTxtPatientId().getText().trim();
<<<<<<< HEAD
=======

		/*
		 * The typed ID wins, with the table selection used only as a fallback. This is
		 * the one screen offering both routes, which spares the doctor retyping an ID
		 * for a patient already highlighted while still allowing a direct entry.
		 */
<<<<<<< HEAD
>>>>>>> stash
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
		if (patientId.isEmpty()) {
			patientId = selectedPatientId();
		}
		if (patientId.isEmpty() || view.getTxtDate().getText().trim().isEmpty()) {
			view.showMessage("Patient ID and follow-up date are required.");
			return;
		}

		Request req = new Request();
		req.setType(RequestType.SCHEDULE_FOLLOWUP);
		req.putMap("patientId", patientId);
		req.putMap("date", view.getTxtDate().getText().trim());
		req.putMap("time", view.getTxtTime().getText().trim());

<<<<<<< HEAD
		Response res = send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
=======
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

>>>>>>> stash
	}

	private String selectedPatientId() {
		int row = view.getTblPatients().getSelectedRow();
		if (row < 0) {
			return "";
		}
		Object value = view.getTableModel().getValueAt(row, 0);
		return value == null ? "" : String.valueOf(value);
	}

	@SuppressWarnings("unchecked")
<<<<<<< HEAD
<<<<<<< HEAD
	private void refresh() {
		Response res = send(new Request(RequestType.GET_ASSIGNED_PATIENTS, "doctor", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
=======
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_ASSIGNED_PATIENTS,
						"doctorId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Assigned patients could not be retrieved");
>>>>>>> stash
			return;
		}
		view.clearTable();
<<<<<<< HEAD
		if (res.getData() instanceof List<?>) {
			for (Object row : (List<Object>) res.getData()) {
				view.addPatient((Object[]) row);
			}
=======

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
>>>>>>> stash
		}
	}
}
