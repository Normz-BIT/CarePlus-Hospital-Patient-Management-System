package com.careplus.client.employee.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.DiagnosisView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.MedicalRecord;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.common.util.DateDisplay;

/*
 * Diagnosis Controller
 * Lets a doctor write up a diagnosis, treatment note and follow up date.
 */
public class DiagnosisController {
	private final DiagnosisView view;
	private static final Logger logger = LogManager.getLogger(DiagnosisController.class);

	public DiagnosisController(DiagnosisView view) {
		this.view = view;
		refresh();
	}


	/*
	 * Save a new medical record
	 */
	public void saveNew() {
		save(RequestType.ADD_DIAGNOSIS);
	}

	/*
	 * Update the selected medical record
	 */
	public void saveUpdate() {
		save(RequestType.UPDATE_DIAGNOSIS);
	}

	/*
	 * Create and update share one implementation because the payload is identical
	 * and only the RequestType and the need for a selected row differ. The type
	 * parameter is what the two public entry points above vary.
	 *
	 */
	private void save(RequestType type) {
		if (view.getTxtPatientId().getText().trim().isEmpty()
				|| view.getTxtDiagnosis().getText().trim().isEmpty()) {

			view.showMessage("Patient ID and diagnosis are required.");
			logger.warn("Medical record rejected because the patient ID or diagnosis was empty");

			return;
		}

		Request req = new Request();
		req.setType(type);

		MedicalRecord medicalRecord = new MedicalRecord();

		try {

			/*
			 * The record has to carry which patient it's for, or the server has nothing
			 * to attach it to. The doctor goes on the request separately below, since
			 * that's the signed-in user.
			 */
			medicalRecord.setPatientId(
					view.getTxtPatientId().getText().trim().toUpperCase());

			medicalRecord.setDiagnosis(
					view.getTxtDiagnosis().getText().trim());

			medicalRecord.setTreatmentNote(
					view.getTxtTreatment().getText().trim());

			/*
			 * Comes straight off the Day/Month/Year spinners, already at midnight since
			 * that picker has no hour or min on it. follow_up_date is a DATE column so
			 * the time half never gets saved anyway.
			 */
			medicalRecord.setFollowUpDate(view.getPickerFollowUpDate().getDateTime());

			medicalRecord.setCreatedDate(LocalDateTime.now());

			/*
			 * An update needs the ID of the row being changed, which only exists once
			 * something is selected. 
			 */
			if (type == RequestType.UPDATE_DIAGNOSIS) {
				int row = view.getTblDiagnosis().getSelectedRow();

				if (row < 0) {
					view.showMessage("Select a medical record to update.");
					logger.warn("Medical record update attempted without selecting a record");

					return;
				}

				medicalRecord.setRecordId(
						Integer.parseInt(
								String.valueOf(
										view.getTableModel().getValueAt(row, 0))));
			}

			logger.info("Medical record created: {}", medicalRecord.toString());

			req.putMap("medicalRecord", medicalRecord);
			req.putMap("doctorId", MainDashboard.getCurrentUser().getPersonId());

			Response res = Client.send(req);
			
			

			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while saving medical record");
			} else {
				logger.info("Server medical record response: {}", res.getMessage());
			}

		} catch (Exception e) {

			logger.error("An error occurred while saving medical record", e);
			view.showMessage("Unable to save the medical record: " + e.getMessage());
		}

		refresh();

	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_DIAGNOSIS_RECORDS,
						"all",
						true));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Medical records could not be retrieved");
			return;
		}

		view.clearTable();

		for (MedicalRecord row : (List<MedicalRecord>) res.getData()) {

			Object[] viewRow = new Object[] {
					row.getRecordId(),
					row.getDiagnosis(),
					row.getTreatmentNote(),
					DateDisplay.dateOnly(row.getFollowUpDate()),
					DateDisplay.withTime(row.getCreatedDate())
			};

			view.addDiagnosis(viewRow);
		}

		logger.info("Medical records refreshed successfully");

	}
}
