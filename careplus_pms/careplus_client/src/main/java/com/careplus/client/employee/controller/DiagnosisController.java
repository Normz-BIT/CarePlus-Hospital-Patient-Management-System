package com.careplus.client.employee.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.DiagnosisView;
import com.careplus.common.client.net.Client;
import com.careplus.common.model.MedicalRecord;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Diagnosis Controller
 * Lets a doctor record a diagnosis, treatment note and follow up date
 *
 * ADD_DIAGNOSIS, UPDATE_DIAGNOSIS and GET_DIAGNOSIS_RECORDS are all unrouted on
 * the server, so nothing written here is persisted yet.
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
	 * Worth noting against the append only intent described on MedicalRecord: an
	 * update path overwrites an existing clinical record rather than superseding it,
	 * which loses the previous diagnosis. Whether that is acceptable is a decision
	 * for the server side service.
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

			medicalRecord.setDiagnosis(
					view.getTxtDiagnosis().getText().trim());

			medicalRecord.setTreatmentNote(
					view.getTxtTreatment().getText().trim());

			/*
			 * Follow up is optional, and left blank the field stays null, which is what
			 * distinguishes a record needing no return visit from one that does.
			 *
			 * Parsed as a date only value, unlike AppointmentController and
			 * PatientsController which both expect yyyy-MM-dd HH:mm:ss. This is the third
			 * distinct date convention in the client, so the expected input differs from
			 * screen to screen with nothing shared between them. atStartOfDay then pads
			 * the value to midnight, so the time component here carries no meaning and
			 * must not be read as an appointment time.
			 */
			if (!view.getTxtFollowUpDate().getText().trim().isEmpty()) {
				medicalRecord.setFollowUpDate(
						LocalDate.parse(view.getTxtFollowUpDate().getText().trim())
								.atStartOfDay());
			}

			medicalRecord.setCreatedDate(LocalDateTime.now());

			/*
			 * An update needs the ID of the row being changed, which only exists once
			 * something is selected. A create has no such requirement, which is why this
			 * guard is inside the type check rather than applying to both paths.
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
			
			Response res = Client.send(req);
			
			

			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while saving medical record");
			} else {
				logger.info("Server medical record response: {}", res.getMessage());
			}

		} catch (Exception e) {

			logger.error("An error occurred while saving medical record", e);
			view.showMessage("Use the follow-up date format: yyyy-MM-dd");
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
					row.getFollowUpDate(),
					row.getCreatedDate()
			};

			view.addDiagnosis(viewRow);
		}

		logger.info("Medical records refreshed successfully");

	}
}
