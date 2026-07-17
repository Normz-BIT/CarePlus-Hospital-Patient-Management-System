package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.DoctorsView;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class DoctorsController {
    private final DoctorsView view;

    public DoctorsController(DoctorsView view) {
        this.view = view;
        view.getBtnRefresh().addActionListener(e -> refresh());
        refresh();
    }

    @SuppressWarnings("unchecked")
    private void refresh() {
        Response res = Client.send(new Request(RequestType.GET_DOCTORS, "all", true));
        if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
            return;
        }
        view.clearTable();
        
        if (res.getData() instanceof List<?>) {
            for (Object element : (List<Object>) res.getData()) {
                view.addDoctor(element instanceof Object[] ? (Object[]) element : new Object[]{element});
            }
        }
    }
}
