package com.careplus.client.employee.controller;

import java.util.Date;
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
 */
public class VitalsController {
	private final VitalsView view;
	private static final Logger logger = LogManager.getLogger(VitalsController.class);

	public VitalsController(VitalsView view) {
		this.view = view;
		init();
		refresh();
	}

	/*
	 * Initialize Vital Sign Events
	 */
	private void init() {
		view.getBtnRecord().addActionListener(e -> record());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}

	/*
	 * Record Patient Vital Signs
	 */
	private void record() {
		String patientId = view.getTxtPatientId().getText().trim();

		if (patientId.isEmpty()) {
			patientId = selectedPatientId();
		}

		if (patientId.isEmpty()) {
			view.showMessage("Enter or select a patient before recording vitals.");
			logger.warn("Vital signs rejected because no patient was selected");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.RECORD_VITALS);

		VitalSigns vitalSigns = new VitalSigns();

		try {

			vitalSigns.setTemperature(
					Double.parseDouble(
							view.getTxtTemperature().getText().trim()));

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

			vitalSigns.setRecordedAt(new Date());

			//TODO log4j2
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

			// TODO
			logger.error("An error occurred while recording vital signs", e);
			view.showMessage("Temperature, heart rate and respiratory rate must be valid numbers.");
		}

		//refresh();

	}

	/** Patient id of the currently selected assigned-case row, or "". */
	private String selectedPatientId() {
		int row = view.getTblCases().getSelectedRow();

		if (row < 0) {

			return "";
		}

		Object value = view.getTableModel().getValueAt(row, 1);

		return value == null ? "" : String.valueOf(value);
	}

	/*
	 * View Patient Vital Signs
	 */
	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_ASSIGNED_CASES,
						"nurseId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !res.getSuccess()) {

			logger.warn("Patient vital signs could not be retrieved");
			return;
		}

		view.clearTable();

		if (res.getData() instanceof List<?>) {
			for (Object row : (List<Object>) res.getData()) {

				if (row instanceof VitalSigns) {
					VitalSigns vitalSigns = (VitalSigns) row;

					Object[] viewRow = new Object[] {
							vitalSigns.getVitalId(),
							"",
							vitalSigns.getTemperature(),
							vitalSigns.getBloodPressure(),
							vitalSigns.getHeartRate(),
							vitalSigns.getRespiratoryRate(),
							vitalSigns.getObservations(),
							vitalSigns.getNursingNotes(),
							vitalSigns.getRecordedAt()
					};

					view.addCase(viewRow);

				} else if (row instanceof Object[]) {
					view.addCase((Object[]) row);
				}
			}
		}

		logger.info("Patient vital signs refreshed successfully");

	}
}