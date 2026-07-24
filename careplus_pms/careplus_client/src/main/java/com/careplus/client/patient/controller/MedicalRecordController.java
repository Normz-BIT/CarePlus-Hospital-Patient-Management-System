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

/*
 * Medical Record Controller
 * Read-only view of a patient's own diagnoses and treatment notes
 *
 * The only patient screen with nothing to write: patients read their record but
 * never change it, since the clinical side is written by doctors through
 * DiagnosisController.
 */
public class MedicalRecordController {
	private final MedicalRecordView view;
	private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

	public MedicalRecordController(MedicalRecordView view) {
		this.view = view;
		refresh();
	}

	/*
	 * Copies the selected row into the detail fields so long free text such as a
	 * treatment note can be read in full, rather than clipped to a table cell.
	 * Nothing is re-fetched: the values come from the row already in the table.
	 */
	public void displaySelectedRecord() {
		int row = view.getTblMedicalRecords().getSelectedRow();

		/*
		 * Returns without a message rather than warning the user. This runs from a
		 * selection listener, and the selection is cleared every time the table is
		 * repopulated, so no selection here is the normal case rather than a mistake.
		 */
		if (row < 0) {

			return;
		}

		/*
		 * The column indices below follow the order refresh builds each row in, so the
		 * two methods have to be kept in step with each other.
		 */
		view.getTxtDiagnosis().setText(String.valueOf(view.getTableModel().getValueAt(row, 1)));

		view.getTxtTreatment().setText(String.valueOf(view.getTableModel().getValueAt(row, 2)));

		view.getTxtFollowUpDate().setText(String.valueOf(view.getTableModel().getValueAt(row, 3)));

		view.getTxtCreatedDate().setText(String.valueOf(view.getTableModel().getValueAt(row, 4)));

		logger.info("Medical record selected: {}", view.getTableModel().getValueAt(row, 0));
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(new Request(RequestType.GET_MEDICAL_RECORDS, "patientId",
				MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Medical records could not be retrieved");
			return;
		}

		view.clearTable();

		for (MedicalRecord row : (List<MedicalRecord>) res.getData()) {

			Object[] viewRow = new Object[] { row.getRecordId(), row.getDiagnosis(), row.getTreatmentNote(),
					row.getFollowUpDate(), row.getCreatedDate() };

			view.addMedicalRecord(viewRow);
		}

		logger.info("Medical records refreshed successfully");

	}
}
