package com.careplus.common.model;

import com.careplus.common.enums.UserRole;
import java.util.Date;
/*
 * Employee Abstract Class
 * */
public  abstract class Employee extends Person {

	private static final long serialVersionUID = 1L;
	protected String Department;
	protected UserRole Role; 
	protected Date hireDate;
	
	
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department;
	}
	public UserRole getRole() {
		return Role;
	}
	public void setRole(UserRole role) {
		Role = role;
	}
	public Date getHireDate() {
		return hireDate;
	}
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
	
	
}
