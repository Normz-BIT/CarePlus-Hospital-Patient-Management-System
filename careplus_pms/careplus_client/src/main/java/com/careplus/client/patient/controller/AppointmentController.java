package com.careplus.client.patient.controller;

import java.util.List;

import javax.swing.JComboBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.patient.view.AppointmentView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.enums.AppointmentStatus;
import com.careplus.common.model.Appointment;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.common.util.DateDisplay;

/*
 * Appointment Controller
 * Lets a patient book, cancel and look through their appointments.
 */
public class AppointmentController {
	private final AppointmentView view;
	private static final Logger logger = LogManager.getLogger(AppointmentController.class);

	public AppointmentController(AppointmentView view) {
		this.view = view;
		/*
		 * Three blocking server calls before this window even shows up, two in here and
		 * one in refresh, all on the Swing thread. Easily the slowest screen we have to
		 * open.
		 */
		loadLookups();
		refresh();
	}


	/*
	 * These combos are filled from the server rather than a local list, so the
	 * doctors shown are actually the ones on staff. Failures are quiet on purpose:
	 * if a lookup fails the combo just stays empty instead of stopping the whole
	 * screen from opening.
	 */
	private void loadLookups() {
		Response doctors = Client.send(new Request(RequestType.GET_DOCTORS, "role", "doctor"));

		if (doctors != null && Boolean.TRUE.equals(doctors.getSuccess())) {
			fillCombo(view.getCboDoctor(), doctors.getData());
		}

		Response departments = Client.send(new Request(RequestType.GET_DEPARTMENTS, "type", "appointment"));

		if (departments != null && Boolean.TRUE.equals(departments.getSuccess())) {
			fillCombo(view.getCboDepartment(), departments.getData());
		}
	}

	@SuppressWarnings("unchecked")
	private void fillCombo(JComboBox<String> combo, Object data) {
		combo.removeAllItems();

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

			/*
			 * Straight off the Day/Month/Year/Hour/Min spinners, so there's nothing to
			 * parse and nothing to validate. The old version joined two typed boxes and
			 * ran them through a yyyy-MM-dd HH:mm:ss formatter, which meant an empty check
			 * and a parse error message on every screen that took a date, all three of
			 * them expecting a slightly different format.
			 */
			appointment.setAppointmentDate(view.getPickerDate().getDateTime());

			appointment.setReason(view.getTxtReason().getText().trim());

			/*
			 * Every new booking starts at SCHEDULED. We set it here too, but don't rely on
			 * that: AppointmentService overwrites it server side anyway, exactly because
			 * anything set here is only a convention.
			 */
			appointment.setStatus(AppointmentStatus.SCHEDULED);

			logger.info("Appointment created: {}", appointment.toString());

			/*
			 * The doctor and department go as their own map entries rather than on the
			 * Appointment, because the doctor arrives as combo text like
			 * "STF0001 - Karen Reid" and the server pulls the real ID out of it.
			 *
			 * String.valueOf on purpose: it gives "null" instead of throwing when nothing
			 * is picked, so an empty combo sends a rubbish value rather than crashing
			 * here. The server catches that and rejects it.
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
			view.showMessage("Unable to schedule the appointment: " + e.getMessage());
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
					DateDisplay.withTime(row.getAppointmentDate()),
					row.getReason(),
					row.getStatus()
			};

			view.addAppointment(viewRow);
		}

		logger.info("Appointment records refreshed successfully");

	}
}