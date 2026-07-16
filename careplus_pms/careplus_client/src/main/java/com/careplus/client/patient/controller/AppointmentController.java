package com.careplus.client.patient.controller;

import java.util.List;

import com.careplus.client.patient.view.Appointment;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class AppointmentController {
    private final Appointment view;

    public AppointmentController(Appointment view) {
        this.view = view;
        init();
        loadLookups();
        refresh();
    }

    private void init() {
        view.getBtnSchedule().addActionListener(e -> schedule());
        view.getBtnRefresh().addActionListener(e -> refresh());
        view.getBtnClear().addActionListener(e -> view.clearFields());
        view.getBtnCancel().addActionListener(e -> cancel());
        view.getBtnUpdate().addActionListener(e -> schedule());
    }

    private Response send(Request request) { return Client.send(request); }

    private void loadLookups() {
        Response doctors = send(new Request(RequestType.GET_DOCTORS, "role", "doctor"));
        if (doctors != null && Boolean.TRUE.equals(doctors.getSuccess())) fillCombo(view.getCboDoctor(), doctors.getData());
        Response departments = send(new Request(RequestType.GET_DEPARTMENTS, "type", "appointment"));
        if (departments != null && Boolean.TRUE.equals(departments.getSuccess())) fillCombo(view.getCboDepartment(), departments.getData());
    }

    @SuppressWarnings("unchecked")
    private void fillCombo(javax.swing.JComboBox<String> combo, Object data) {
        combo.removeAllItems();
        if (data instanceof List<?>) for (Object value : (List<Object>) data) combo.addItem(String.valueOf(value));
    }

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

    private void cancel() {
        int row = view.getTblAppointments().getSelectedRow();
        if (row < 0) { view.showMessage("Select an appointment to cancel."); return; }
        Object id = view.getTableModel().getValueAt(row, 0);
        Request req = new Request(RequestType.CANCEL_APPOINTMENT, "appointmentId", id);
        Response res = send(req);
        view.showMessage(res == null ? "No response from server." : res.getMessage());
        refresh();
    }

    @SuppressWarnings("unchecked")
    private void refresh() {
        Response res = send(new Request(RequestType.GET_MY_APPOINTMENTS, "patientId", "current"));
        if (res == null || !Boolean.TRUE.equals(res.getSuccess())) return;
        view.clearTable();
        if (res.getData() instanceof List<?>) for (Object row : (List<Object>) res.getData()) view.addAppointment((Object[]) row);
    }
}

