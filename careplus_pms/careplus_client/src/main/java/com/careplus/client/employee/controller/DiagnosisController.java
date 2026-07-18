package com.careplus.client.employee.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.DiagnosisView;
import com.careplus.common.client.net.Client;
import com.careplus.common.model.MedicalRecord;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class DiagnosisController {
	private final DiagnosisView view;
	private static final Logger logger = LogManager.getLogger(DiagnosisController.class);

	public DiagnosisController(DiagnosisView view) {
		this.view = view;
		init();
		loadStatus();
		refresh();
	}

	private void init() {
		view.getBtnSave().addActionListener(e -> save(RequestType.ADD_DIAGNOSIS));
		view.getBtnUpdate().addActionListener(e -> save(RequestType.UPDATE_DIAGNOSIS));
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}

	private void loadStatus() {
		view.getCboStatus().removeAllItems();
		view.getCboStatus().addItem(
				new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

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

			if (!view.getTxtPrescription().getText().trim().isEmpty()) {
				medicalRecord.setFollowUpDate(
						new SimpleDateFormat("yyyy-MM-dd").parse(
								view.getTxtPrescription().getText().trim()));
			}

			medicalRecord.setCreatedDate(new Date());

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

			//TODO log4j2
			logger.info("Medical record created: {}", medicalRecord.toString());

			req.putMap("medicalRecord", medicalRecord);
			req.putMap("patientId", view.getTxtPatientId().getText().trim());

			Response res = Client.send(req);

			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while saving medical record");
			} else {
				logger.info("Server medical record response: {}", res.getMessage());
			}

		} catch (Exception e) {

			// TODO
			logger.error("An error occurred while saving medical record", e);
			view.showMessage("Use the follow-up date format: yyyy-MM-dd");
		}

		//refresh();

	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_DIAGNOSIS_RECORDS,
						"all",
						true));

		if (res == null || !res.getSuccess()) {

			logger.warn("Medical records could not be retrieved");
			return;
		}

		view.clearTable();

		for (MedicalRecord row : (List<MedicalRecord>) res.getData()) {

			Object[] viewRow = new Object[] {
					row.getRecordId(),
					"",
					row.getDiagnosis(),
					row.getTreatmentNote(),
					row.getFollowUpDate(),
					row.getCreatedDate()
			};

			view.addDiagnosis(viewRow);
		}

		loadStatus();

		logger.info("Medical records refreshed successfully");

	}
}
