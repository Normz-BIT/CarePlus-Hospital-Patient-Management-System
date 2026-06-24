package com.careplus.common.model;

import java.util.List;

/*
 * Child of the Employee and Person Class
 * Receptionist handles the Complaints for the Patients
 */
public class Receptionist extends Employee {
	private static final long serialVersionUID = 1L;

	private String deskNo;
	private List<Complaint> complaintsList;

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
