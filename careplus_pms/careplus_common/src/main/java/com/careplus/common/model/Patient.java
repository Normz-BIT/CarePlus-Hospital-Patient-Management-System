package com.careplus.common.model;

import java.util.Date;
import java.util.List;

/*
 * Child of the Person class
 * 	can make Payments
 * 	can make Complaints
 * 	can set Appointments
 *  has Vital Signs
 *  has Medical Records
 *    
 */
public class Patient extends Person {
	private static final long serialVersionUID = 1L;

	private Date dateofBrith;
	private String gender;
	private String address;
	private String medicalHistory;

	private List<Payment> paymentList;
	private List<Complaint> complaintsList;
	private List<Appointment> appointmentList;
	private List<VitalSigns> vitalSignsList;
	private List<MedicalRecord> medMeidcalRecordsList;

	public List<Payment> getPaymentList() {
		return paymentList;
	}

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
