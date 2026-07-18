package com.careplus.client.patient.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.patient.view.MedicalRecordView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.MedicalRecord;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class MedicalRecordController {
	private final MedicalRecordView view;
	private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

	public MedicalRecordController(MedicalRecordView view) {
		this.view = view;
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
		view.getTblMedicalRecords().getSelectionModel().addListSelectionListener(e -> displaySelectedRecord());
		refresh();
	}

	private void displaySelectedRecord() {
		int row = view.getTblMedicalRecords().getSelectedRow();

		if (row < 0) {

			return;
		}

		view.getTxtDiagnosis().setText(
				String.valueOf(view.getTableModel().getValueAt(row, 1)));

		view.getTxtTreatment().setText(
				String.valueOf(view.getTableModel().getValueAt(row, 2)));

		view.getTxtPrescription().setText(
				String.valueOf(view.getTableModel().getValueAt(row, 3)));

		view.getTxtCreatedDate().setText(
				String.valueOf(view.getTableModel().getValueAt(row, 4)));

		logger.info("Medical record selected: {}",
				view.getTableModel().getValueAt(row, 0));
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_MEDICAL_RECORDS,
						"patientId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !res.getSuccess()) {

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

			view.addMedicalRecord(viewRow);
		}

		logger.info("Medical records refreshed successfully");

	}
}
