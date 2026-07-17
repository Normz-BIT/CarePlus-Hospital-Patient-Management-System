package com.careplus.common.model;

import java.util.List;

import com.careplus.common.enums.DoctorSpecialization;
import com.careplus.common.enums.UserRole;

/*
 * Child of the Employee and Person Class
 * 
 * Doctors create medical records and attend Appointments
 */
public class Doctor extends Employee {
	private static final long serialVersionUID = 1L;

	private DoctorSpecialization specialization;
	private String licenseNo;


	public Doctor() {
		super();
		setRole(UserRole.DOCTOR);
	}

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