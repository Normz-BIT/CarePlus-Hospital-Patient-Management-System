package com.careplus.common.model;

import com.careplus.common.enums.DoctorSpecialization;
import com.careplus.common.enums.UserRole;

/*
 * Child of the Employee and Person Class
 *
 * Doctors create medical records and attend Appointments
 *
 * UNMAPPED: unlike Person, Employee and Patient, this class carries no @Entity,
 * @Table or @PrimaryKeyJoinColumn annotation, and the two fields below have no
 * @Column mapping. Hibernate therefore has no knowledge of Doctor at all, which
 * has a concrete consequence: because Employee is abstract, a staff row in the
 * database has no concrete type Hibernate can instantiate, so the Person lookup
 * in AuthService cannot return a doctor. Adding the annotations here, and on
 * Nurse and Receptionist, is what makes staff login work.
 */
public class Doctor extends Employee {
	private static final long serialVersionUID = 1L;

	private DoctorSpecialization specialization;
	private String licenseNo;


	/*
	 * Role is fixed by the subclass rather than passed in, so a Doctor can never be
	 * constructed carrying the wrong UserRole. That matters because role is what the
	 * client reads to decide which dashboard features to show.
	 */
	public Doctor() {
		super();
		setRole(UserRole.DOCTOR);
	}

	/*
	 * Note the asymmetry with the no-arg constructor above: this one relies on
	 * passing UserRole.DOCTOR up to super rather than calling setRole, so the two
	 * paths set the role by different means but reach the same state.
	 */
	public Doctor(String personId, String firstName, String lastName, String email, String phone, String password) {
		super(personId, firstName, lastName, email, phone, password, UserRole.DOCTOR);

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


}