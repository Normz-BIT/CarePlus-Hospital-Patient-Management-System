package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.EmployeeComplaint;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class ComplaintController {
    private final EmployeeComplaint view;

    public ComplaintController(EmployeeComplaint view) {
        this.view = view;
        init();
        loadCombos();
        refresh();
    }

    private void init() {
        view.getBtnAssign().addActionListener(e -> assign());
        view.getBtnResolve().addActionListener(e -> resolve());
        view.getBtnRefresh().addActionListener(e -> refresh());
        view.getBtnClear().addActionListener(e -> view.clearFields());
    }

    private void loadCombos() {
        add(view.getCboCategory(), "Medical", "Billing", "Appointment", "Staff", "Other");
        add(view.getCboPriority(), "Low", "Medium", "High");
        add(view.getCboStatus(), "Pending", "Assigned", "In Progress", "Resolved", "Closed");
    }

    private void add(javax.swing.JComboBox<String> box, String... items) {
        box.removeAllItems();
        for (String item : items) box.addItem(item);
    }

    private void assign() {
        Request req = new Request();
        req.setType(RequestType.ASSIGN_STAFF);
        req.putMap("complaintId", view.getTxtComplaintId().getText().trim());
        req.putMap("remarks", view.getTxtRemarks().getText().trim());
        Response res = new Client().send(req);
        view.showMessage(res == null ? "No response from server." : res.getMessage());
        refresh();
    }

    private void resolve() {
        Request req = new Request();
        req.setType(RequestType.RESPOND_TO_COMPLAINT);
        req.putMap("complaintId", view.getTxtComplaintId().getText().trim());
        req.putMap("status", String.valueOf(view.getCboStatus().getSelectedItem()));
        req.putMap("response", view.getTxtRemarks().getText().trim());
        Response res = new Client().send(req);
        view.showMessage(res == null ? "No response from server." : res.getMessage());
        refresh();
    }

    @SuppressWarnings("unchecked")
    private void refresh() {
        Response res = new Client().send(new Request(RequestType.GET_ALL_COMPLAINTS, "all", true));
        if (res == null || !Boolean.TRUE.equals(res.getSuccess())) return;
        view.clearTable();
        if (res.getData() instanceof List<?>) for (Object row : (List<Object>) res.getData()) view.addComplaint((Object[]) row);
    }
}
