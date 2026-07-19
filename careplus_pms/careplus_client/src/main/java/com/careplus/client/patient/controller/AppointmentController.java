package com.careplus.client.patient.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

public class AppointmentController {
	private final AppointmentView view;
	private static final Logger logger = LogManager.getLogger(AppointmentController.class);

	public AppointmentController(AppointmentView view) {
		this.view = view;
		loadLookups();
		refresh();
	}


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

			if (view.getTxtDate().getText().trim().isEmpty()
					|| view.getTxtTime().getText().trim().isEmpty()) {

				view.showMessage("Date and time are required.");
				logger.warn("Appointment rejected because the date or time was empty");

				return;
			}

			String appointmentDateTime = view.getTxtDate().getText().trim()
					+ " " + view.getTxtTime().getText().trim();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.parse(appointmentDateTime, formatter);
			
			appointment.setAppointmentDate(localDateTime);

			appointment.setReason(view.getTxtReason().getText().trim());

			appointment.setStatus(AppointmentStatus.SCHEDULED);

			logger.info("Appointment created: {}", appointment.toString());

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

		if (row < 0) {
			view.showMessage("Select an appointment to cancel.");
			logger.warn("Appointment cancellation attempted without selecting a record");

			return;
		}

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