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
import com.careplus.common.util.DateDisplay;

/*
 * Doctors Controller
 * Fetches and shows the doctors directory
 *
 * The only read-only screen on the employee side, and the only one two roles can
 * see: doctors browsing colleagues, and receptionists who need the list when
 * they're assigning staff to a complaint.
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
         * Casting straight to Doctor instead of Person so we can reach the
         * specialization and licence columns below. This only works because the request
         * specifically asked for doctors: anything else coming back would blow up here
         * with a ClassCastException.
         */
        for (Doctor row : (List<Doctor>) res.getData()) {

            Object[] viewRow = new Object[] {
                    row.getPersonId(),
                    row.getFirstName(),
                    row.getLastName(),
                    row.getEmail(),
                    row.getPhone(),
                    row.getDepartment(),
                    DateDisplay.dateOnly(row.getHireDate()),
                    row.getSpecialization(),
                    row.getLicenseNo()
            };

            view.addDoctor(viewRow);
        }

        logger.info("Doctor records refreshed successfully");
    }
}