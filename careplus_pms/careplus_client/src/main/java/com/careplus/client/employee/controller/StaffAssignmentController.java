package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.StaffAssignment;
import com.careplus.common.client.net.Client;
import com.careplus.common.enums.ComplaintStatus;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

<<<<<<< HEAD
=======
/*
 * Staff Assignment Controller
 * Assigns complaints to employees
 * Updates and views staff assignments
 *
 * Note: there is no StaffAssignment model in careplus_common and the server does
 * not implement ASSIGN_STAFF / GET_STAFF_ASSIGNMENTS yet, so this controller
 * works with raw Object[] rows rather than a typed model.
 */
>>>>>>> stash
public class StaffAssignmentController {
<<<<<<< HEAD
	private final StaffAssignment view;
=======

	/*
	 * The four departments a member of staff can be assigned to. These are listed
	 * here so the combo is populated without a round trip to the server, since the
	 * set of departments is part of how the hospital is organised rather than data
	 * that changes during use.
	 *
	 * TODO: make department an enum on Employee, as DoctorSpecialization already
	 * is, so this list and the stored values come from one definition.
	 */
	private static final String[] DEPARTMENTS = { "Medical", "Billing", "Reception", "Administration" };

	private final StaffAssignmentView view;
	private static final Logger logger = LogManager.getLogger(StaffAssignmentController.class);
>>>>>>> stash

	public StaffAssignmentController(StaffAssignment view) {
		this.view = view;
		loadCombos();
		refresh();
	}

<<<<<<< HEAD
	private void init() {
		view.getBtnAssign().addActionListener(e -> save(RequestType.ASSIGN_STAFF));
		view.getBtnUpdate().addActionListener(e -> save(RequestType.ASSIGN_STAFF));
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}

=======
	/*
	 * Load Department and Complaint Status
	 *
	 * Both combos are filled locally rather than from the server, the departments
	 * from the constant above and the statuses from the ComplaintStatus enum. Both
	 * describe the domain rather than stored data, so the screen opens without
	 * waiting on a request.
	 */
>>>>>>> stash
	private void loadCombos() {
<<<<<<< HEAD
		add(view.getCboDepartment(), "Medical", "Billing", "Reception", "Administration");
		add(view.getCboPriority(), "Low", "Medium", "High", "Urgent");
	}
=======
		view.getCboDepartment().removeAllItems();
>>>>>>> stash

<<<<<<< HEAD
	private void add(javax.swing.JComboBox<String> box, String... items) {
		box.removeAllItems();
		for (String item : items)
			box.addItem(item);
=======
		for (String department : DEPARTMENTS)
			view.getCboDepartment().addItem(department);

		view.getCboStatus().removeAllItems();

		for (ComplaintStatus status : ComplaintStatus.values())
			view.getCboStatus().addItem(status.name());
>>>>>>> stash
	}

<<<<<<< HEAD
=======
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

>>>>>>> stash
	private void save(RequestType type) {
<<<<<<< HEAD
=======
		if (view.getTxtComplaintId().getText().trim().isEmpty() || view.getTxtStaffId().getText().trim().isEmpty()) {

			view.showMessage("Complaint ID and employee ID are required.");
			logger.warn("Staff assignment rejected because the complaint ID or employee ID was empty");

			return;
		}

>>>>>>> stash
		Request req = new Request();
		req.setType(type);
<<<<<<< HEAD
		req.putMap("complaintId", view.getTxtComplaintId().getText().trim());
		req.putMap("staffId", view.getTxtStaffId().getText().trim());
		req.putMap("department", String.valueOf(view.getCboDepartment().getSelectedItem()));
		req.putMap("priority", String.valueOf(view.getCboPriority().getSelectedItem()));
		req.putMap("notes", view.getTxtNotes().getText().trim());
		Response res = new Client().send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
=======

		try {

			req.putMap("complaintId", Integer.parseInt(view.getTxtComplaintId().getText().trim()));

			req.putMap("employeeId", Integer.parseInt(view.getTxtStaffId().getText().trim()));

			req.putMap("department", String.valueOf(view.getCboDepartment().getSelectedItem()));

			req.putMap("status", String.valueOf(view.getCboStatus().getSelectedItem()));

			req.putMap("notes", view.getTxtNotes().getText().trim());

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
			view.showMessage("Complaint ID and employee ID must be valid numbers.");
		}

		refresh();

>>>>>>> stash
	}

	@SuppressWarnings("unchecked")
<<<<<<< HEAD
	private void refresh() {
		Response res = new Client().send(new Request(RequestType.GET_STAFF_ASSIGNMENTS, "all", true));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
=======
	public void refresh() {
		Response res = Client.send(new Request(RequestType.GET_STAFF_ASSIGNMENTS, "all", true));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Staff assignments could not be retrieved");
>>>>>>> stash
			return;
		view.clearTable();
		if (res.getData() instanceof List<?>)
			for (Object row : (List<Object>) res.getData())
				view.addAssignment((Object[]) row);
	}
}
