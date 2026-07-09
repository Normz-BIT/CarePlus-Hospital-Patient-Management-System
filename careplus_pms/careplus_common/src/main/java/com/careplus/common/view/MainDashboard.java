package com.careplus.common.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class MainDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private JDesktopPane desktopPane;

    private JMenuBar menuBar;

    private JMenu menuSystem;
    private JMenu menuPatient;
    private JMenu menuEmployee;

    private JMenuItem mnuLogout;
    private JMenuItem mnuExit;

    private JMenuItem mnuAppointments;
    private JMenuItem mnuComplaints;
    private JMenuItem mnuMedicalRecords;
    private JMenuItem mnuPayments;
    private JMenuItem mnuChat;

    private JMenuItem mnuComplaintManager;
    private JMenuItem mnuDiagnosis;
    private JMenuItem mnuStaffAssignment;

    public MainDashboard() {

        setTitle("CarePlus Hospital Management System");

        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(225, 238, 250));

        setLayout(new BorderLayout());
        add(desktopPane, BorderLayout.CENTER);

        buildMenu();

        setSize(1300, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void buildMenu() {

        menuBar = new JMenuBar();

        menuSystem = new JMenu("System");
        menuPatient = new JMenu("Patient");
        menuEmployee = new JMenu("Employee");

        mnuLogout = new JMenuItem("Logout");
        mnuExit = new JMenuItem("Exit");

        mnuAppointments = new JMenuItem("Appointments");
        mnuComplaints = new JMenuItem("Complaints");
        mnuMedicalRecords = new JMenuItem("Medical Records");
        mnuPayments = new JMenuItem("Payments");
        mnuChat = new JMenuItem("Chat");

        mnuComplaintManager = new JMenuItem("Complaint Manager");
        mnuDiagnosis = new JMenuItem("Diagnosis");
        mnuStaffAssignment = new JMenuItem("Staff Assignment");

        mnuLogout.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        mnuExit.addActionListener(e -> System.exit(0));

        mnuAppointments.addActionListener(e ->
                openFrame("com.careplus.client.patient.view.Appointment"));

        mnuComplaints.addActionListener(e ->
                openFrame("com.careplus.client.patient.view.Complaint"));

        mnuMedicalRecords.addActionListener(e ->
                openFrame("com.careplus.client.patient.view.MedicalRecord"));

        mnuPayments.addActionListener(e ->
                openFrame("com.careplus.client.patient.view.Payment"));

        mnuChat.addActionListener(e ->
                openFrame("com.careplus.client.patient.view.Chat"));

        mnuComplaintManager.addActionListener(e ->
                openFrame("com.careplus.client.employee.view.EmployeeComplaint"));

        mnuDiagnosis.addActionListener(e ->
                openFrame("com.careplus.client.employee.view.Diagnosis"));

        mnuStaffAssignment.addActionListener(e ->
                openFrame("com.careplus.client.employee.view.StaffAssignment"));

        menuSystem.add(mnuLogout);
        menuSystem.addSeparator();
        menuSystem.add(mnuExit);

        menuPatient.add(mnuAppointments);
        menuPatient.add(mnuComplaints);
        menuPatient.add(mnuMedicalRecords);
        menuPatient.add(mnuPayments);
        menuPatient.add(mnuChat);

        menuEmployee.add(mnuComplaintManager);
        menuEmployee.add(mnuDiagnosis);
        menuEmployee.add(mnuStaffAssignment);

        menuBar.add(menuSystem);
        menuBar.add(menuPatient);
        menuBar.add(menuEmployee);

        setJMenuBar(menuBar);
    }

    private void openFrame(String className) {

        try {

            Class<?> clazz = Class.forName(className);

            for (JInternalFrame frame : desktopPane.getAllFrames()) {

                if (frame.getClass().getName().equals(className)) {

                    frame.setIcon(false);
                    frame.setSelected(true);
                    frame.toFront();
                    return;
                }
            }

            Object object = clazz.getDeclaredConstructor().newInstance();

            if (object instanceof JInternalFrame) {

                JInternalFrame frame = (JInternalFrame) object;

                desktopPane.add(frame);
                frame.setVisible(true);
                frame.setSelected(true);
                frame.toFront();

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainDashboard dashboard = new MainDashboard();
            dashboard.setVisible(true);

        });
    }
}