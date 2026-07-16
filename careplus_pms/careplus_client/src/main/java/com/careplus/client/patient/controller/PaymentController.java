package com.careplus.client.patient.controller;

import java.util.List;

import com.careplus.client.patient.view.Payment;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class PaymentController {
	private final Payment view;

	public PaymentController(Payment view) {
		this.view = view;
		view.getBtnPay().addActionListener(e -> pay());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
		refresh();
	}

	private void pay() {
		Request req = new Request();
		req.setType(RequestType.MAKE_PAYMENT);
		req.putMap("amount", view.getTxtAmount().getText().trim());
		req.putMap("method", view.getTxtMethod().getText().trim());
		Response res = Client.send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(new Request(RequestType.GET_MY_PAYMENTS, "patientId", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
			return;
		view.clearTable();
		if (res.getData() instanceof List<?>)
			for (Object row : (List<Object>) res.getData())
				view.addPayment((Object[]) row);
	}
}
