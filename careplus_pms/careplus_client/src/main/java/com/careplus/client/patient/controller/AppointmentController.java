package com.careplus.client.patient.controller;

<<<<<<< HEAD
=======
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
>>>>>>> stash
import java.util.List;

<<<<<<< HEAD
import com.careplus.client.patient.view.Appointment;
=======
import javax.swing.JComboBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.patient.view.AppointmentView;
>>>>>>> stash
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Appointment Controller
 * Lets a patient book, cancel and review appointments
 *
 * SCHEDULE_APPOINTMENT, CANCEL_APPOINTMENT, GET_MY_APPOINTMENTS, GET_DOCTORS and
 * GET_DEPARTMENTS are all unrouted on the server, so every call below currently
 * returns an empty Response. The screen is written against the finished protocol
 * and will work once AppointmentService is implemented.
 */
public class AppointmentController {
    private final Appointment view;

<<<<<<< HEAD
    public AppointmentController(Appointment view) {
        this.view = view;
        init();
        loadLookups();
        refresh();
    }
=======
	public AppointmentController(AppointmentView view) {
		this.view = view;
		/*
		 * Three blocking server calls run before this frame is shown, two here and one
		 * in refresh, all on the Event Dispatch Thread. This is the slowest screen in
		 * the client to open.
		 */
		loadLookups();
		refresh();
	}
>>>>>>> stash

<<<<<<< HEAD
    private void init() {
        view.getBtnSchedule().addActionListener(e -> schedule());
        view.getBtnRefresh().addActionListener(e -> refresh());
        view.getBtnClear().addActionListener(e -> view.clearFields());
        view.getBtnCancel().addActionListener(e -> cancel());
        view.getBtnUpdate().addActionListener(e -> schedule());
    }
=======
>>>>>>> stash

<<<<<<< HEAD
    private Response send(Request request) { return new Client().send(request); }

    private void loadLookups() {
        Response doctors = send(new Request(RequestType.GET_DOCTORS, "role", "doctor"));
        if (doctors != null && Boolean.TRUE.equals(doctors.getSuccess())) fillCombo(view.getCboDoctor(), doctors.getData());
        Response departments = send(new Request(RequestType.GET_DEPARTMENTS, "type", "appointment"));
        if (departments != null && Boolean.TRUE.equals(departments.getSuccess())) fillCombo(view.getCboDepartment(), departments.getData());
    }
=======
	/*
	 * Combo contents come from the server rather than from a local enum, so the
	 * doctor list reflects who is actually on staff. Failures are silent by design:
	 * if either lookup fails the combo simply stays empty rather than blocking the
	 * whole screen from opening.
	 */
	private void loadLookups() {
		Response doctors = Client.send(new Request(RequestType.GET_DOCTORS, "role", "doctor"));
>>>>>>> stash

    @SuppressWarnings("unchecked")
    private void fillCombo(javax.swing.JComboBox<String> combo, Object data) {
        combo.removeAllItems();
        if (data instanceof List<?>) for (Object value : (List<Object>) data) combo.addItem(String.valueOf(value));
    }

<<<<<<< HEAD
    private void schedule() {
        if (view.getTxtDate().getText().trim().isEmpty() || view.getTxtTime().getText().trim().isEmpty()) {
            view.showMessage("Date and time are required.");
            return;
        }
        Request req = new Request();
        req.setType(RequestType.SCHEDULE_APPOINTMENT);
        req.putMap("doctor", String.valueOf(view.getCboDoctor().getSelectedItem()));
        req.putMap("department", String.valueOf(view.getCboDepartment().getSelectedItem()));
        req.putMap("date", view.getTxtDate().getText().trim());
        req.putMap("time", view.getTxtTime().getText().trim());
        Response res = send(req);
        view.showMessage(res == null ? "No response from server." : res.getMessage());
        refresh();
    }
=======
		Response departments = Client.send(new Request(RequestType.GET_DEPARTMENTS, "type", "appointment"));
>>>>>>> stash

    private void cancel() {
        int row = view.getTblAppointments().getSelectedRow();
        if (row < 0) { view.showMessage("Select an appointment to cancel."); return; }
        Object id = view.getTableModel().getValueAt(row, 0);
        Request req = new Request(RequestType.CANCEL_APPOINTMENT, "appointmentId", id);
        Response res = send(req);
        view.showMessage(res == null ? "No response from server." : res.getMessage());
        refresh();
    }

<<<<<<< HEAD
    @SuppressWarnings("unchecked")
    private void refresh() {
        Response res = send(new Request(RequestType.GET_MY_APPOINTMENTS, "patientId", "current"));
        if (res == null || !Boolean.TRUE.equals(res.getSuccess())) return;
        view.clearTable();
        if (res.getData() instanceof List<?>) for (Object row : (List<Object>) res.getData()) view.addAppointment((Object[]) row);
    }
}
=======
	@SuppressWarnings("unchecked")
	private void fillCombo(JComboBox<String> combo, Object data) {
		combo.removeAllItems();
>>>>>>> stash

<<<<<<< HEAD
=======
		if (data instanceof List<?>) {
			for (Object value : (List<Object>) data) {
				combo.addItem(String.valueOf(value));
			}
		}
	}

