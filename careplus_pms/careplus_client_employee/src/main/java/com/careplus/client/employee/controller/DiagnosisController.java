package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.Diagnosis;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class DiagnosisController {
    private final Diagnosis view;

    public DiagnosisController(Diagnosis view) {
        this.view = view;
        init();
        loadStatus();
        refresh();
    }

    private void init() {
        view.getBtnSave().addActionListener(e -> save(RequestType.ADD_DIAGNOSIS));
        view.getBtnUpdate().addActionListener(e -> save(RequestType.UPDATE_DIAGNOSIS));
        view.getBtnRefresh().addActionListener(e -> refresh());
        view.getBtnClear().addActionListener(e -> view.clearFields());
    }

    private void loadStatus() {
        view.getCboStatus().removeAllItems();
        view.getCboStatus().addItem("Active");
        view.getCboStatus().addItem("Follow-up Required");
        view.getCboStatus().addItem("Closed");
    }

    private void save(RequestType type) {
        if (view.getTxtPatientId().getText().trim().isEmpty() || view.getTxtDiagnosis().getText().trim().isEmpty()) {
            view.showMessage("Patient ID and diagnosis are required.");
            return;
        }
        Request req = new Request();
        req.setType(type);
        req.putMap("patientId", view.getTxtPatientId().getText().trim());
        req.putMap("diagnosis", view.getTxtDiagnosis().getText().trim());
        req.putMap("treatment", view.getTxtTreatment().getText().trim());
        req.putMap("prescription", view.getTxtPrescription().getText().trim());
        req.putMap("status", String.valueOf(view.getCboStatus().getSelectedItem()));
        Response res = new Client().send(req);
        view.showMessage(res == null ? "No response from server." : res.getMessage());
        refresh();
    }

    @SuppressWarnings("unchecked")
    private void refresh() {
        Response res = new Client().send(new Request(RequestType.GET_DIAGNOSIS_RECORDS, "all", true));
        if (res == null || !Boolean.TRUE.equals(res.getSuccess())) return;
        view.clearTable();
        if (res.getData() instanceof List<?>) for (Object row : (List<Object>) res.getData()) view.addDiagnosis((Object[]) row);
    }
}
