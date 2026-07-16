package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.StaffAssignment;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class StaffAssignmentController {
	private final StaffAssignment view;

	public StaffAssignmentController(StaffAssignment view) {
		this.view = view;
		init();
		loadCombos();
		refresh();
	}

	private void init() {
		view.getBtnAssign().addActionListener(e -> save(RequestType.ASSIGN_STAFF));
		view.getBtnUpdate().addActionListener(e -> save(RequestType.ASSIGN_STAFF));
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}

	private void loadCombos() {
		add(view.getCboDepartment(), "Medical", "Billing", "Reception", "Administration");
		add(view.getCboPriority(), "Low", "Medium", "High", "Urgent");
	}

	private void add(javax.swing.JComboBox<String> box, String... items) {
		box.removeAllItems();
		for (String item : items)
			box.addItem(item);
	}

	private void save(RequestType type) {
		Request req = new Request();
		req.setType(type);
		req.putMap("complaintId", view.getTxtComplaintId().getText().trim());
		req.putMap("staffId", view.getTxtStaffId().getText().trim());
		req.putMap("department", String.valueOf(view.getCboDepartment().getSelectedItem()));
		req.putMap("priority", String.valueOf(view.getCboPriority().getSelectedItem()));
		req.putMap("notes", view.getTxtNotes().getText().trim());
		Response res = Client.send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(new Request(RequestType.GET_STAFF_ASSIGNMENTS, "all", true));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
			return;
		view.clearTable();
		if (res.getData() instanceof List<?>)
			for (Object row : (List<Object>) res.getData())
				view.addAssignment((Object[]) row);
	}
}
