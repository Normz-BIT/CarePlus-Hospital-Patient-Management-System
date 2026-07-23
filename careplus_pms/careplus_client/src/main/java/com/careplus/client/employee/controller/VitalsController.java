package com.careplus.client.employee.controller;

<<<<<<< HEAD
=======
import java.time.LocalDateTime;
>>>>>>> stash
import java.util.List;

import com.careplus.client.employee.view.Vitals;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

<<<<<<< HEAD
=======
/*
 * Vitals Controller
 * Allows nurses to record and view patient vital signs
 *
 * Each submission creates a new timestamped observation rather than overwriting
 * a current reading. We modelled it this way because a patient's vital signs are
 * only meaningful as a series: a doctor reading the record needs to see how a
 * temperature moved over a shift, not just its latest value.
 *
 * TODO: route RECORD_VITALS and GET_ASSIGNED_CASES on the server so readings are
 * saved and the case list is populated.
 */
>>>>>>> stash
public class VitalsController {
	private final Vitals view;

	public VitalsController(Vitals view) {
		this.view = view;
		refresh();
	}

<<<<<<< HEAD
	private void init() {
		view.getBtnRecord().addActionListener(e -> record());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}

	private Response send(Request request) {
		return new Client().send(request);
	}

	private void record() {
=======
	/*
	 * Record Patient Vital Signs
	 */
	public void record() {
		/*
		 * TODO: take the patient from the case table selection rather than a typed ID,
		 * so a reading cannot be filed against the wrong patient.
		 */
>>>>>>> stash
		String patientId = view.getTxtPatientId().getText().trim();
		if (patientId.isEmpty()) {
<<<<<<< HEAD
			patientId = selectedPatientId();
		}
		if (patientId.isEmpty()) {
			view.showMessage("Enter or select a patient before recording vitals.");
=======
			view.showMessage("Enter a patient ID before recording vitals.");
			logger.warn("Vital signs rejected because no patient was selected");

>>>>>>> stash
			return;
		}

		Request req = new Request();
		req.setType(RequestType.RECORD_VITALS);
		req.putMap("patientId", patientId);
		req.putMap("temperature", view.getTxtTemperature().getText().trim());
		req.putMap("bloodPressure", view.getTxtBloodPressure().getText().trim());
		req.putMap("pulse", view.getTxtPulse().getText().trim());
		req.putMap("respiratoryRate", view.getTxtRespiratory().getText().trim());
		req.putMap("observations", view.getTxtObservations().getText().trim());
		req.putMap("nursingNotes", view.getTxtNursingNotes().getText().trim());

<<<<<<< HEAD
		Response res = send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
=======
		VitalSigns vitalSigns = new VitalSigns();

		try {

			/*
			 * Temperature is a double while heart rate and respiratory rate are ints,
			 * matching how each is actually measured: a temperature is recorded to one
			 * decimal place, whereas a pulse is a whole count of beats per minute.
			 *
			 * TODO: add clinical range checks on these three values, on the server side
			 * where they cannot be bypassed by a modified client.
			 */
			vitalSigns.setTemperature(
					Double.parseDouble(
							view.getTxtTemperature().getText().trim()));

			/*
			 * Blood pressure stays as text because it is a systolic over diastolic pair
			 * rather than a single number, so no numeric type would hold it without
			 * splitting it into two fields.
			 */
			vitalSigns.setBloodPressure(
					view.getTxtBloodPressure().getText().trim());

			vitalSigns.setHeartRate(
					Integer.parseInt(
							view.getTxtPulse().getText().trim()));

			vitalSigns.setRespiratoryRate(
					Integer.parseInt(
							view.getTxtRespiratory().getText().trim()));

			vitalSigns.setObservations(
					view.getTxtObservations().getText().trim());

			vitalSigns.setNursingNotes(
					view.getTxtNursingNotes().getText().trim());

			vitalSigns.setRecordedAt(LocalDateTime.now());

			logger.info(
					"Vital signs created for patient ID: {}",
					patientId);

			req.putMap("vitalSigns", vitalSigns);
			req.putMap("patientId", patientId);
			req.putMap(
					"nurseId",
					MainDashboard.getCurrentUser().getPersonId());

			Response res = Client.send(req);
			
			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while recording vital signs");
			} else {
				logger.info("Server vital-sign response: {}", res.getMessage());
			}

		} catch (Exception e) {

			/*
			 * A single catch covers all three number parses. The three readings are taken
			 * and entered together, so naming them all in one message matches how a nurse
			 * works through the form.
			 */
			logger.error("An error occurred while recording vital signs", e);
			view.showMessage("Temperature, heart rate and respiratory rate must be valid numbers.");
		}

		refresh();

>>>>>>> stash
	}

<<<<<<< HEAD
	/** Patient id of the currently selected assigned-case row, or "". */
	private String selectedPatientId() {
		int row = view.getTblCases().getSelectedRow();
		if (row < 0) {
			return "";
		}
		Object value = view.getTableModel().getValueAt(row, 0);
		return value == null ? "" : String.valueOf(value);
	}

=======
	/*
	 * View Patient Vital Signs
	 */
>>>>>>> stash
	@SuppressWarnings("unchecked")
<<<<<<< HEAD
	private void refresh() {
		Response res = send(new Request(RequestType.GET_ASSIGNED_CASES, "nurse", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
=======
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_ASSIGNED_CASES,
						"nurseId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Patient vital signs could not be retrieved");
>>>>>>> stash
			return;
		}
		view.clearTable();
<<<<<<< HEAD
		if (res.getData() instanceof List<?>) {
			for (Object row : (List<Object>) res.getData()) {
				view.addCase((Object[]) row);
			}
=======

		for (VitalSigns row : (List<VitalSigns>) res.getData()) {

			Object[] viewRow = new Object[] {
					row.getVitalId(),
					row.getTemperature(),
					row.getBloodPressure(),
					row.getHeartRate(),
					row.getRespiratoryRate(),
					row.getObservations(),
					row.getNursingNotes(),
					row.getRecordedAt()
			};

			view.addCase(viewRow);
>>>>>>> stash
		}
	}
}
