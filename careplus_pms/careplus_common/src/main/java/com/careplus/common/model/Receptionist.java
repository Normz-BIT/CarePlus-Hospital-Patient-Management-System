package com.careplus.common.model;

import com.careplus.common.enums.UserRole;

/*
 * Receptionist is the front desk staff type and the triage point of our
 * complaint workflow. Receptionists are the only role that assigns a complaint
 * to a doctor or nurse, which is the step that moves it from SUBMITTED to
 * ASSIGNED. Keeping that permission to one role is what stops two members of
 * staff picking up the same complaint.
 *
 * TODO: add the JPA mapping for the staff subclasses, as described on Doctor.
 */
public class Receptionist extends Employee {
	private static final long serialVersionUID = 1L;

	/*
	 * Desk number is a String rather than an int because desks are labelled
	 * rather than numbered in sequence, so values like "A2" need to be valid.
	 */
	private String deskNo;

	/*
	 * As with the other staff types, the role is fixed here so a Receptionist
	 * cannot be built carrying the wrong UserRole.
	 */
	public Receptionist() {
		super();
		setRole(UserRole.RECEPTIONIST);
	}

	public Receptionist(String personId, String firstName, String lastName, String email, String phone, String password) {
		super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST);
	}

	public String getDeskNo() {
		return deskNo;
	}

	public void setDeskNo(String deskNo) {
		this.deskNo = deskNo;
	}


}
