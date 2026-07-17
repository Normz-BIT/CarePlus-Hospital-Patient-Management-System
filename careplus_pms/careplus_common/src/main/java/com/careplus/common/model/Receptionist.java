package com.careplus.common.model;

import com.careplus.common.enums.UserRole;

/*
 * Child of the Employee and Person Class
 * Receptionist handles the Complaints for the Patients
 */
public class Receptionist extends Employee {
	private static final long serialVersionUID = 1L;

	private String deskNo;

	public Receptionist() {
		super();
		setRole(UserRole.RECEPTIONIST);
	}

	public Receptionist(String personId, String firstName, String lastName, String email, String phone, String password) {
		super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST);
		// TODO Auto-generated constructor stub
	}

	public String getDeskNo() {
		return deskNo;
	}

	public void setDeskNo(String deskNo) {
		this.deskNo = deskNo;
	}


}
