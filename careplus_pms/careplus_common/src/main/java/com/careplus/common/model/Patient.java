package com.careplus.common.model;

import java.util.Date;


import java.util.List;

import com.careplus.common.enums.UserRole;


/* *
 * Child of the Person class
 * 	can make Payments
 * 	can make Complaints
 * 	can set Appointments
 *  has Vital Signs
 *  has Medical Records
 *    
 */
<<<<<<< HEAD
=======

/*
 * Unlike the three staff subclasses, this one is fully mapped, which is why
 * patient login and patient lookups work while staff equivalents do not. It sits
 * one level below Person rather than under Employee, so a patient read costs a
 * single join instead of two.
 */
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "person_id")
>>>>>>> stash
public class Patient extends Person {
	private static final long serialVersionUID = 1L;
<<<<<<< HEAD
=======

	/*
	 * LocalDate rather than LocalDateTime, since a birth date has no meaningful
	 * time component. We store the date and work age out when it is needed, because
	 * a stored age would be wrong from the patient's next birthday onwards.
	 */
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

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
>>>>>>> stash

<<<<<<< HEAD
	private Date dateofBrith;
	private String gender;
	private String address;
	private String medicalHistory;

	private List<Payment> paymentList;
	private List<Complaint> complaintsList;
	private List<Appointment> appointmentList;
	private List<VitalSigns> vitalSignsList;
	private List<MedicalRecord> medMeidcalRecordsList;
=======
    /*
     * TEXT rather than a bounded VARCHAR because clinical history is free form and
     * accumulates over time, so any column limit would eventually truncate a
     * patient's record.
     */
    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;
>>>>>>> stash

	public Patient() {
		super();
		setRole(UserRole.PATIENT);
	}

	public Patient(String personId, String firstName, String lastName, String email, String phone, String password,
			List<ChatMessages> complaint) {
		super(personId, firstName, lastName, email, phone, password, UserRole.PATIENT, complaint);
	}

<<<<<<< HEAD
	public List<Payment> getPaymentList() {
		return paymentList;
=======


	public LocalDate getDateOfBirth() {
		return dateOfBirth;
>>>>>>> stash
	}

<<<<<<< HEAD
	public void setPaymentList(List<Payment> paymentList) {
		this.paymentList = paymentList;
	}

	public List<Complaint> getComplaintsList() {
		return complaintsList;
	}

	public void setComplaintsList(List<Complaint> complaintsList) {
		this.complaintsList = complaintsList;
	}

	public List<Appointment> getAppointmentList() {
		return appointmentList;
	}

	public void setAppointmentList(List<Appointment> appointmentList) {
		this.appointmentList = appointmentList;
	}

	public List<VitalSigns> getVitalSignsList() {
		return vitalSignsList;
	}

	public void setVitalSignsList(List<VitalSigns> vitalSignsList) {
		this.vitalSignsList = vitalSignsList;
	}

	public List<MedicalRecord> getMedMeidcalRecordsList() {
		return medMeidcalRecordsList;
	}

	public void setMedMeidcalRecordsList(List<MedicalRecord> medMeidcalRecordsList) {
		this.medMeidcalRecordsList = medMeidcalRecordsList;
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
=======
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
>>>>>>> stash
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
