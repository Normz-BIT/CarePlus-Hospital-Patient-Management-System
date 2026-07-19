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
 *
 * The only read only feature on the employee side, and the only one visible to
 * two roles: doctors browse colleagues, receptionists need the list when
 * assigning staff to a complaint.
 *
 * GET_DOCTORS is unrouted on the server, so the directory is currently empty.
 * Note this also depends on Doctor being a mapped entity, which it is not yet, so
 * this table cannot populate until those annotations are added.
 */
public class DoctorsController {
    private final DoctorsView view;
    private static final Logger logger = LogManager.getLogger(DoctorsController.class);

    public DoctorsController(DoctorsView view) {
        this.view = view;
        refresh();
    }

    /*
     * View All Doctors
     */
    @SuppressWarnings("unchecked")
    public void refresh() {
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

        /*
         * Casting straight to Doctor rather than Person, so the specialization and
         * licence columns below are reachable. This only holds because the request asks
         * specifically for doctors: a response carrying any other Person subclass would
         * fail here with a ClassCastException.
         */
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