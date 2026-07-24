package com.careplus.server.net;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.server.service.AppointmentService;
import com.careplus.server.service.AuthService;
import com.careplus.server.service.ChatService;
import com.careplus.server.service.ComplaintService;
import com.careplus.server.service.MedicalRecordService;
import com.careplus.server.service.PaymentService;
import com.careplus.server.service.ReportService;

/*
 * ClientHandler
 * One of these per connected client, each on its own thread. This is what makes
 * the server handle several clients at once.
 *
 * It's the server end of our protocol: read a Request in a loop, pick a service
 * based on the RequestType, write back a Response. Since every client gets its
 * own instance, nothing in here is shared between connections. The only thing in
 * the whole request path that really is shared is the Hibernate SessionFactory,
 * and we get at that through HibernateUtil.
 */
public class ClientHandler extends Thread {
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	/*
	 * Each handler builds its own services rather than sharing them or making them
	 * static. if two client threads shared one
	 * service object, one request could commit or close the other one's transaction.
	 */
	private AuthService authservice;
	private PaymentService paymentService;
	private ComplaintService complaintService;
	private AppointmentService appointmentService;
	private MedicalRecordService medicalRecordService;
	private ChatService chatService;
	private ReportService reportService;

	private static final Logger logger = LogManager.getLogger(Server.class);

	public ClientHandler(Socket socket) {
		this.socket = socket;
		authservice = new AuthService();
		paymentService = new PaymentService();
		complaintService = new ComplaintService();
		appointmentService = new AppointmentService();
		medicalRecordService = new MedicalRecordService();
		chatService = new ChatService();
		reportService = new ReportService();

	}

