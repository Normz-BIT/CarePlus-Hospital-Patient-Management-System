package com.careplus.client.employee.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.DoctorsView;
import com.careplus.common.client.net.Client;
import com.careplus.common.model.Doctor;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

/*
 * Doctors Controller
 * Retrieves and displays the doctors directory
 */
public class DoctorsController {
    private final DoctorsView view;
    private static final Logger logger = LogManager.getLogger(DoctorsController.class);

    public DoctorsController(DoctorsView view) {
        this.view = view;
        view.getBtnRefresh().addActionListener(e -> refresh());
        refresh();
    }

    /*
     * View All Doctors
     */
    @SuppressWarnings("unchecked")
    private void refresh() {
        Response res = Client.send(
                new Request(
                        RequestType.GET_DOCTORS,
                        "all",
                        true));

        if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

            logger.warn("Doctor records could not be retrieved");
            return;
        }

        view.clearTable();

        for (Doctor row : (List<Doctor>) res.getData()) {

            Object[] viewRow = new Object[] {
                    row.getPersonId(),
                    row.getFirstName(),
                    row.getLastName(),
                    row.getEmail(),
                    row.getPhone(),
                    row.getDepartment(),
                    row.getHireDate(),
                    row.getSpecialization(),
                    row.getLicenseNo()
            };

            view.addDoctor(viewRow);
        }

        logger.info("Doctor records refreshed successfully");
    }
}