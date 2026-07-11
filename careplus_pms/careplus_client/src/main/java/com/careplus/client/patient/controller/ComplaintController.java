package com.careplus.client.patient.controller;

import java.util.List;

import com.careplus.client.patient.view.Complaint;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class ComplaintController {
	private final Complaint view;

	public ComplaintController(Complaint view) {
		this.view = view;
		init();
		refresh();
	}

	private void init() {
		view.getBtnSubmit().addActionListener(e -> submit());
		view.getBtnUpdate().addActionListener(e -> submit());
		view.getBtnDelete().addActionListener(e -> delete());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}

	private Response send(Request request) {
		return new Client().send(request);
	}

	private void submit() {
		String desc = view.getTxtDescription().getText().trim();
		if (desc.isEmpty()) {
			view.showMessage("Complaint description is required.");
			return;
		}
		Request req = new Request();
		req.setType(RequestType.SUBMIT_COMPLAINT);
		req.putMap("category", String.valueOf(view.getCboCategory().getSelectedItem()));
		req.putMap("priority", String.valueOf(view.getCboPriority().getSelectedItem()));
		req.putMap("description", desc);
		Response res = send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
	}

	private void delete() {
		int row = view.getTblComplaints().getSelectedRow();
		if (row < 0) {
			view.showMessage("Select a complaint to delete.");
			return;
		}
		Request req = new Request(RequestType.DELETE_COMPLAINT, "complaintId", view.getTableModel().getValueAt(row, 0));
		Response res = send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = send(new Request(RequestType.GET_MY_COMPLAINTS, "patientId", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
			return;
		view.clearTable();
		if (res.getData() instanceof List<?>)
			for (Object row : (List<Object>) res.getData())
				view.addComplaint((Object[]) row);
	}
}
