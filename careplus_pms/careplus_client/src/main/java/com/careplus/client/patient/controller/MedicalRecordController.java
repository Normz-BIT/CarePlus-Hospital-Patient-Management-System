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
 * Read only view of a patient's own diagnoses and treatment notes
 *
 * The only patient feature with no write path: patients read their record but
 * never alter it, since clinical content is authored by doctors through
 * DiagnosisController. GET_MEDICAL_RECORDS is unrouted on the server, so the
 * table is currently empty.
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
		 * Returns silently rather than warning, because this fires from a selection
		 * listener during ordinary table repopulation where a cleared selection is
		 * expected rather than a user error.
		 */
		if (row < 0) {

			return;
		}

		/*
		 * Column indices are positional and must match the order refresh builds each
		 * row in. The rows are untyped Object arrays, so reordering the columns there
		 * silently populates the wrong fields here with no compiler warning.
		 */
		view.getTxtDiagnosis().setText(
				String.valueOf(view.getTableModel().getValueAt(row, 1)));

		view.getTxtTreatment().setText(
				String.valueOf(view.getTableModel().getValueAt(row, 2)));

		view.getTxtFollowUpDate().setText(
				String.valueOf(view.getTableModel().getValueAt(row, 3)));

		view.getTxtCreatedDate().setText(
				String.valueOf(view.getTableModel().getValueAt(row, 4)));

		logger.info("Medical record selected: {}",
				view.getTableModel().getValueAt(row, 0));
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_MEDICAL_RECORDS,
						"patientId",
						MainDashboard.getCurrentUser().getPersonId()));

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

			view.addMedicalRecord(viewRow);
		}

		logger.info("Medical records refreshed successfully");

	}
}
