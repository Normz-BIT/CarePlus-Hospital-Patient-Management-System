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
 * Sits directly under Person rather than under Employee, so reading a patient is
 * one join instead of two. Staff go through Employee because they share the
 * department and hire date columns, patients don't need any of that.
 */
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "person_id")
public class Patient extends Person {

	@Transient
	private static final long serialVersionUID = 1L;

	/*
	 * We save the date of birth and work the age out when we need it. Saving the
	 * age itself would go stale on the patient's next birthday.
	 *
	 * Note the column is a DATE, so the time half of this never gets saved. MySQL
	 * throws it away on write and reads come back at midnight. Only the date part
	 * means anything.
	 */
	@Column(name = "date_of_birth")
	private LocalDateTime dateOfBirth;

    /*
     * Saved by name not number, same reason as role on Person: reordering the
     * Gender constants shouldn't change what's already in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "address", length = 200)
    private String address;

    /*
     * TEXT instead of a VARCHAR with a limit, since medical history is free
     * writing that keeps getting added to. Any limit we picked would eventually
     * chop off part of someone's record.
     */
    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

	public Patient() {
		super();
		setRole(UserRole.PATIENT);
	}

	



	public Patient(String personId, String firstName, String lastName, String email, String phone, String password, LocalDateTime createdAt) {
		super(personId, firstName, lastName, email, phone, password, UserRole.PATIENT, createdAt);
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
