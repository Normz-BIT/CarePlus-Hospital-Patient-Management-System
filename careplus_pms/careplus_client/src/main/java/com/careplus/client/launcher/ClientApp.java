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

<<<<<<< HEAD
=======
	private static final Logger logger = LogManager.getLogger(ClientApp.class);

	/*
	 * Assign Views and Controllers to User Roles
	 *
	 * The single registry of every feature in the client, and the only place role
	 * based access is declared. Nothing inside any view or controller checks the
	 * user's role, so this method is where to look when asking who can reach a given
	 * screen.
	 *
	 * Each entry supplies a factory rather than an instance, so the twelve screens
	 * are built lazily on first menu click. That matters because every controller
	 * constructor performs a blocking server call: constructing all twelve eagerly
	 * would stall login behind twelve round trips.
	 *
	 * The repeated view then controller then registerActionListener sequence is the
	 * project's MVC wiring convention. There is no base controller or view class, so
	 * the three step handshake is written out per feature: the controller needs the
	 * view to read its inputs, and the view needs the controller to handle its
	 * buttons, so the listener is attached after both exist.
	 *
	 * Note "Doctors" is the only feature visible to two roles, since both doctors
	 * and receptionists need the directory when assigning staff.
	 */
>>>>>>> stash
	public static List<DashboardFeature> assignFeatures() {

		/*
		 * Immutable, so the feature list cannot be altered after startup. It is shared
		 * with LoginController and then with every MainDashboard, and an unmodifiable
		 * list makes that sharing safe.
		 */
		return List.of(

				// Patient features
				new DashboardFeature("Patient", "Appointments", Set.of(UserRole.PATIENT), () -> {
<<<<<<< HEAD
					Appointment view = new Appointment();
					new AppointmentController(view);
=======
					AppointmentView view = new AppointmentView();
					AppointmentController controller = new AppointmentController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Patient", "Complaints", Set.of(UserRole.PATIENT), () -> {
<<<<<<< HEAD
					Complaint view = new Complaint();
					new ComplaintController(view);
=======
					ComplaintView view = new ComplaintView();
					ComplaintController controller = new ComplaintController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Patient", "Medical Records", Set.of(UserRole.PATIENT), () -> {
<<<<<<< HEAD
					MedicalRecord view = new MedicalRecord();
					new MedicalRecordController(view);
=======
					MedicalRecordView view = new MedicalRecordView();
					MedicalRecordController controller = new MedicalRecordController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Patient", "Payments", Set.of(UserRole.PATIENT), () -> {
<<<<<<< HEAD
					Payment view = new Payment();
					new PaymentController(view);
=======
					PaymentView view = new PaymentView();
					PaymentController controller = new PaymentController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Patient", "Chat", Set.of(UserRole.PATIENT), () -> {
<<<<<<< HEAD
					Chat view = new Chat();
					new ChatController(view);
=======
					ChatView view = new ChatView();
					ChatController controller = new ChatController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}),

				// Employee features
				new DashboardFeature("Employee", "Diagnosis", Set.of(UserRole.DOCTOR), () -> {
<<<<<<< HEAD
					Diagnosis view = new Diagnosis();
					new DiagnosisController(view);
=======
					DiagnosisView view = new DiagnosisView();
					DiagnosisController controller = new DiagnosisController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Employee", "Complaint Manager", Set.of(UserRole.RECEPTIONIST), () -> {
<<<<<<< HEAD
					EmployeeComplaint view = new EmployeeComplaint();
					new EmployeeComplaintController(view);
=======
					EmployeeComplaintView view = new EmployeeComplaintView();
					EmployeeComplaintController controller = new EmployeeComplaintController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Employee", "Staff Assignment", Set.of(UserRole.RECEPTIONIST), () -> {
<<<<<<< HEAD
					StaffAssignment view = new StaffAssignment();
					new StaffAssignmentController(view);
=======
					StaffAssignmentView view = new StaffAssignmentView();
					StaffAssignmentController controller = new StaffAssignmentController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Employee", "My Patients", Set.of(UserRole.DOCTOR), () -> {
<<<<<<< HEAD
					Patients view = new Patients();
					new PatientsController(view);
=======
					PatientsView view = new PatientsView();
					PatientsController controller = new PatientsController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Employee", "Doctors", Set.of(UserRole.DOCTOR, UserRole.RECEPTIONIST), () -> {
<<<<<<< HEAD
					Doctors view = new Doctors();
					new DoctorsController(view);
=======
					DoctorsView view = new DoctorsView();
					DoctorsController controller = new DoctorsController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Employee", "Nurse Station", Set.of(UserRole.NURSE), () -> {
<<<<<<< HEAD
					Vitals view = new Vitals();
					new VitalsController(view);
=======
					VitalsView view = new VitalsView();
					VitalsController controller = new VitalsController(view);
					view.registerActionListener(controller);
>>>>>>> stash
					return view;
				}), new DashboardFeature("Employee", "Staff Chat",
						Set.of(UserRole.DOCTOR, UserRole.NURSE, UserRole.RECEPTIONIST), () -> {
<<<<<<< HEAD
							StaffChat view = new StaffChat();
							new StaffChatController(view);
=======
							StaffChatView view = new StaffChatView();
							StaffChatController controller = new StaffChatController(view);
							view.registerActionListener(controller);
>>>>>>> stash
							return view;
						}));

	}

<<<<<<< HEAD
=======
	/*
	 * Start CarePlus Client Application
	 *
	 * Shared by both the patient and the employee client: there is one executable,
	 * and the role attached to whoever logs in decides which dashboard they get.
	 *
	 * The whole bootstrap runs inside invokeLater because Swing components must be
	 * created and touched only on the Event Dispatch Thread, construction included.
	 */
>>>>>>> stash
	public static void main(String[] args) {

		List<DashboardFeature> features = assignFeatures();

		SwingUtilities.invokeLater(() -> {
<<<<<<< HEAD
			Login login = new Login();
			new LoginController(login, features);
			login.setVisible(true);
=======

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
				 * Last resort handler. A failure this early leaves no usable window, so the
				 * only thing left is to record why before the application dies.
				 */
				// TODO
				logger.error("CarePlus client application could not be started", e);
			}
>>>>>>> stash
		});
	}
}
