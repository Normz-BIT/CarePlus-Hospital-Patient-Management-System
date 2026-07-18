package com.careplus.client.launcher;

import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.controller.*;
import com.careplus.client.employee.view.*;

import com.careplus.client.patient.controller.*;
import com.careplus.client.patient.view.*;

import com.careplus.common.client.controller.LoginController;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.*;
import com.careplus.common.enums.UserRole;

/**
 * entry point for the CarePlus client (patients and employees).
 */
public class ClientApp {

	private static final Logger logger = LogManager.getLogger(ClientApp.class);

	/*
	 * Assign Views and Controllers to User Roles
	 */
	public static List<DashboardFeature> assignFeatures() {

		return List.of(

				// Patient features
				new DashboardFeature("Patient", "Appointments", Set.of(UserRole.PATIENT), () -> {
					AppointmentView view = new AppointmentView();
					new AppointmentController(view);
					return view;
				}), new DashboardFeature("Patient", "Complaints", Set.of(UserRole.PATIENT), () -> {
					ComplaintView view = new ComplaintView();
					new ComplaintController(view);
					return view;
				}), new DashboardFeature("Patient", "Medical Records", Set.of(UserRole.PATIENT), () -> {
					MedicalRecordView view = new MedicalRecordView();
					new MedicalRecordController(view);
					return view;
				}), new DashboardFeature("Patient", "Payments", Set.of(UserRole.PATIENT), () -> {
					PaymentView view = new PaymentView();
					new PaymentController(view);
					return view;
				}), new DashboardFeature("Patient", "Chat", Set.of(UserRole.PATIENT), () -> {
					ChatView view = new ChatView();
					new ChatController(view);
					return view;
				}),

				// Employee features
				new DashboardFeature("Employee", "Diagnosis", Set.of(UserRole.DOCTOR), () -> {
					DiagnosisView view = new DiagnosisView();
					new DiagnosisController(view);
					return view;
				}), new DashboardFeature("Employee", "Complaint Manager", Set.of(UserRole.RECEPTIONIST), () -> {
					EmployeeComplaintView view = new EmployeeComplaintView();
					new EmployeeComplaintController(view);
					return view;
				}), new DashboardFeature("Employee", "Staff Assignment", Set.of(UserRole.RECEPTIONIST), () -> {
					StaffAssignmentView view = new StaffAssignmentView();
					new StaffAssignmentController(view);
					return view;
				}), new DashboardFeature("Employee", "My Patients", Set.of(UserRole.DOCTOR), () -> {
					PatientsView view = new PatientsView();
					new PatientsController(view);
					return view;
				}), new DashboardFeature("Employee", "Doctors", Set.of(UserRole.DOCTOR, UserRole.RECEPTIONIST), () -> {
					DoctorsView view = new DoctorsView();
					new DoctorsController(view);
					return view;
				}), new DashboardFeature("Employee", "Nurse Station", Set.of(UserRole.NURSE), () -> {
					VitalsView view = new VitalsView();
					new VitalsController(view);
					return view;
				}), new DashboardFeature("Employee", "Staff Chat",
						Set.of(UserRole.DOCTOR, UserRole.NURSE, UserRole.RECEPTIONIST), () -> {
							StaffChatView view = new StaffChatView();
							new StaffChatController(view);
							return view;
						}));

	}

	/*
	 * Start CarePlus Client Application
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			try {

				logger.info("Starting CarePlus client application");

				//get list of controllers and views and who can access them
				List<DashboardFeature> features = assignFeatures();

				//initialise client connection
				new Client();

				Login login = new Login();
				new LoginController(login, features);
				login.setVisible(true);

				logger.info("CarePlus login view opened successfully");

			} catch (Exception e) {

				// TODO
				logger.error("CarePlus client application could not be started", e);
			}
		});

	}
}