	public void schedule() {
		Request req = new Request();
		req.setType(RequestType.SCHEDULE_APPOINTMENT);

		Appointment appointment = new Appointment();

		try {

			if (view.getTxtDate().getText().trim().isEmpty()
					|| view.getTxtTime().getText().trim().isEmpty()) {

				view.showMessage("Date and time are required.");
				logger.warn("Appointment rejected because the date or time was empty");

				return;
			}

			/*
			 * Date and time are captured as two free text fields and joined before parsing,
			 * so the user is required to type an exact format with no picker to guide them.
			 * The same yyyy-MM-dd HH:mm:ss convention is duplicated in PatientsController,
			 * while DiagnosisController parses a date only value. Those three parsing rules
			 * are unrelated code that must nonetheless stay mutually consistent, and a
			 * shared formatter constant would be the natural fix.
			 */
			String appointmentDateTime = view.getTxtDate().getText().trim()
					+ " " + view.getTxtTime().getText().trim();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			/*
			 * Throws on any malformed input, which is what the catch below converts into
			 * the format hint shown to the user. That hint is a hardcoded string and must
			 * be updated by hand if this pattern ever changes.
			 */
			LocalDateTime localDateTime = LocalDateTime.parse(appointmentDateTime, formatter);

			appointment.setAppointmentDate(localDateTime);

			appointment.setReason(view.getTxtReason().getText().trim());

			/*
			 * Every new booking starts SCHEDULED, the only valid entry point of the
			 * status lifecycle. The client sets it rather than the server, so this is a
			 * convention rather than an enforced rule.
			 */
			appointment.setStatus(AppointmentStatus.SCHEDULED);

			logger.info("Appointment created: {}", appointment.toString());

			/*
			 * Doctor and department travel as separate map entries rather than on the
			 * Appointment itself, because the model has no field for either. Once
			 * Appointment carries proper patient and doctor references these three extra
			 * keys should collapse into the object.
			 *
			 * String.valueOf is used deliberately: it yields "null" rather than throwing
			 * when nothing is selected, so an empty combo produces a bad value instead of
			 * an exception. The server would need to reject that.
			 */
			req.putMap("appointment", appointment);
			req.putMap("patientId", MainDashboard.getCurrentUser().getPersonId());
			req.putMap("doctor", String.valueOf(view.getCboDoctor().getSelectedItem()));
			req.putMap("department", String.valueOf(view.getCboDepartment().getSelectedItem()));

			Response res = Client.send(req);

			view.showMessage(res == null ? "No response from server." : res.getMessage());

			if (res == null) {
				logger.error("No response received from server while scheduling appointment");
			} else {
				logger.info("Server appointment response: {}", res.getMessage());
			}

		} catch (Exception e) {

			logger.error("An error occurred while scheduling appointment", e);
			view.showMessage("Use the date and time format: yyyy-MM-dd HH:mm:ss");
		}

		refresh();

	}

	public void cancel() {
		int row = view.getTblAppointments().getSelectedRow();

		/*
		 * getSelectedRow returns -1 when nothing is highlighted, so this guard is what
		 * stops the lookup below from running against a non existent row.
		 */
		if (row < 0) {
			view.showMessage("Select an appointment to cancel.");
			logger.warn("Appointment cancellation attempted without selecting a record");

			return;
		}

		/*
		 * Column 0 holds the appointment ID, matching the order refresh builds each row
		 * in. The two methods share that column order as a convention, so any change to
		 * the columns in refresh needs the indices here changed with it.
		 */
		Object id = view.getTableModel().getValueAt(row, 0);
		Request req = new Request(RequestType.CANCEL_APPOINTMENT, "appointmentId", id);
		Response res = Client.send(req);

		view.showMessage(res == null ? "No response from server." : res.getMessage());

		if (res == null) {
			logger.error("No response received from server while cancelling appointment");
		} else {
			logger.info("Appointment cancellation response: {}", res.getMessage());
		}

		refresh();
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(
				new Request(
						RequestType.GET_MY_APPOINTMENTS,
						"patientId",
						MainDashboard.getCurrentUser().getPersonId()));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Appointment records could not be retrieved");
			return;
		}

		view.clearTable();

		for (Appointment row : (List<Appointment>) res.getData()) {

			Object[] viewRow = new Object[] {
					row.getAppointmentId(),
					row.getAppointmentDate(),
					row.getReason(),
					row.getStatus()
			};

			view.addAppointment(viewRow);
		}

		logger.info("Appointment records refreshed successfully");

	}
}
>>>>>>> stash