	/*
	 * Says whether the handshake worked, so run() can bail out cleanly instead of
	 * carrying on with null streams and crashing on the first read.
	 */
	private boolean getStreams() {
		try {
			/*
			 * Output first, then input, and the client does the same.
			 * building an ObjectInputStream blocks until it reads a header that the other
			 * side's ObjectOutputStream sends when it's created. If both ends built their
			 * input stream first they'd sit there waiting on each other forever.
			 */
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());

			return true;
		} catch (IOException ex) {
			logger.error("Stream handshake failed for {}", socket.getRemoteSocketAddress(), ex);

			return false;
		}
	}

	private void closeConnection() {

		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
				logger.info("Socket closed. Thread terminating.");
			}
		} catch (IOException e) {
			logger.error("Failed to close socket: " + e.getMessage());
		}

	}

	/*
	 * Shuts this client's socket so the blocked read in run() wakes up and the
	 * thread can finish. Server.stop() calls this on everyone.
	 */
	public void disconnect() {
		closeConnection();
	}

	/*
	 * Strictly one Response per Request, in order. The client blocks waiting for a
	 * reply after every send, so this loop has to answer exactly once every time.
	 * Miss a write and that client hangs forever. Write twice and the stream gets
	 * out of step, so every reply after that lands against the wrong request.
	 *
	 * The while(true)  ends when the socket closes, which makes
	 * readObject() throw and drops us into the finally below. That's how
	 * Server.stop() shuts these threads down without needing a shared flag.
	 */
	@Override
	
	public void run() {
		try {

			if (!this.getStreams()) {
				// Handshake failed, so there's no stream to read. The finally below
				// still closes the socket.
				return;
			}

			while (true) {

				logger.debug("Waiting for the next request...");
				Request req = (Request) inputStream.readObject();

				RequestType reqtype = req.getType();

				Response resp = new Response();

				/*
				 * Switching on RequestType keeps this easy to add to
				 * a new feature means a new enum value and a new case here, 
				 * and the read/write loop around it doesn't get touched.
				 */
				switch (reqtype) {

				case LOGIN:
					resp = authservice.login(req);
					break;

				// Payments
				case MAKE_PAYMENT:
					resp = paymentService.pay(req);
					break;
				case GET_MY_PAYMENTS:
					resp = paymentService.getPayments(req);
					break;

				// Complaints
				case SUBMIT_COMPLAINT:
					resp = complaintService.submit(req);
					break;
				case GET_MY_COMPLAINTS:
					resp = complaintService.getMyComplaints(req);
					break;
				case GET_ALL_COMPLAINTS:
					resp = complaintService.getAllComplaints(req);
					break;
				case DELETE_COMPLAINT:
					resp = complaintService.delete(req);
					break;
				case RESPOND_TO_COMPLAINT:
					resp = complaintService.respond(req);
					break;

				// Appointments and the lookups the booking screen needs
				case SCHEDULE_APPOINTMENT:
					resp = appointmentService.schedule(req);
					break;
				case SCHEDULE_FOLLOWUP:
					resp = appointmentService.scheduleFollowUp(req);
					break;
				case GET_MY_APPOINTMENTS:
					resp = appointmentService.getMyAppointments(req);
					break;
				case CANCEL_APPOINTMENT:
					resp = appointmentService.cancel(req);
					break;
				case GET_DOCTORS:
					resp = appointmentService.getDoctors(req);
					break;
				case GET_PATIENTS:
					resp = appointmentService.getPatients(req);
					break;
				case GET_STAFF:
					resp = appointmentService.getStaff(req);
					break;
				case GET_DEPARTMENTS:
					resp = appointmentService.getDepartments(req);
					break;
				case GET_ASSIGNED_PATIENTS:
					resp = appointmentService.getAssignedPatients(req);
					break;

				// Medical records and vitals
				case ADD_DIAGNOSIS:
					resp = medicalRecordService.addDiagnosis(req);
					break;
				case UPDATE_DIAGNOSIS:
					resp = medicalRecordService.updateDiagnosis(req);
					break;
				case GET_DIAGNOSIS_RECORDS:
					resp = medicalRecordService.getDiagnosisRecords(req);
					break;
				case GET_MEDICAL_RECORDS:
					resp = medicalRecordService.getMedicalRecords(req);
					break;
				case RECORD_VITALS:
					resp = medicalRecordService.recordVitals(req);
					break;
				case GET_ASSIGNED_CASES:
					resp = medicalRecordService.getAssignedCases(req);
					break;

				// Chat
				case CHAT_SEND:
					resp = chatService.send(req);
					break;
				case CHAT_POLL:
					resp = chatService.poll(req);
					break;

				// The JDBC side: assignments and the dashboard numbers
				case ASSIGN_STAFF:
					resp = reportService.assignStaff(req);
					break;
				case GET_STAFF_ASSIGNMENTS:
					resp = reportService.getStaffAssignments(req);
					break;
				case GET_DASHBOARD_STATS:
					resp = reportService.getDashboardStats(req);
					break;

				default:
					
					resp.setSuccess(false);
					resp.setMessage("The server has no handler for " + reqtype);

					logger.warn("Unhandled request type {}", reqtype);
					break;

				}

				/*
				 * New Response object every time round the loop, never reused.
				 * ObjectOutputStream remembers objects it has already written, so if we sent
				 * the same instance twice the client would just get the first version again
				 * even after we'd changed what was in it.
				 *
				 * flush() after the write or the reply can sit in the buffer and the client
				 * waits for something we already "sent".
				 */
				outputStream.writeObject(resp);
				outputStream.flush();
			}

		} catch (ClassNotFoundException e) {
			/*
			 * The client sent a class this server doesn't have. In practice that means the
			 * two sides were built against different versions of careplus_common, so
			 * rebuild both.
			 */
			logger.error("Class not found Exception:" + e.getMessage());

		} catch (IOException e) {
			/*
			 * This is the normal way things end as well as the error path. A client
			 * disconnecting, or Server.stop() closing the socket, both look exactly like
			 * this from inside the blocked read, so it isn't necessarily a problem.
			 */
			logger.error("Error:" + e.getMessage());
		}

		finally {

			/*
			 * Runs even if the socket is already shut, since closeConnection checks
			 * isClosed() first. 
			 */
			closeConnection();
		}

	}
}