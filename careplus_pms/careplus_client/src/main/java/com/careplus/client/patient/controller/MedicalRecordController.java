package com.careplus.client.patient.controller;

import java.util.List;

import com.careplus.client.patient.view.MedicalRecord;
import com.careplus.common.client.net.Client;
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
	private final MedicalRecord view;

	public MedicalRecordController(MedicalRecord view) {
		this.view = view;
<<<<<<< HEAD
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
=======
>>>>>>> stash
		refresh();
	}

<<<<<<< HEAD
=======
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

>>>>>>> stash
	@SuppressWarnings("unchecked")
<<<<<<< HEAD
	private void refresh() {
		Response res = new Client().send(new Request(RequestType.GET_MEDICAL_RECORDS, "patientId", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
=======
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_MEDICAL_RECORDS,
						"patientId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Medical records could not be retrieved");
>>>>>>> stash
			return;
		view.clearTable();
		if (res.getData() instanceof List<?>)
			for (Object row : (List<Object>) res.getData())
				view.addMedicalRecord((Object[]) row);
	}
}
