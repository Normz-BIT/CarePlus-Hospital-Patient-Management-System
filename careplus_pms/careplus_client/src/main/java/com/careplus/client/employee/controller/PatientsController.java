package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.PatientsView;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class PatientsController {
	private final PatientsView view;

	public PatientsController(PatientsView view) {
		this.view = view;
		init();
		refresh();
	}

	private void init() {
		view.getBtnFollowUp().addActionListener(e -> scheduleFollowUp());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}


	private void scheduleFollowUp() {
		String patientId = view.getTxtPatientId().getText().trim();
		if (patientId.isEmpty()) {
			patientId = selectedPatientId();
		}
		if (patientId.isEmpty() || view.getTxtDate().getText().trim().isEmpty()) {
			view.showMessage("Patient ID and follow-up date are required.");
			return;
		}

		Request req = new Request();
		req.setType(RequestType.SCHEDULE_FOLLOWUP);
		req.putMap("patientId", patientId);
		req.putMap("date", view.getTxtDate().getText().trim());
		req.putMap("time", view.getTxtTime().getText().trim());

		Response res = Client.send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
	}

	private String selectedPatientId() {
		int row = view.getTblPatients().getSelectedRow();
		if (row < 0) {
			return "";
		}
		Object value = view.getTableModel().getValueAt(row, 0);
		return value == null ? "" : String.valueOf(value);
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(new Request(RequestType.GET_ASSIGNED_PATIENTS, "doctor", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
			return;
		}
		view.clearTable();
		if (res.getData() instanceof List<?>) {
			for (Object row : (List<Object>) res.getData()) {
				view.addPatient((Object[]) row);
			}
		}
	}
}
