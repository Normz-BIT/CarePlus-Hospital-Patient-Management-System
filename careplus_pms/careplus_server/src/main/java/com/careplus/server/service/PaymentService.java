package com.careplus.server.service;

import java.util.List;

import com.careplus.common.model.Payment;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * PaymentService
 * Records payments and serves a patient's payment history.
 *
 * One of only three request types currently wired into ClientHandler, so this
 * class plus AuthService is the working reference for how a service should be
 * shaped: acquire session, work, roll back on failure, always endSession.
 */
public class PaymentService extends BaseService {

	public Response pay(Request request) {

		/*
		 * The Payment arrives already built from the client rather than being assembled
		 * from separate parameters here, which keeps the request to a single object and
		 * the payment fields defined in one place.
		 *
		 * TODO: check the amount against what the patient actually owes before
		 * persisting, rather than accepting the figures the client sends.
		 */
		Payment payment = (Payment) request.getParams().get("payment");

		startSession();

		try {
			// TODO change to log4j2
			System.out.println("Read : " + payment.toString());

			/*
			 * persist rather than merge, because paymentId is database generated and this
			 * is always a new row. Hibernate assigns the identifier here but the INSERT is
			 * not guaranteed to reach the database until the commit inside endSession, so
			 * a constraint violation surfaces there rather than on this line.
			 */
			session.persist(payment);

			resp.setSuccess(true);

			resp.setMessage("Payment Added to databse succesfully");

		} catch (Exception e) {
			// TODO add log4j2
			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failes to save payment");
			System.out.println("Error: " + e.getMessage());

		}

		finally {

			endSession();
		}

		return resp;

	}

	public Response getPayments(Request request) {

		startSession();

		String id = (String) request.getParams().get("patientId");

		try {
			/*
			 * HQL, not SQL: "Payment" is the mapped entity name and "patientId" the Java
			 * field, so this query survives a change to the underlying table or column
			 * names.
			 *
			 * The ?1 placeholder with setParameter is what keeps this safe from injection.
			 * Concatenating the id into the query string instead would let a crafted
			 * patient ID read another patient's records.
			 *
			 * Note the result is unbounded. A patient with a long billing history returns
			 * every row in one Response, all of which must serialize across the socket
			 * and land in a JTable. Paging would be needed before this scales.
			 */
			List<Payment> payments = session.createQuery("FROM Payment WHERE patientId = ?1", Payment.class)
					.setParameter(1, id).list();

			resp.setSuccess(true);
			resp.setMessage("Payments found");
			/*
			 * Hibernate's list implementation is itself Serializable here, but anything
			 * lazily loaded inside these entities would fail on the client with a
			 * LazyInitializationException, since the session closes before the Response is
			 * written. Payment holds only primitives and Strings, so it travels safely.
			 */
			resp.setData(payments);

		}

		catch (Exception e) {

			// TODO add log4j2
			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get payment");
			System.out.println("Error: " + e.getMessage());

		} finally {

			endSession();
		}

		return resp;
	}

}
