package com.careplus.common.model;

import java.time.LocalDate;

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

@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "person_id")
public class Patient extends Person {

	@Transient
	private static final long serialVersionUID = 1L;
	
	@Column(name = "date_of_birth")
	private LocalDate dateOfBrith;
	
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;
	
    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

	public Patient() {
		super();
		setRole(UserRole.PATIENT);
	}

	public Patient(String personId, String firstName, String lastName, String email, String phone, String password) {
		super(personId, firstName, lastName, email, phone, password, UserRole.PATIENT);
	}

	public LocalDate getdateOfBrith() {
		return dateOfBrith;
	}

	public void setdateOfBrith(LocalDate dateOfBrith) {
		this.dateOfBrith = dateOfBrith;
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

}
