package com.careplus.client.patient.controller;

import java.util.List;

import com.careplus.client.patient.view.Payment;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Payment Controller
 * Lets a patient record a payment and review their payment history
 *
 * Payments are one of only three features the server actually implements, so
 * unlike most controllers in this package this one round trips against a real
 * service rather than receiving empty responses.
 */
public class PaymentController {
	private final Payment view;

	public PaymentController(Payment view) {
		this.view = view;
		/*
		 * Populating from the constructor means opening this screen performs a blocking
		 * server call before the frame is shown. This runs on the Event Dispatch Thread,
		 * so the dashboard is frozen for the duration of that round trip.
		 */
		refresh();
	}

	public void pay() {
		Request req = new Request();
		req.setType(RequestType.MAKE_PAYMENT);
<<<<<<< HEAD
		req.putMap("amount", view.getTxtAmount().getText().trim());
		req.putMap("method", view.getTxtMethod().getText().trim());
		Response res = new Client().send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
=======

		Payment payment = new Payment();

		try {

			/*
			 * The patient ID is taken from the session rather than from any input, so a
			 * patient cannot file a payment against someone else's account from this
			 * screen. Note the server does not re-verify this, so it is a UI guarantee
			 * rather than an enforced one.
			 */
			payment.setPatientId(MainDashboard.getCurrentUser().getPersonId());

			/*
			 * A non numeric amount throws here and is caught below, which is what turns a
			 * typo into a message rather than a crash.
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
			 * PLACEHOLDER: the balance is invented at random rather than derived from what
			 * the patient actually owes, so the figure shown in the history is not real.
			 * A true balance has to be calculated server side from prior charges and
			 * payments, since only the server can see the full account.
			 */
			// simulate outstanding balance
			payment.setOutstandingBalance(new Random().nextInt(500, 1001));

			/*
			 * Stamped from the workstation clock rather than the server's, so a
			 * misconfigured client machine records a misleading payment time.
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

>>>>>>> stash
	}

	/*
	 * The refresh pattern used by every controller in this project: ask the server
	 * for the whole list, clear the table, rebuild it row by row. Simple and always
	 * consistent with the server, at the cost of re-rendering everything after each
	 * action and losing the user's selection and scroll position.
	 */
	@SuppressWarnings("unchecked")
<<<<<<< HEAD
	private void refresh() {
		Response res = new Client().send(new Request(RequestType.GET_MY_PAYMENTS, "patientId", "current"));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
=======
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
>>>>>>> stash
			return;
<<<<<<< HEAD
=======
		}

		/*
		 * Cleared only after the response is known to be good, so a failed refresh
		 * leaves the previous rows on screen rather than blanking the table.
		 */
>>>>>>> stash
		view.clearTable();
		if (res.getData() instanceof List<?>)
			for (Object row : (List<Object>) res.getData())
				view.addPayment((Object[]) row);
	}
}
