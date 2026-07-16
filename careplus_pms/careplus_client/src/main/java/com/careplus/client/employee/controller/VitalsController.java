package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.Vitals;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class VitalsController {
	private final Vitals view;

	public VitalsController(Vitals view) {
		this.view = view;
		init();
		refresh();
	}

	private void init() {
		view.getBtnRecord().addActionListener(e -> record());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
	}


	private void record() {
		String patientId = view.getTxtPatientId().getText().trim();
		if (patientId.isEmpty()) {
			patientId = selectedPatientId();
		}
		if (patientId.isEmpty()) {
			view.showMessage("Enter or select a patient before recording vitals.");
			return;
		}

		Request req = new Request();
		req.setType(RequestType.RECORD_VITALS);
		req.putMap("patientId", patientId);
		req.putMap("temperature", view.getTxtTemperature().getText().trim());
		req.putMap("bloodPressure", view.getTxtBloodPressure().getText().trim());
		req.putMap("pulse", view.getTxtPulse().getText().trim());
		req.putMap("respiratoryRate", view.getTxtRespiratory().getText().trim());
		req.putMap("observations", view.getTxtObservations().getText().trim());
		req.putMap("nursingNotes", view.getTxtNursingNotes().getText().trim());

		Response res = Client.send(req);
		
		view.showMessage(res == null ? "No response from server." : res.getMessage());
	}

	/** Patient id of the currently selected assigned-case row, or "". */
	private String selectedPatientId() {
		int row = view.getTblCases().getSelectedRow();
		if (row < 0) {
			return "";
		}
		Object value = view.getTableModel().getValueAt(row, 0);
		return value == null ? "" : String.valueOf(value);
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(new Request(RequestType.GET_ASSIGNED_CASES, "nurse", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
			return;
		}
		view.clearTable();
		if (res.getData() instanceof List<?>) {
			for (Object row : (List<Object>) res.getData()) {
				view.addCase((Object[]) row);
			}
		}
	}
}
