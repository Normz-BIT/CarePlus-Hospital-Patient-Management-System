package com.careplus.client.patient.controller;

<<<<<<< HEAD
<<<<<<< HEAD
=======
import java.time.LocalDateTime;
>>>>>>> stash
=======
import java.time.LocalDateTime;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
import java.util.List;

import com.careplus.client.patient.view.Complaint;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Complaint Controller
 * Lets a patient file a complaint and review previous ones with their responses
 *
 * SUBMIT_COMPLAINT, DELETE_COMPLAINT and GET_MY_COMPLAINTS are all unrouted on
 * the server, so every call below currently returns an empty Response.
 */
public class ComplaintController {
	private final Complaint view;

	public ComplaintController(Complaint view) {
		this.view = view;
		loadCategories();
		refresh();
	}


	/*
	 * Load the complaint categories straight from the enum
	 *
	 * Read locally rather than fetched from the server, unlike the doctor and
	 * department combos in AppointmentController. Categories are a fixed part of the
	 * domain rather than data, so this costs no round trip and cannot drift out of
	 * step with the enum. The tradeoff is that adding a category needs a client
	 * rebuild.
	 */
	private void loadCategories() {
		view.getCboCategory().removeAllItems();

		for (ComplaintCategory category : ComplaintCategory.values())
			view.getCboCategory().addItem(category.name());
	}

<<<<<<< HEAD
<<<<<<< HEAD
	private Response send(Request request) {
		return new Client().send(request);
	}

	private void submit() {
=======
	public void submit() {
>>>>>>> stash
=======
	public void submit() {
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
		String desc = view.getTxtDescription().getText().trim();
		if (desc.isEmpty()) {
			view.showMessage("Complaint description is required.");
			return;
		}
		Request req = new Request();
		req.setType(RequestType.SUBMIT_COMPLAINT);
<<<<<<< HEAD
		req.putMap("category", String.valueOf(view.getCboCategory().getSelectedItem()));
		req.putMap("priority", String.valueOf(view.getCboPriority().getSelectedItem()));
		req.putMap("description", desc);
		Response res = send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
=======

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

>>>>>>> stash
	}

	public void delete() {
		int row = view.getTblComplaints().getSelectedRow();
		if (row < 0) {
			view.showMessage("Select a complaint to delete.");
			return;
		}
<<<<<<< HEAD
		Request req = new Request(RequestType.DELETE_COMPLAINT, "complaintId", view.getTableModel().getValueAt(row, 0));
		Response res = send(req);
=======

		Request req = new Request(
				RequestType.DELETE_COMPLAINT,
				"complaintId",
				view.getTableModel().getValueAt(row, 0));

		Response res = Client.send(req);

>>>>>>> stash
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
	}

	@SuppressWarnings("unchecked")
<<<<<<< HEAD
<<<<<<< HEAD
	private void refresh() {
		Response res = send(new Request(RequestType.GET_MY_COMPLAINTS, "patientId", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
=======
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_MY_COMPLAINTS,
						"patientId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Complaint records could not be retrieved");
>>>>>>> stash
			return;
		view.clearTable();
<<<<<<< HEAD
		if (res.getData() instanceof List<?>)
			for (Object row : (List<Object>) res.getData())
				view.addComplaint((Object[]) row);
=======

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

>>>>>>> stash
	}
}
