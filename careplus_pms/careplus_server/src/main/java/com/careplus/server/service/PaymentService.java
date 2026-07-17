package com.careplus.server.service;

import java.util.List;

import com.careplus.common.model.Payment;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

public class PaymentService extends BaseService {

	public Response pay(Request request) {

		Payment payment = (Payment) request.getParams().get("payment");

		startSession();

		try {
			// TODO change to log4j2
			System.out.println("Read : " + payment.toString());

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
			List<Payment> payments = session.createQuery("FROM Payment WHERE patientId = ?1", Payment.class)
					.setParameter(1, id).list();

			resp.setSuccess(true);
			resp.setMessage("Payments found");
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
