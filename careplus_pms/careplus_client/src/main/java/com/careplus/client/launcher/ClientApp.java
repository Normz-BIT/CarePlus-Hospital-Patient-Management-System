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
	 *
	 * The one list of every feature in the client, and the only place we say which
	 * role can see what. Nothing inside any view or controller checks the role
	 * itself, so this is the method to look at when someone asks who can get to a
	 * given screen.
	 *
	 * Each entry hands over a factory instead of a ready-made screen, so the twelve
	 * of them only get built when someone clicks the menu item. That matters
	 * because every controller constructor does a blocking server call, so building
	 * them all at login would mean waiting on twelve round trips before the
	 * dashboard appears.
	 *
	 * That view then controller then registerActionListener pattern repeated below
	 * is just how we wire MVC in this project. There's no base class for it, so all
	 * three steps are written out for each feature: the controller needs the view
	 * to read its inputs, and the view needs the controller to handle its buttons,
	 * so the listener goes on last once both exist.
	 *
	 * "Doctors" is the only screen two roles can see, since doctors and
	 * receptionists both need the directory.
	 */
	public static List<DashboardFeature> assignFeatures() {

		/*
		 * List.of gives an unmodifiable list, so nothing can add or remove features
		 * after startup. This gets passed to LoginController and then on to every
		 * MainDashboard, so making it unchangeable keeps that sharing safe.
		 */
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
	 *
	 * Shared by both the patient and the employee client: there is one executable,
	 * and the role attached to whoever logs in decides which dashboard they get.
	 *
	 * The whole bootstrap runs inside invokeLater because Swing components must be
	 * created and touched only on the Event Dispatch Thread, construction included.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			try {
	
				logger.info("Starting CarePlus client application");

				//get list of controllers and views and who can access them
				List<DashboardFeature> features = assignFeatures();

				LoginView login = new LoginView();
				LoginController loginController = new LoginController(login, features);
				login.registerActionListener(loginController);
				login.setVisible(true);

				
				
				/*
				 * Connects after the window is already visible, so the user sees the login form
				 * immediately rather than waiting on the socket. Ordering it this way also
				 * means a server that is down does not block the UI from appearing.
				 *
				 * The instance is discarded because Client holds its socket in static fields,
				 * so this exists purely to establish the connection. Client.send would
				 * reconnect on demand anyway, making this a warm up rather than a
				 * prerequisite.
				 */
				new Client();

				logger.info("CarePlus login view opened successfully");

			} catch (Exception e) {

				/*
				 *A failure this early leaves no usable window, so the
				 * only thing left is to record why before the application dies.
				 */
				logger.error("CarePlus client application could not be started", e);
			}
		});

	}
}
