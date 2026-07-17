package com.careplus.common.model;

import java.util.Date;

import com.careplus.common.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Patient extends Person {

	@Transient
	private static final long serialVersionUID = 1L;
	@Column(name = "date_of_birth")
	private Date dateofBrith;
	@Column(name = "gender")
	private String gender;
	@Column(name = "adress")
	private String address;
	@Column(name = "medical_history")
	private String medicalHistory;

	public Patient() {
		super();
		setRole(UserRole.PATIENT);
	}

	public Patient(String personId, String firstName, String lastName, String email, String phone, String password) {
		super(personId, firstName, lastName, email, phone, password, UserRole.PATIENT);
	}

	public Date getDateofBrith() {
		return dateofBrith;
	}

	public void setDateofBrith(Date dateofBrith) {
		this.dateofBrith = dateofBrith;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
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

}
