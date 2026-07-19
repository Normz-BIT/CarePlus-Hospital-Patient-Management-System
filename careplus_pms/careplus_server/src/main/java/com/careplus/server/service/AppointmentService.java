package com.careplus.server.service;

import java.util.List;

import com.careplus.common.model.Appointment;
import com.careplus.common.net.Response;

/*
 * AppointmentService
 * Intended home of appointment booking and lookup.
 *
 * NOT YET IMPLEMENTED. Both methods are placeholders returning null, and none of
 * the appointment RequestType values are routed in ClientHandler, so the patient
 * AppointmentController currently gets empty responses.
 *
 * Does not extend BaseService yet, so it has no session or transaction handling.
 */
public class AppointmentService {

	/*
	 * Backs the patient's appointment inquiry screen, which needs the date, the
	 * assigned doctor and the status for each booking.
	 *
	 * Takes an int while Person identifiers are Strings throughout the model, so
	 * this signature needs reconciling before it can be wired to a real lookup.
	 */
	public List<Appointment> forPatient (int pid){
		return null;

	}

	/*
	 * A new booking should enter at AppointmentStatus.SCHEDULED, the only sensible
	 * starting state of the SCHEDULED, COMPLETED, CANCELLED lifecycle.
	 *
	 * Double booking is the rule that has to live here rather than in the client:
	 * two patients can submit the same slot concurrently on separate handler
	 * threads, so a client side availability check cannot prevent a clash. A
	 * uniqueness constraint on doctor and time, or a check inside the transaction,
	 * is what would actually enforce it.
	 */
	public Response schedule(Appointment appointment) {
		return null;

	}

}
