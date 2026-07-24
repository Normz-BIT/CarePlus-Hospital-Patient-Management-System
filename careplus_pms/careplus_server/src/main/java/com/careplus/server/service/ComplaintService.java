package com.careplus.server.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.enums.ComplaintStatus;
import com.careplus.common.model.Complaint;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * ComplaintService
 * The complaint workflow: patients submit them, receptionists sort and assign
 * them, doctors and nurses answer them.
 *
 * Same shape as AuthService and PaymentService: startSession, do the work, roll
 * back if it goes wrong, always endSession.
 */
public class ComplaintService extends BaseService {

	private static final Logger logger = LogManager.getLogger(ComplaintService.class);

	/*
	 * A new complaint always starts at SUBMITTED. We set both the status and the
	 * patient here on the server rather than trusting whatever arrived, otherwise a
	 * messed-with client could file a complaint as someone else or start one off
	 * already marked RESOLVED.
	 */
	public Response submit(Request request) {

		Complaint complaint = (Complaint) request.getParams().get("complaint");
		String patientId = (String) request.getParams().get("patientId");

		startSession();

		try {

			complaint.setPatientId(patientId);
			complaint.setStatus(ComplaintStatus.SUBMITTED);

			if (complaint.getDateSubmitted() == null) {
				complaint.setDateSubmitted(LocalDateTime.now());
			}

			session.persist(complaint);

			resp.setSuccess(true);
			resp.setMessage("Complaint submitted successfully");

			logger.info("Complaint submitted for patient {}", patientId);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to submit complaint");

			logger.error("Could not submit complaint for patient {}", patientId, e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * The patient's own complaint history. This is also where they see the reply,
	 * when it came and which employee wrote it, which the brief asks for.
	 */
	public Response getMyComplaints(Request request) {

		String patientId = (String) request.getParams().get("patientId");

		startSession();

		try {
			/*
			 * HQL with a placeholder, same as PaymentService.getPayments. Don't ever glue
			 * the ID into the string instead, or someone could type a crafted "patient ID"
			 * into the box and read everyone else's complaints.
			 */
			List<Complaint> complaints = session
					.createQuery("FROM Complaint WHERE patientId = ?1 ORDER BY dateSubmitted DESC", Complaint.class)
					.setParameter(1, patientId).list();

			resp.setSuccess(true);
			resp.setMessage("Complaints found");
			resp.setData(complaints);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get complaints");

			logger.error("Could not load complaints for patient {}", patientId, e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Every complaint in the system, for the receptionist's queue. The category
	 * search happens on the client over this list, so we just send the lot back,
	 * newest first.
	 */
	public Response getAllComplaints(Request request) {

		startSession();

		try {
			List<Complaint> complaints = session
					.createQuery("FROM Complaint ORDER BY dateSubmitted DESC", Complaint.class).list();

			resp.setSuccess(true);
			resp.setMessage("Complaints found");
			resp.setData(complaints);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get complaints");

			logger.error("Could not load the complaint list", e);

		} finally {
			endSession();
		}

		return resp;
	}

	public Response delete(Request request) {

		startSession();

		try {
			int complaintId = idFrom(request.getParams().get("complaintId"));

			Complaint complaint = session.find(Complaint.class, complaintId);

			if (complaint == null) {
				throw new IllegalArgumentException("No complaint with ID " + complaintId);
			}

			session.remove(complaint);

			resp.setSuccess(true);
			resp.setMessage("Complaint deleted");

			logger.info("Complaint {} deleted", complaintId);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to delete complaint");

			logger.error("Could not delete complaint", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Careful here. The complaint the client sends is nearly empty: just the ID, the
	 * reply text, the date and the new status. So we load the real row first and
	 * copy those few fields onto it. If we called merge on the one that arrived
	 * instead, it would null out the description and the patient, wiping the very
	 * complaint we're supposed to be answering.
	 */
	public Response respond(Request request) {

		Complaint reply = (Complaint) request.getParams().get("complaint");
		String employeeId = (String) request.getParams().get("employeeId");

		startSession();

		try {
			Complaint complaint = session.find(Complaint.class, reply.getComplaintId());

			if (complaint == null) {
				throw new IllegalArgumentException("No complaint with ID " + reply.getComplaintId());
			}

			complaint.setResponse(reply.getResponse());
			complaint.setResponseDate(reply.getResponseDate() == null ? LocalDateTime.now() : reply.getResponseDate());
			complaint.setRespondedBy(employeeId);

			if (reply.getStatus() != null) {
				complaint.setStatus(reply.getStatus());
			}

			/*
			 * No merge or persist call needed. The complaint is attached to this session,
			 * so the setters above get written out when endSession commits.
			 */

			resp.setSuccess(true);
			resp.setMessage("Response saved");

			logger.info("Employee {} responded to complaint {}", employeeId, complaint.getComplaintId());

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to respond to complaint");

			logger.error("Could not respond to complaint", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Looking for assignStaff? It's not here, it's in ReportService as a JDBC
	 * update, because the assignment screen is the bit we kept on plain JDBC to
	 * cover the brief's JDBC requirement.
	 */

	/*
	 * The two screens that send complaint IDs don't agree on the type: one sends a
	 * String and the other an Integer. Rather than chase that around the client we
	 * just take either one here.
	 */
	private int idFrom(Object value) {

		if (value instanceof Integer id) {
			return id;
		}

		return Integer.parseInt(String.valueOf(value).trim());
	}
}
