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

public class ComplaintController {
	private final ComplaintView view;
	private static final Logger logger = LogManager.getLogger(ComplaintController.class);

	public ComplaintController(ComplaintView view) {
		this.view = view;
		loadCategories();
		refresh();
	}


	/*
	 * Load the complaint categories straight from the enum
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

			complaint.setStatus(ComplaintStatus.SUBMITTED);

			complaint.setDateSubmitted(LocalDateTime.now());

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
					row.getDateSubmitted(),
					row.getResponse(),
					row.getResponseDate(),
					row.getStatus()
			};

			view.addComplaint(viewRow);
		}

		logger.info("Complaint records refreshed successfully");

	}
}