package com.careplus.client.patient.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.patient.view.ComplaintView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.enums.ComplaintCategory;
import com.careplus.common.enums.ComplaintStatus;
import com.careplus.common.model.Complaint;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.common.util.DateDisplay;

/*
 * Complaint Controller
 * Lets a patient file a complaint and look back at previous ones with whatever
 * response they got.
 */
public class ComplaintController {
	private final ComplaintView view;
	private static final Logger logger = LogManager.getLogger(ComplaintController.class);

	public ComplaintController(ComplaintView view) {
		this.view = view;
		loadCategories();
		refresh();
	}


	/*
	 * Fill the categories straight from the enum
	 *
	 * Done locally instead of asking the server, unlike the doctor combo in
	 * AppointmentController. Categories are a fixed part of the domain rather than
	 * data that changes, so there's no round trip and they can never drift out of
	 * step with the enum. Downside is adding a category means rebuilding the client.
	 */
	private void loadCategories() {
		view.getCboCategory().removeAllItems();

		for (ComplaintCategory category : ComplaintCategory.values())
			view.getCboCategory().addItem(category.name());
	}

	public void submit() {
		String desc = view.getTxtDescription().getText().trim();

		if (desc.isEmpty()) {
			view.showMessage("Complaint description is required.");
			logger.warn("Complaint rejected because the description was empty");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.SUBMIT_COMPLAINT);

		Complaint complaint = new Complaint();

		try {

			complaint.setDescription(desc);

			complaint.setCategory(
					ComplaintCategory.valueOf(
							String.valueOf(view.getCboCategory().getSelectedItem())));

			/*
			 * Every complaint enters at SUBMITTED, the only valid entry point of the
			 * lifecycle. A receptionist moves it onward from there.
			 */
			complaint.setStatus(ComplaintStatus.SUBMITTED);

			complaint.setDateSubmitted(LocalDateTime.now());

			/*
			 * An optional parent ID is what turns this complaint into a follow up on an
			 * earlier one rather than a new case. Left unset it stays zero, since the
			 * field is a primitive int, and zero is what marks an original complaint.
			 *
			 * The ID is typed by hand with no validation that it exists or belongs to this
			 * patient, so the server would need to verify ownership before linking.
			 */
			if (!view.getTxtParentId().getText().trim().isEmpty()) {
				complaint.setComplaintParentId(
						Integer.parseInt(view.getTxtParentId().getText().trim()));
			}

			logger.info("Complaint created: {}", complaint.toString());

			req.putMap("complaint", complaint);
			req.putMap("patientId", MainDashboard.getCurrentUser().getPersonId());

			Response res = Client.send(req);

			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while submitting complaint");
			} else {
				logger.info("Server complaint response: {}", res.getMessage());
			}

		} catch (Exception e) {

			logger.error("An error occurred while submitting complaint", e);
			view.showMessage("Unable to submit complaint: " + e.getMessage());
		}

		refresh();

	}

	public void delete() {
		int row = view.getTblComplaints().getSelectedRow();

		if (row < 0) {
			view.showMessage("Select a complaint to delete.");
			logger.warn("Complaint deletion attempted without selecting a record");

			return;
		}

		Request req = new Request(
				RequestType.DELETE_COMPLAINT,
				"complaintId",
				view.getTableModel().getValueAt(row, 0));

		Response res = Client.send(req);

		view.showMessage(res == null ? "No response from server." : res.getMessage());

		if (res == null) {
			logger.error("No response received from server while deleting complaint");
		} else {
			logger.info("Complaint deletion response: {}", res.getMessage());
		}

		refresh();
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_MY_COMPLAINTS,
						"patientId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Complaint records could not be retrieved");
			return;
		}

		view.clearTable();

		for (Complaint row : (List<Complaint>) res.getData()) {

			Object[] viewRow = new Object[] {
					row.getComplaintId(),
					row.getComplaintParentId(),
					row.getCategory(),
					row.getDescription(),
					DateDisplay.withTime(row.getDateSubmitted()),
					row.getResponse(),
					DateDisplay.withTime(row.getResponseDate()),
					row.getStatus()
			};

			view.addComplaint(viewRow);
		}

		logger.info("Complaint records refreshed successfully");

	}
}