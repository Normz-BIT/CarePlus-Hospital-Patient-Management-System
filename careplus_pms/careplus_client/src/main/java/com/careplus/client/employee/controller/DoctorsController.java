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

        if (res == null || !res.getSuccess()) {

            logger.warn("Doctor records could not be retrieved");
            return;
        }

        view.clearTable();
        
        if (res.getData() instanceof List<?>) {
            for (Object element : (List<Object>) res.getData()) {

                if (element instanceof Doctor) {
                    Doctor doctor = (Doctor) element;

                    Object[] viewRow = new Object[] {
                            doctor.getPersonId(),
                            doctor.getFirstName(),
                            doctor.getLastName(),
                            doctor.getEmail(),
                            doctor.getPhone(),
                            doctor.getDepartment(),
                            doctor.getHireDate(),
                            doctor.getSpecialization(),
                            doctor.getLicenseNo()
                    };

                    view.addDoctor(viewRow);

                } else if (element instanceof Object[]) {
                    view.addDoctor((Object[]) element);
                }
            }
        }

        logger.info("Doctor records refreshed successfully");
    }
}