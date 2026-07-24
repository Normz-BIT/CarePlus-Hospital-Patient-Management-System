package com.careplus.common.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.careplus.common.enums.DoctorSpecialization;
import com.careplus.common.enums.UserRole;

import jakarta.persistence.*;

/*
 * One of the three staff types, alongside Nurse and Receptionist. Doctors are
 * the ones making the clinical calls: they write diagnoses and medical records
 * and take appointments.
 *
 * specialization is an enum rather than free text so the booking and directory
 * screens can group doctors properly. licenseNo stays a String since it's just
 * an identifier we show on screen.
 */

@Entity
@Table(name = "doctor")
@PrimaryKeyJoinColumn(name = "person_id")
public class Doctor extends Employee {

	@Transient
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	@Column(name = "specialization", nullable = false)
	private DoctorSpecialization specialization;

	@Column(name = "license_no", length = 40, unique = true)
	private String licenseNo;

	/*
	 * We set the role in here instead of taking it as a parameter, so nobody can
	 * accidentally build a Doctor with the wrong UserRole. 
	 */
	public Doctor() {
		super();
		setRole(UserRole.DOCTOR);
	}

	/*
	 * This one passes the role up to super instead of calling setRole, so both
	 * constructors end up in the same place by different routes. We keep both
	 * because Hibernate needs the empty one
	 */
	public Doctor(String personId, String firstName, String lastName, String email, String phone, String password, LocalDateTime createdAt) {
		super(personId, firstName, lastName, email, phone, password, UserRole.DOCTOR, createdAt);
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
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Doctor)) {
			return false;
		}
		Doctor other = (Doctor) obj;
		return Objects.equals(licenseNo, other.licenseNo) && specialization == other.specialization;
	}

	@Override
	public String toString() {
		return "Doctor [personId=" + personId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", phone=" + phone + ", role=" + role + ", department=" + department + ", hireDate="
				+ hireDate + ", specialization=" + specialization + ", licenseNo=" + licenseNo + "]";
	}

}