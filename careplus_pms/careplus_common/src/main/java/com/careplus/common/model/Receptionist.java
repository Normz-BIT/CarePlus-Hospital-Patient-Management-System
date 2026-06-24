package com.careplus.common.model;

import java.util.List;

import com.careplus.common.enums.UserRole;

/*
 * Child of the Employee and Person Class
 * Receptionist handles the Complaints for the Patients
 */
public class Receptionist extends Employee {
	private static final long serialVersionUID = 1L;

	private String deskNo;
	private List<Complaint> complaintsList;

	public Receptionist() {
		super();
		setRole(UserRole.RECEPTIONIST);
	}

	public Receptionist(String personId, String firstName, String lastName, String email, String phone, String password,
			List<ChatMessages> complaint) {
		super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST, complaint);
		// TODO Auto-generated constructor stub
	}

	public String getDeskNo() {
		return deskNo;
	}

	public void setDeskNo(String deskNo) {
		this.deskNo = deskNo;
	}

	public List<Complaint> getComplaintsList() {
		return complaintsList;
	}

	public void setComplaintsList(List<Complaint> complaintsList) {
		this.complaintsList = complaintsList;
	}

}
