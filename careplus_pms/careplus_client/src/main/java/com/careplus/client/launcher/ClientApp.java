package com.careplus.client.launcher;

import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import com.careplus.client.employee.controller.*;
import com.careplus.client.employee.view.*;

import com.careplus.client.patient.controller.*;
import com.careplus.client.patient.view.*;

import com.careplus.common.client.controller.LoginController;
import com.careplus.common.client.view.*;
import com.careplus.common.enums.UserRole;

/**
 * entry point for the CarePlus client (patients and employees).
 */
public class ClientApp {

	public static List<DashboardFeature> assignFeatures() {

		return List.of(

				// Patient features
				new DashboardFeature("Patient", "Appointments", Set.of(UserRole.PATIENT), () -> {
					Appointment view = new Appointment();
					new AppointmentController(view);
					return view;
				}), new DashboardFeature("Patient", "Complaints", Set.of(UserRole.PATIENT), () -> {
					Complaint view = new Complaint();
					new ComplaintController(view);
					return view;
				}), new DashboardFeature("Patient", "Medical Records", Set.of(UserRole.PATIENT), () -> {
					MedicalRecord view = new MedicalRecord();
					new MedicalRecordController(view);
					return view;
				}), new DashboardFeature("Patient", "Payments", Set.of(UserRole.PATIENT), () -> {
					Payment view = new Payment();
					new PaymentController(view);
					return view;
				}), new DashboardFeature("Patient", "Chat", Set.of(UserRole.PATIENT), () -> {
					Chat view = new Chat();
					new ChatController(view);
					return view;
				}),

				// Employee features
				new DashboardFeature("Employee", "Diagnosis", Set.of(UserRole.DOCTOR), () -> {
					Diagnosis view = new Diagnosis();
					new DiagnosisController(view);
					return view;
				}), new DashboardFeature("Employee", "Complaint Manager", Set.of(UserRole.RECEPTIONIST), () -> {
					EmployeeComplaint view = new EmployeeComplaint();
					new EmployeeComplaintController(view);
					return view;
				}), new DashboardFeature("Employee", "Staff Assignment", Set.of(UserRole.RECEPTIONIST), () -> {
					StaffAssignment view = new StaffAssignment();
					new StaffAssignmentController(view);
					return view;
				}), new DashboardFeature("Employee", "My Patients", Set.of(UserRole.DOCTOR), () -> {
					Patients view = new Patients();
					new PatientsController(view);
					return view;
				}), new DashboardFeature("Employee", "Doctors", Set.of(UserRole.DOCTOR, UserRole.RECEPTIONIST), () -> {
					Doctors view = new Doctors();
					new DoctorsController(view);
					return view;
				}), new DashboardFeature("Employee", "Nurse Station", Set.of(UserRole.NURSE), () -> {
					Vitals view = new Vitals();
					new VitalsController(view);
					return view;
				}), new DashboardFeature("Employee", "Staff Chat",
						Set.of(UserRole.DOCTOR, UserRole.NURSE, UserRole.RECEPTIONIST), () -> {
							StaffChat view = new StaffChat();
							new StaffChatController(view);
							return view;
						}));

	}

	public static void main(String[] args) {

		List<DashboardFeature> features = assignFeatures();

		SwingUtilities.invokeLater(() -> {
			Login login = new Login();
			new LoginController(login, features);
			login.setVisible(true);
		});
	}
}
