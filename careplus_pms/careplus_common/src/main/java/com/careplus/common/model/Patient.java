package com.careplus.common.model;

import java.time.LocalDateTime;

import com.careplus.common.enums.Gender;
import com.careplus.common.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/* *
 * Child of the Person class
 * 	can make Payments
 * 	can make Complaints
 * 	can set Appointments
 *  has Vital Signs
 *  has Medical Records
 *    
 */

/*
 * Unlike the three staff subclasses, this one is fully mapped, which is why
 * patient login and patient lookups work while staff equivalents do not. It sits
 * one level below Person rather than under Employee, so a patient read costs a
 * single join instead of two.
 */
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "person_id")
public class Patient extends Person {

	@Transient
	private static final long serialVersionUID = 1L;

	/*
	 * We store the date of birth and work age out when it is needed, because a
	 * stored age would be wrong from the patient's next birthday onwards.
	 *
	 * patient.date_of_birth is a DATE column, so the time half of this value is
	 * not stored: MySQL discards it on write and a read comes back at midnight.
	 * Only the date part is meaningful.
	 */
	@Column(name = "date_of_birth")
	private LocalDateTime dateOfBirth;

    /*
     * Stored by name rather than ordinal, for the same reason as role on Person:
     * reordering the Gender constants must not change what rows already in the
     * database mean.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "address", length = 200)
    private String address;

    /*
     * TEXT rather than a bounded VARCHAR because clinical history is free form and
     * accumulates over time, so any column limit would eventually truncate a
     * patient's record.
     */
    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

	public Patient() {
		super();
		setRole(UserRole.PATIENT);
	}

	



	public Patient(String personId, String firstName, String lastName, String email, String phone, String password, LocalDateTime createdAt) {
		super(personId, firstName, lastName, email, phone, password, UserRole.PATIENT, createdAt);
		// TODO Auto-generated constructor stub
	}





	public LocalDateTime getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDateTime dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	@Override
	public String toString() {
		return "Patient [dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", address=" + address
				+ ", medicalHistory=" + medicalHistory + ", personId=" + personId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", phone=" + phone + ", password=" + password
				+ ", role=" + role + "]";
	}

	
	
}
