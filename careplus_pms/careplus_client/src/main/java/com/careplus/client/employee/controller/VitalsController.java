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
public class VitalsController {
	private final VitalsView view;
	private static final Logger logger = LogManager.getLogger(VitalsController.class);

	public VitalsController(VitalsView view) {
		this.view = view;
		refresh();
	}

	/*
	 * Record Patient Vital Signs
	 */
	public void record() {
		/*
		 * TODO: take the patient from the case table selection rather than a typed ID,
		 * so a reading cannot be filed against the wrong patient.
		 */
		String patientId = view.getTxtPatientId().getText().trim();

		if (patientId.isEmpty()) {
			view.showMessage("Enter a patient ID before recording vitals.");
			logger.warn("Vital signs rejected because no patient was selected");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.RECORD_VITALS);

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
					row.getRecordedAt()
			};

			view.addCase(viewRow);
		}

		logger.info("Patient vital signs refreshed successfully");

	}
}