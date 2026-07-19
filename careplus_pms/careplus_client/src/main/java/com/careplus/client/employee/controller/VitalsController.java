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
 * Each submission creates a new timestamped observation rather than updating a
 * current reading, so a patient accumulates a history a clinician can read as a
 * trend.
 *
 * RECORD_VITALS and GET_ASSIGNED_CASES are both unrouted on the server, so
 * readings are not persisted and the case list is currently empty.
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
		 * The patient is identified by a hand typed ID rather than by selecting from the
		 * case list, so a mistyped ID files a reading against the wrong patient with
		 * nothing to catch it. Driving this from the table selection would remove that
		 * whole class of error.
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
			 * The three numeric fields are only checked for parseability, not for
			 * plausibility. A temperature of 400 or a heart rate of 5 is accepted and
			 * stored, so clinical range validation still has to be added, ideally server
			 * side in MedicalRecordService where it cannot be bypassed.
			 */
			vitalSigns.setTemperature(
					Double.parseDouble(
							view.getTxtTemperature().getText().trim()));

			/*
			 * Kept as free text because blood pressure is a systolic over diastolic pair
			 * rather than a single number. Nothing enforces that shape, so an unparseable
			 * value reaches the record unchallenged.
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
			 * One catch covers all three parses, so the message names every numeric field
			 * rather than the one that actually failed. Acceptable because they are entered
			 * together, but it does mean the nurse has to check all three.
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