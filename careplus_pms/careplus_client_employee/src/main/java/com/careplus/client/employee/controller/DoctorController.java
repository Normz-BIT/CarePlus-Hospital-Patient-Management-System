package com.careplus.client.employee.controller;

import com.careplus.common.client.net.Client;
import com.careplus.common.model.Doctor;

public class DoctorController extends EmployeeController {

   // private final DoctorView view;

    public DoctorController(MainController main, Client connection, Doctor doctor) {
        super(main, connection, doctor);
       // this.view = new DoctorView(doctor);
       // this.view.setLogoutHandler(this::logout);
        // TODO: wire diagnosis, treatment-notes,appointment actions here
    }
    

    /*@Override
    public  getView() {
        return view;
    }*/
}