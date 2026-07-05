package com.careplus.client.employee.controller;

import com.careplus.common.client.net.ServerConnection;
import com.careplus.common.model.Nurse;

public class NurseController extends EmployeeController{

	// private final DoctorView view;

    public NurseController(MainController main, ServerConnection connection, Nurse nurse) {
        super(main, connection, nurse);
       // this.view = new NurseView(nurse);
       // this.view.setLogoutHandler(this::logout);
        // TODO: wire record vital signs, patient observations,nursing notes actions here
    }

    /*@Override
    public  getView() {
        return view;
    }*/	
	
}
