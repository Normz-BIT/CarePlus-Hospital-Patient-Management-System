package com.careplus.common.model;

import java.util.List;

import com.careplus.common.enums.DoctorSpecialization;
import com.careplus.common.enums.UserRole;

/*
 * Doctor is one of the three staff types in our Person hierarchy, alongside
 * Nurse and Receptionist. Doctors are the clinical decision makers in the
 * system: they record diagnoses, write medical records and attend appointments.
 *
 * We chose an enum for specialization rather than free text so the scheduling
 * and directory screens can group doctors reliably, and a String for licenseNo
 * because a licence number is an identifier we display and never calculate with.
 *
 * TODO: add the JPA mapping for the staff subclasses (@Entity, @Table and the
 * @PrimaryKeyJoinColumn, plus @Column on the two fields below) so Hibernate can
 * load a staff row into a Doctor. Person, Employee and Patient are already
 * mapped; the staff side is the next piece of persistence work.
 */
public class Doctor extends Employee {
	private static final long serialVersionUID = 1L;

	private DoctorSpecialization specialization;
	private String licenseNo;
	private List<Appointment> appointmentsList;
	private List<MedicalRecord> medicalRecordsList;

	/*
	 * We fix the role inside the constructor rather than accepting it as a
	 * parameter, so a Doctor can never be built carrying the wrong UserRole. This
	 * matters because the client reads role to decide which dashboard features to
	 * open, and a mismatch there would show a doctor the wrong workspace.
	 */
	public Doctor() {
		super();
		setRole(UserRole.DOCTOR);
	}

<<<<<<< HEAD
	public Doctor(String personId, String firstName, String lastName, String email, String phone, String password,
			List<ChatMessages> complaint) {
		super(personId, firstName, lastName, email, phone, password, UserRole.DOCTOR, complaint);
=======
	/*
	 * The full constructor passes the role up to super instead of calling setRole,
	 * so the two constructors reach the same state by different routes. Both are
	 * kept because Hibernate needs the no-arg version while application code is
	 * clearer building a doctor in one step.
	 */
	public Doctor(String personId, String firstName, String lastName, String email, String phone, String password) {
		super(personId, firstName, lastName, email, phone, password, UserRole.DOCTOR);
>>>>>>> stash

	}

	public DoctorSpecialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(DoctorSpecialization specialization) {
		this.specialization = specialization;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public List<Appointment> getAppointmentsList() {
		return appointmentsList;
	}

	public void setAppointmentsList(List<Appointment> appointmentsList) {
		this.appointmentsList = appointmentsList;
	}

	public List<MedicalRecord> getMedicalRecordsList() {
		return medicalRecordsList;
	}

	public void setMedicalRecordsList(List<MedicalRecord> medicalRecordsList) {
		this.medicalRecordsList = medicalRecordsList;
	}

}