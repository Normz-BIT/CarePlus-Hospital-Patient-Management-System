package com.careplus.common.model;

import com.careplus.common.enums.NurseWard;
import com.careplus.common.enums.UserRole;

/*
 * Child of the Employee and Person Class
 * Nurses record the vital Signs for the patients
 *
 * UNMAPPED: carries no @Entity or @Column annotations, so Hibernate cannot
 * resolve a staff row to a Nurse. See the fuller explanation on Doctor, which has
 * the same gap.
 *
 * TODO: Add @Entity, @Table("nurse"), and @PrimaryKeyJoinColumn("nurse_id") to this class,
 * and mark ward with @Column. Staff login is currently broken.
 */
public class Nurse extends Employee {

	private static final long serialVersionUID = 1L;
	/*
	 * Ward assignment is what scopes a nurse's case list, so this is the field that
	 * would drive "assigned cases" once the server side query exists.
	 */
	private NurseWard ward;

	/*
	 * Role is fixed by the subclass so a Nurse can never carry the wrong UserRole,
	 * which is what the client reads to decide the visible dashboard features.
	 */
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
