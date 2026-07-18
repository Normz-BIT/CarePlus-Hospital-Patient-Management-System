package com.careplus.client.patient.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.patient.view.PaymentView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.model.Payment;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class PaymentController {
	private final PaymentView view;
	private static final Logger logger = LogManager.getLogger(PaymentController.class);

	public PaymentController(PaymentView view) {
		this.view = view;
		view.getBtnPay().addActionListener(e -> pay());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
		refresh();
	}

	private void pay() {
		Request req = new Request();
		req.setType(RequestType.MAKE_PAYMENT);

		Payment payment = new Payment();

		try {

			payment.setPatientId(MainDashboard.getCurrentUser().getPersonId());

			payment.setAmountPaid(Double.parseDouble(view.getTxtAmount().getText().trim()));

			if (payment.getAmountPaid() <= 0) {
				view.showMessage("Amount must be greater than zero (0)");
				logger.warn("Payment rejected because the amount was not greater than zero");
			
				return;
			}
			
			payment.setDescription(view.getTxtDiscription().getText().trim());

			// simulate outstanding balance
			payment.setOutstandingBalance(new Random().nextInt(500, 1001));

			payment.setPaymentDate(LocalDateTime.now());

			//TODO log4j2
			logger.info("Payment created: {}", payment.toString());

			req.putMap("payment", payment);

			Response res = Client.send(req);

			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while making payment");
			} else {
				logger.info("Server payment response: {}", res.getMessage());
			}

		} catch (Exception e) {

			// TODO
			logger.error("An error occurred while making payment", e);
			view.showMessage("Unable to complete payment: " + e.getMessage());
		}

		//refresh();

	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client
				.send(new Request(RequestType.GET_MY_PAYMENTS, "patientId", MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !res.getSuccess()) {

			logger.warn("Payment records could not be retrieved");
			return;
		}

		view.clearTable();

		for (Payment row : (List<Payment>) res.getData()) {
		    
		    Object[] viewRow = new Object[] {
		    		row.getPaymentId(),
		    		row.getAmountPaid(),
		    		row.getOutstandingBalance(),
		    		row.getDescription(),
		    		row.getPaymentDate()
		    };
		    
		    view.addPayment(viewRow);
		}

		logger.info("Payment records refreshed successfully");

	}
}
}
