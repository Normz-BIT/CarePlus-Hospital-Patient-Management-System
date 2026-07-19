package com.careplus.client.employee.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.StaffAssignmentView;
import com.careplus.common.client.net.Client;
import com.careplus.common.enums.ComplaintStatus;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Staff Assignment Controller
 * Assigns complaints to employees
 * Updates and views staff assignments
 *
 * Note: there is no StaffAssignment model in careplus_common and the server does
 * not implement ASSIGN_STAFF / GET_STAFF_ASSIGNMENTS yet, so this controller
 * works with raw Object[] rows rather than a typed model.
 */
public class StaffAssignmentController {

	/*
	 * Departments are a free-text column on Employee, not an enum.
	 *
	 * Hardcoded here as a result, so this list is a client side convention that
	 * nothing validates against the values actually stored on employee rows. A
	 * department renamed in the database would leave this combo silently wrong.
	 * Promoting department to an enum, as DoctorSpecialization already is, would
	 * remove the duplication.
	 */
	private static final String[] DEPARTMENTS = { "Medical", "Billing", "Reception", "Administration" };

	private final StaffAssignmentView view;
	private static final Logger logger = LogManager.getLogger(StaffAssignmentController.class);

	public StaffAssignmentController(StaffAssignmentView view) {
		this.view = view;
		loadCombos();
		refresh();
	}

	/*
	 * Load Department and Complaint Status
	 *
	 * Both combos are populated locally rather than from the server, the departments
	 * from the constant above and the statuses from the enum. Neither costs a round
	 * trip, but the department list can drift from the data while the status list
	 * cannot.
	 */
	private void loadCombos() {
		view.getCboDepartment().removeAllItems();

		for (String department : DEPARTMENTS)
			view.getCboDepartment().addItem(department);

		view.getCboStatus().removeAllItems();

		for (ComplaintStatus status : ComplaintStatus.values())
			view.getCboStatus().addItem(status.name());
	}

	/*
	 * Assign or update a staff assignment. Both actions send the same request.
	 *
	 * There is no separate update action because the server cannot distinguish the
	 * two without a StaffAssignment model: ASSIGN_STAFF is expected to create or
	 * overwrite the assignment for a given complaint. That makes assignment
	 * idempotent, so reassigning the same complaint replaces rather than duplicates.
	 */
	public void save() {
		save(RequestType.ASSIGN_STAFF);
	}

	private void save(RequestType type) {
		if (view.getTxtComplaintId().getText().trim().isEmpty()
				|| view.getTxtStaffId().getText().trim().isEmpty()) {

			view.showMessage("Complaint ID and employee ID are required.");
			logger.warn("Staff assignment rejected because the complaint ID or employee ID was empty");

			return;
		}

		Request req = new Request();
		req.setType(type);

		try {

			req.putMap(
					"complaintId",
					Integer.parseInt(view.getTxtComplaintId().getText().trim()));

			req.putMap(
					"employeeId",
					Integer.parseInt(view.getTxtStaffId().getText().trim()));

			req.putMap(
					"department",
					String.valueOf(view.getCboDepartment().getSelectedItem()));

			req.putMap(
					"status",
					String.valueOf(view.getCboStatus().getSelectedItem()));

			req.putMap(
					"notes",
					view.getTxtNotes().getText().trim());

			logger.info(
					"Assigning complaint ID: {} to employee ID: {}",
					view.getTxtComplaintId().getText().trim(),
					view.getTxtStaffId().getText().trim());

			Response res = Client.send(req);

			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while assigning staff");
			} else {
				logger.info("Server staff assignment response: {}", res.getMessage());
			}

		} catch (Exception e) {

			logger.error("An error occurred while assigning staff", e);
			view.showMessage("Complaint ID and employee ID must be valid numbers.");
		}

		refresh();

	}

	/*
	 * View All Staff Assignments
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_STAFF_ASSIGNMENTS,
						"all",
						true));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Staff assignments could not be retrieved");
			return;
		}

		view.clearTable();

		if (res.getData() instanceof List<?>) {
			for (Object row : (List<Object>) res.getData()) {

				if (row instanceof Object[]) {
					view.addAssignment((Object[]) row);
				}
			}
		}

		logger.info("Staff assignments refreshed successfully");

	}
}