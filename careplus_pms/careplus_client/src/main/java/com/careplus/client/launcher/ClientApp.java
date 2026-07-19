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
					AppointmentController controller = new AppointmentController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Patient", "Complaints", Set.of(UserRole.PATIENT), () -> {
					ComplaintView view = new ComplaintView();
					ComplaintController controller = new ComplaintController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Patient", "Medical Records", Set.of(UserRole.PATIENT), () -> {
					MedicalRecordView view = new MedicalRecordView();
					MedicalRecordController controller = new MedicalRecordController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Patient", "Payments", Set.of(UserRole.PATIENT), () -> {
					PaymentView view = new PaymentView();
					PaymentController controller = new PaymentController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Patient", "Chat", Set.of(UserRole.PATIENT), () -> {
					ChatView view = new ChatView();
					ChatController controller = new ChatController(view);
					view.registerActionListener(controller);
					return view;
				}),

				// Employee features
				new DashboardFeature("Employee", "Diagnosis", Set.of(UserRole.DOCTOR), () -> {
					DiagnosisView view = new DiagnosisView();
					DiagnosisController controller = new DiagnosisController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Employee", "Complaint Manager", Set.of(UserRole.RECEPTIONIST), () -> {
					EmployeeComplaintView view = new EmployeeComplaintView();
					EmployeeComplaintController controller = new EmployeeComplaintController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Employee", "Staff Assignment", Set.of(UserRole.RECEPTIONIST), () -> {
					StaffAssignmentView view = new StaffAssignmentView();
					StaffAssignmentController controller = new StaffAssignmentController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Employee", "My Patients", Set.of(UserRole.DOCTOR), () -> {
					PatientsView view = new PatientsView();
					PatientsController controller = new PatientsController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Employee", "Doctors", Set.of(UserRole.DOCTOR, UserRole.RECEPTIONIST), () -> {
					DoctorsView view = new DoctorsView();
					DoctorsController controller = new DoctorsController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Employee", "Nurse Station", Set.of(UserRole.NURSE), () -> {
					VitalsView view = new VitalsView();
					VitalsController controller = new VitalsController(view);
					view.registerActionListener(controller);
					return view;
				}), new DashboardFeature("Employee", "Staff Chat",
						Set.of(UserRole.DOCTOR, UserRole.NURSE, UserRole.RECEPTIONIST), () -> {
							StaffChatView view = new StaffChatView();
							StaffChatController controller = new StaffChatController(view);
							view.registerActionListener(controller);
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

				Login login = new Login();
				LoginController loginController = new LoginController(login, features);
				login.registerActionListener(loginController);
				login.setVisible(true);

				//initialise client connection
				new Client();
				
				logger.info("CarePlus login view opened successfully");

			} catch (Exception e) {

				// TODO
				logger.error("CarePlus client application could not be started", e);
			}
		});

	}
}
