package com.careplus.common.model;

import com.careplus.common.enums.NurseWard;
import com.careplus.common.enums.UserRole;

/*
 * Child of the Employee and Person Class
 * Nurses record the vital Signs for the patients
 */
public class Nurse extends Employee {

	private static final long serialVersionUID = 1L;
	private NurseWard ward;

	public Nurse() {
		super();
		setRole(UserRole.NURSE);
	}

	public Nurse(String personId, String firstName, String lastName, String email, String phone, String password) {
		super(personId, firstName, lastName, email, phone, password, UserRole.NURSE);
	}

	public NurseWard getWard() {
		return ward;
	}

	public void setWard(NurseWard ward) {
		this.ward = ward;
	}


}
