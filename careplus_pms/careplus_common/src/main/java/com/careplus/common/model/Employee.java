package com.careplus.common.model;

import java.util.Date;
/*
 * Employee Abstract Class
 * */
public  abstract class Employee extends Person {

	private static final long serialVersionUID = 1L;
	protected String Department;
	
	protected Date hireDate;
	
	
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
