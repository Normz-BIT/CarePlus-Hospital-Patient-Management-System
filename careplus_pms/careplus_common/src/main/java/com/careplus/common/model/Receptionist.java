package com.careplus.common.model;

import com.careplus.common.enums.UserRole;

/*
 * Child of the Employee and Person Class
 * Receptionist handles the Complaints for the Patients
 *
 * Receptionists are the triage point of the complaint workflow: they are the only
 * role that assigns a complaint to a doctor or nurse, which is what moves it from
 * SUBMITTED to ASSIGNED.
 *
 * UNMAPPED: carries no @Entity or @Column annotations, so Hibernate cannot
 * resolve a staff row to a Receptionist. See the fuller explanation on Doctor,
 * which has the same gap.
 *
 * TODO: Add @Entity, @Table("receptionist"), and @PrimaryKeyJoinColumn("receptionist_id")
 * to this class, and mark deskNo with @Column. Staff login is currently broken.
 */
public class Receptionist extends Employee {
	private static final long serialVersionUID = 1L;

	private String deskNo;

	/*
	 * Role is fixed by the subclass so a Receptionist can never carry the wrong
	 * UserRole, which is what the client reads to decide the visible dashboard
	 * features.
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
