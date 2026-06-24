package com.careplus.common.model;

import java.util.Date;
import java.util.List;

import com.careplus.common.enums.UserRole;

/*
 * Employee Abstract Class
 * */
public abstract class Employee extends Person {

	private static final long serialVersionUID = 1L;
	protected String Department;

	protected Date hireDate;

	
	protected Employee() {
		super();
	}
	
	protected Employee(String personId, String firstName, String lastName, String email, String phone, String password,
			UserRole role, List<ChatMessages> complaint) {
		super(personId, firstName, lastName, email, phone, password, role, complaint);
		
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

}
