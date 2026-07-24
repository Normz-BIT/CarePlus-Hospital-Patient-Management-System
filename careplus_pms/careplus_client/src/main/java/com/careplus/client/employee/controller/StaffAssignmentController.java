package com.careplus.client.employee.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.StaffAssignmentView;
import com.careplus.common.client.net.Client;
import com.careplus.common.enums.ComplaintStatus;
import com.careplus.common.enums.Department;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Staff Assignment Controller
 * Assigns complaints to employees
 * Updates and views staff assignments
 *
 * Note: there is no StaffAssignment model in careplus_common. The server side
 * (ReportService) answers GET_STAFF_ASSIGNMENTS with plain Object[] rows from a
 * JDBC query, and this controller renders them straight into the table. An
 * assignment is really just two columns on complaint, so a model class never
 * felt worth it.
 */
public class StaffAssignmentController {

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
	 * Both combos are filled from enums rather than from the server: departments
	 * from Department and statuses from ComplaintStatus. Both describe the domain
	 * rather than stored data, so the screen opens without waiting on a request,
	 * and the department list is the same definition Employee stores.
	 */
	private void loadCombos() {
		view.getCboDepartment().removeAllItems();

		for (Department department : Department.values())
			view.getCboDepartment().addItem(department.name());

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
	 * idempotent, so reassigning the same complaint replaces rather than
	 * duplicates.
	 */
	public void save() {
		save(RequestType.ASSIGN_STAFF);
	}

	private void save(RequestType type) {
		if (view.getTxtComplaintId().getText().trim().isEmpty() || view.getTxtStaffId().getText().trim().isEmpty()) {

			view.showMessage("Complaint ID and employee ID are required.");
			logger.warn("Staff assignment rejected because the complaint ID or employee ID was empty");

			return;
		}

		Request req = new Request();
		req.setType(type);

		try {

			req.putMap("complaintId", Integer.parseInt(view.getTxtComplaintId().getText().trim()));

			/*
			 * The staff ID goes as a String: employee IDs look like STF0001, so parsing
			 * this as a number could never work. Only the complaint ID is numeric.
			 */
			req.putMap("employeeId", view.getTxtStaffId().getText().trim());

			req.putMap("department", String.valueOf(view.getCboDepartment().getSelectedItem()));

			req.putMap("status", String.valueOf(view.getCboStatus().getSelectedItem()));

			logger.info("Assigning complaint ID: {} to employee ID: {}", view.getTxtComplaintId().getText().trim(),
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
			view.showMessage("The complaint ID must be a valid number.");
		}

		refresh();

	}

	/*
	 * View All Staff Assignments
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(new Request(RequestType.GET_STAFF_ASSIGNMENTS, "all", true));

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