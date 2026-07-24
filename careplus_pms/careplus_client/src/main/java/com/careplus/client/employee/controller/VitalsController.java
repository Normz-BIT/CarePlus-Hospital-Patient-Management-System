package com.careplus.client.employee.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.VitalsView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.VitalSigns;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.common.util.DateDisplay;

/*
 * Vitals Controller
 * Allows nurses to record and view patient vital signs
 *
 * Each submission creates a new timestamped observation rather than overwriting
 * a current reading. We modelled it this way because a patient's vital signs are
 * only meaningful as a series: a doctor reading the record needs to see how a
 * temperature moved over a shift, not just its latest value.
 */
public class VitalsController {
	private final VitalsView view;
	private static final Logger logger = LogManager.getLogger(VitalsController.class);

	public VitalsController(VitalsView view) {
		this.view = view;
		loadPatients();
		refresh();
	}

	/*
	 * Fill the patient combo with "PAT0001 - Name" strings from the server. This
	 * replaces the typed patient ID, so a reading can't be filed against a typo.
	 */
	private void loadPatients() {
		Response res = Client.send(new Request(RequestType.GET_PATIENTS, "all", true));

		view.getCboPatient().removeAllItems();

		if (res != null && Boolean.TRUE.equals(res.getSuccess()) && res.getData() instanceof List<?> patients) {

			for (Object patient : patients) {
				view.getCboPatient().addItem(String.valueOf(patient));
			}
		} else {
			logger.warn("The patient list could not be loaded for the vitals combo");
		}
	}

	/*
	 * Pulls "PAT0001" back off the front of the combo's display string. Empty when
	 * nothing is selected.
	 */
	private String selectedPatientId() {
		Object selected = view.getCboPatient().getSelectedItem();

		if (selected == null) {
			return "";
		}

		String display = String.valueOf(selected);
		int dash = display.indexOf(" - ");

		return (dash < 0 ? display : display.substring(0, dash)).trim();
	}

	/*
	 * Record Patient Vital Signs
	 */
	public void record() {
		String patientId = selectedPatientId();

		if (patientId.isEmpty()) {
			view.showMessage("Select a patient before recording vitals.");
			logger.warn("Vital signs rejected because no patient was selected");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.RECORD_VITALS);

		VitalSigns vitalSigns = new VitalSigns();

		try {

			/*
			 * Temperature is a double while heart rate and respiratory rate are ints,
			 * matching how each is actually measured.
			 *
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

	}

	/*
	 * View Patient Vital Signs
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_ASSIGNED_CASES,
						"nurseId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Patient vital signs could not be retrieved");
			return;
		}

		view.clearTable();

		for (VitalSigns row : (List<VitalSigns>) res.getData()) {

			Object[] viewRow = new Object[] {
					row.getVitalId(),
					row.getTemperature(),
					row.getBloodPressure(),
					row.getHeartRate(),
					row.getRespiratoryRate(),
					row.getObservations(),
					row.getNursingNotes(),
					DateDisplay.withTime(row.getRecordedAt())
			};

			view.addCase(viewRow);
		}

		logger.info("Patient vital signs refreshed successfully");

	}
}