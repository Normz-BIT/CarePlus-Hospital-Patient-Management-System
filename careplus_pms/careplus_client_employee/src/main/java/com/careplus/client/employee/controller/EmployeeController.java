package com.careplus.client.employee.controller;




import com.careplus.common.client.net.ServerConnection;
import com.careplus.common.model.Employee;

/*
 * Execution Start for the Employee Application 
 *	can Login
 *	live chat with patients (8:00 a.m. – 7:00 p.m.) 
 * Doctors
 *  can view Assigned Patients
 * 	can add Diagnosis
 * 	can add Treatment Notes
 * 	can set/view Appointments (follow-ups)
 * Nurse
 * 	can record VitalSigns
 * 	can record patientObservations
 * 	can make nursingNotes
 * Receptionist 
 * 	view total Patient Requests (Appointments)
 *  view resolved Complaints
 *  view outstanding Complaints
 *  view all Complaints
 *  search Complaints by Category
 *  respond to patients 
 *  assign doctors/nurses
 *   
 */


public abstract class EmployeeController {

    protected final MainController main;
    protected final ServerConnection connection;
    protected final Employee employee;

    protected EmployeeController(MainController main, ServerConnection connection, Employee employee) {
        this.main = main;
        this.connection = connection;
        this.employee = employee;
    }

    //The Swing displays for this role.
    //public abstract  getView();

    //Wired to each role view's Logout button.
    public void logout() {
        main.logout();
    }
}
