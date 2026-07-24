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
import com.careplus.common.util.DateDisplay;

/*
 * Payment Controller
 * Lets a patient record a payment and look back at their payment history.
 *
 * This was the first screen we got talking to a real service end to end, so if
 * you're adding a new one this is a decent example to copy.
 */
public class PaymentController {
	private final PaymentView view;
	private static final Logger logger = LogManager.getLogger(PaymentController.class);

	public PaymentController(PaymentView view) {
		this.view = view;
		/*
		 * Loading from the constructor means opening this screen does a blocking server
		 * call before the window even appears. It runs on the Swing thread, so the whole
		 * dashboard is frozen for however long that takes.
		 */
		refresh();
	}

	public void pay() {
		Request req = new Request();
		req.setType(RequestType.MAKE_PAYMENT);

		Payment payment = new Payment();

		try {

			/*
			 * The patient ID comes from whoever is signed in, not from anything typed on
			 * screen, so a patient can't put a payment on someone else's account from
			 * here. The server doesn't double check this though, so it's really only a
			 * UI guarantee.
			 */
			payment.setPatientId(MainDashboard.getCurrentUser().getPersonId());

			/*
			 * A non-numeric amount throws right here and the catch below turns it into a
			 * message, so a typo doesn't crash the screen.
			 */
			payment.setAmountPaid(Double.parseDouble(view.getTxtAmount().getText().trim()));

			/*
			 * Rejecting zero and negatives is the one business rule enforced on this
			 * screen, and it is checked before anything is sent so an invalid payment never
			 * reaches the server. The server performs no equivalent check, so a modified
			 * client could still submit one.
			 */
			if (payment.getAmountPaid() <= 0) {
				view.showMessage("Amount must be greater than zero (0)");
				logger.warn("Payment rejected because the amount was not greater than zero");

				return;
			}

			payment.setDescription(view.getTxtDescription().getText().trim());

			/*
			 * PLACEHOLDER, don't forget this one. The balance is a random number rather
			 * than what the patient actually owes, so the figure in the history is made
			 * up. A real balance would have to be worked out on the server from previous
			 * charges and payments, since only the server can see the whole account.
			 */
			payment.setOutstandingBalance(new Random().nextInt(500, 1001));

			/*
			 * Time comes off this machine's clock, not the server's, so a workstation with
			 * the wrong time records a misleading payment time.
			 */
			payment.setPaymentDate(LocalDateTime.now());

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

			logger.error("An error occurred while making payment", e);
			view.showMessage("Unable to complete payment: " + e.getMessage());
		}

		/*
		 * Outside the try, so the table is re-read even when the payment failed. That
		 * keeps the display consistent with the server rather than with what the user
		 * just attempted.
		 */
		refresh();

	}

	/*
	 * The refresh pattern used by every controller in this project: ask the server
	 * for the whole list, clear the table, rebuild it row by row. Simple and always
	 * consistent with the server, at the cost of re-rendering everything after each
	 * action and losing the user's selection and scroll position.
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client
				.send(new Request(RequestType.GET_MY_PAYMENTS, "patientId", MainDashboard.getCurrentUser().getPersonId()));

		/*
		 * Boolean.TRUE.equals rather than a direct unboxing, because getSuccess is null
		 * for any request the server does not implement. Written this way the null case
		 * is treated as failure instead of throwing a NullPointerException.
		 */
		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Payment records could not be retrieved");
			return;
		}

		/*
		 * Cleared only after the response is known to be good, so a failed refresh
		 * leaves the previous rows on screen rather than blanking the table.
		 */
		view.clearTable();

		for (Payment row : (List<Payment>) res.getData()) {
		    
		    Object[] viewRow = new Object[] {
		    		row.getPaymentId(),
		    		row.getAmountPaid(),
		    		row.getOutstandingBalance(),
		    		row.getDescription(),
		    		DateDisplay.withTime(row.getPaymentDate())
		    };
		    
		    view.addPayment(viewRow);
		}

		logger.info("Payment records refreshed successfully");

	}
}

