package com.careplus.client.patient.controller;

import java.util.List;

import com.careplus.client.patient.view.MedicalRecordView;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class MedicalRecordController {
	private final MedicalRecordView view;

	public MedicalRecordController(MedicalRecordView view) {
		this.view = view;
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
		refresh();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(new Request(RequestType.GET_MEDICAL_RECORDS, "patientId", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
			return;
		view.clearTable();
		if (res.getData() instanceof List<?>)
			for (Object row : (List<Object>) res.getData())
				view.addMedicalRecord((Object[]) row);
	}
}
