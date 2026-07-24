package com.careplus.server.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.enums.AppointmentStatus;
import com.careplus.common.enums.Department;
import com.careplus.common.model.Appointment;
import com.careplus.common.model.Doctor;
import com.careplus.common.model.Employee;
import com.careplus.common.model.Patient;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * AppointmentService
 * Booking, looking up and cancelling appointments, plus the doctor, patient and
 * department lists that the screens fill their combo boxes from.
 */
public class AppointmentService extends BaseService {

	private static final Logger logger = LogManager.getLogger(AppointmentService.class);

	/*
	 * Patient books an appointment. The doctor comes in as the combo box text
	 * ("STF0001 - Karen Reid") since that's what getDoctors below sent them, so we
	 * chop the ID back off the front. Checking it here means a doctor we can't find
	 * gives a readable message instead of a foreign key error at commit time.
	 */
	public Response schedule(Request request) {

		Appointment appointment = (Appointment) request.getParams().get("appointment");
		String patientId = (String) request.getParams().get("patientId");
		String doctorDisplay = (String) request.getParams().get("doctor");

		startSession();

		try {

			String doctorId = idFromDisplay(doctorDisplay);

			if (session.find(Doctor.class, doctorId) == null) {
				throw new IllegalArgumentException("No doctor matches \"" + doctorDisplay + "\"");
			}

			appointment.setPatientId(patientId);
			appointment.setDoctorId(doctorId);
			/*
			 * We set the status here rather than taking whatever the client sent. Every new
			 * booking starts at SCHEDULED, no matter what the request says.
			 */
			appointment.setStatus(AppointmentStatus.SCHEDULED);

			session.persist(appointment);

			resp.setSuccess(true);
			resp.setMessage("Appointment scheduled");

			logger.info("Appointment booked for patient {} with doctor {}", patientId, doctorId);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to schedule appointment: " + e.getMessage());

			logger.error("Could not schedule appointment for patient {}", patientId, e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Doctor books a follow-up for a patient. Same write as schedule above, except
	 * here the doctor is whoever is signed in and the patient ID was typed by hand,
	 * so this time it's the patient we check exists before writing anything.
	 */
	public Response scheduleFollowUp(Request request) {

		Appointment appointment = (Appointment) request.getParams().get("appointment");
		String patientId = (String) request.getParams().get("patientId");
		String doctorId = (String) request.getParams().get("doctorId");

		startSession();

		try {

			String normalisedId = patientId == null ? "" : patientId.trim().toUpperCase();

			if (session.find(Patient.class, normalisedId) == null) {
				throw new IllegalArgumentException("No patient with ID " + patientId);
			}

			appointment.setPatientId(normalisedId);
			appointment.setDoctorId(doctorId);
			appointment.setStatus(AppointmentStatus.SCHEDULED);

			session.persist(appointment);

			resp.setSuccess(true);
			resp.setMessage("Follow-up scheduled for " + normalisedId);

			logger.info("Follow-up booked for patient {} by doctor {}", normalisedId, doctorId);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to schedule follow-up: " + e.getMessage());

			logger.error("Could not schedule follow-up for patient {}", patientId, e);

		} finally {
			endSession();
		}

		return resp;
	}

	public Response getMyAppointments(Request request) {

		String patientId = (String) request.getParams().get("patientId");

		startSession();

		try {
			List<Appointment> appointments = session
					.createQuery("FROM Appointment WHERE patientId = ?1 ORDER BY appointmentDate DESC",
							Appointment.class)
					.setParameter(1, patientId).list();

			resp.setSuccess(true);
			resp.setMessage("Appointments found");
			resp.setData(appointments);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get appointments");

			logger.error("Could not load appointments for patient {}", patientId, e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Cancelling just flips the status instead of deleting the row, so the booking
	 * still turns up in the patient's history showing what happened to it.
	 */
	public Response cancel(Request request) {

		startSession();

		try {
			Object raw = request.getParams().get("appointmentId");
			int appointmentId = raw instanceof Integer id ? id : Integer.parseInt(String.valueOf(raw).trim());

			Appointment appointment = session.find(Appointment.class, appointmentId);

			if (appointment == null) {
				throw new IllegalArgumentException("No appointment with ID " + appointmentId);
			}

			appointment.setStatus(AppointmentStatus.CANCELLED);

			resp.setSuccess(true);
			resp.setMessage("Appointment cancelled");

			logger.info("Appointment {} cancelled", appointmentId);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to cancel appointment");

			logger.error("Could not cancel appointment", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * This one feeds two screens that want different things:
	 *
	 * - the booking combo (sends "role") wants text, because fillCombo on the
	 *   client just calls String.valueOf on whatever we send. We format them as
	 *   "STF0001 - Karen Reid" so schedule() above can get the ID back out.
	 * - the Doctors table (sends "all") wants the actual Doctor objects, because
	 *   DoctorsController reads the individual fields off each row.
	 */
	public Response getDoctors(Request request) {

		boolean wantEntities = request.getParams().containsKey("all");

		startSession();

		try {

			List<Doctor> doctors = session.createQuery("FROM Doctor ORDER BY personId", Doctor.class).list();

			if (wantEntities) {
				resp.setData(doctors);
			} else {
				resp.setData(doctors.stream()
						.map(d -> d.getPersonId() + " - " + d.getFullName())
						.toList());
			}

			resp.setSuccess(true);
			resp.setMessage("Doctors found");

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get doctors");

			logger.error("Could not load the doctor list", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * "PAT0001 - Andre Campbell" text for the staff screens that pick a patient
	 * from a combo (staff chat and vitals). Same idea as the doctor combo above,
	 * and the controller pulls the ID back off the front the same way.
	 */
	public Response getPatients(Request request) {

		startSession();

		try {
			List<Patient> patients = session.createQuery("FROM Patient ORDER BY personId", Patient.class).list();

			resp.setData(patients.stream()
					.map(p -> p.getPersonId() + " - " + p.getFullName())
					.toList());

			resp.setSuccess(true);
			resp.setMessage("Patients found");

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get patients");

			logger.error("Could not load the patient list", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * "STF0001 - Karen Reid (DOCTOR)" text for the patient's chat screen, so a
	 * patient can pick the actual person they want instead of just a job title.
	 *
	 * The role goes on the end because someone looking at a list of names has no
	 * other way of telling which one is the doctor and which is the receptionist.
	 *
	 * Employee not Person, so this is staff only. A patient has no business being
	 * offered other patients to message.
	 */
	public Response getStaff(Request request) {

		startSession();

		try {
			List<Employee> staff = session.createQuery("FROM Employee ORDER BY personId", Employee.class).list();

			resp.setData(staff.stream()
					.map(e -> e.getPersonId() + " - " + e.getFullName() + " (" + e.getRole() + ")")
					.toList());

			resp.setSuccess(true);
			resp.setMessage("Staff found");

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get staff");

			logger.error("Could not load the staff list", e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Department names for the booking combo. These come straight off the
	 * Department enum rather than a SELECT DISTINCT over employee, because the enum
	 * is now the one place departments are defined and the database column is built
	 * from the same list. Querying would just be a slower way to get the same
	 * answer.
	 */
	public Response getDepartments(Request request) {

		resp = new Response();

		ArrayList<String> departments = new ArrayList<>();

		for (Department department : Department.values()) {
			departments.add(department.name());
		}

		resp.setSuccess(true);
		resp.setMessage("Departments found");
		resp.setData(departments);

		return resp;
	}

	/*
	 * A doctor's patient list. We never built an assignment table, so we work it
	 * out from the two places a doctor-patient link actually exists: complaints a
	 * receptionist assigned to them, and appointments booked with them. DISTINCT
	 * because the same patient often turns up through both.
	 */
	public Response getAssignedPatients(Request request) {

		String doctorId = (String) request.getParams().get("doctorId");

		startSession();

		try {
			List<Patient> patients = session.createQuery(
					"SELECT DISTINCT p FROM Patient p WHERE "
							+ "p.personId IN (SELECT c.patientId FROM Complaint c WHERE c.assignedTo = ?1) "
							+ "OR p.personId IN (SELECT a.patientId FROM Appointment a WHERE a.doctorId = ?1)",
					Patient.class)
					.setParameter(1, doctorId).list();

			resp.setSuccess(true);
			resp.setMessage("Assigned patients found");
			resp.setData(patients);

		} catch (Exception e) {

			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Failed to get assigned patients");

			logger.error("Could not load assigned patients for doctor {}", doctorId, e);

		} finally {
			endSession();
		}

		return resp;
	}

	/*
	 * Pulls "STF0001" back out of "STF0001 - Karen Reid". A plain ID on its own
	 * works too, since then there's no " - " to cut at.
	 */
	private String idFromDisplay(String display) {

		if (display == null || display.trim().isEmpty() || "null".equals(display)) {
			throw new IllegalArgumentException("No doctor was selected");
		}

		int dash = display.indexOf(" - ");

		return (dash < 0 ? display : display.substring(0, dash)).trim().toUpperCase();
	}
}
