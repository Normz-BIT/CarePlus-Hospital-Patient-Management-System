package com.careplus.client.employee.controller;

import java.util.List;

import com.careplus.client.employee.view.Doctors;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

<<<<<<< HEAD
=======
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
>>>>>>> stash
public class DoctorsController {
    private final Doctors view;

    public DoctorsController(Doctors view) {
        this.view = view;
        refresh();
    }

    @SuppressWarnings("unchecked")
<<<<<<< HEAD
<<<<<<< HEAD
    private void refresh() {
        Response res = new Client().send(new Request(RequestType.GET_DOCTORS, "all", true));
        if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {
=======
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
    public void refresh() {
        Response res = Client.send(
                new Request(
                        RequestType.GET_DOCTORS,
                        "all",
                        true));

        if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

            logger.warn("Doctor records could not be retrieved");
>>>>>>> stash
            return;
        }
        view.clearTable();
<<<<<<< HEAD
<<<<<<< HEAD
        
        if (res.getData() instanceof List<?>) {
            for (Object element : (List<Object>) res.getData()) {
                view.addDoctor(element instanceof Object[] ? (Object[]) element : new Object[]{element});
            }
=======
=======

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
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

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
>>>>>>> stash
        }
    }
}
