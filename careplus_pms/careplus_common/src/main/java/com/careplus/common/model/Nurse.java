package com.careplus.common.model;

import java.time.LocalDateTime;

import com.careplus.common.enums.NurseWard;
import com.careplus.common.enums.UserRole;

import jakarta.persistence.*;

/*
 * Nurse is the staff type responsible for the bedside record: nurses take
 * patients' vital signs and write the nursing notes that go with them.
 *
 */

@Entity
@Table(name = "nurse")
@PrimaryKeyJoinColumn(name = "person_id")
public class Nurse extends Employee {

	@Transient
	private static final long serialVersionUID = 1L;
	/*
	 * Ward is what scopes a nurse to a set of patients, so this field is what the
	 * "assigned cases" list is built from once the server side query is written. We
	 * made it an enum rather than free text so two nurses on the same ward always
	 * match exactly, which a typed ward name could not guarantee.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "ward", nullable = false)
	private NurseWard ward;

	/*
	 * As with Doctor, the role is fixed here rather than passed in so a Nurse
	 * cannot be built with the wrong UserRole, which is what drives the dashboard
	 * features the client shows.
	 */
	public Nurse() {
		super();
		setRole(UserRole.NURSE);
	}

	
	public Nurse(String personId, String firstName, String lastName, String email, String phone, String password, LocalDateTime createdAt) {
		super(personId, firstName, lastName, email, phone, password, UserRole.NURSE, createdAt);
		// TODO Auto-generated constructor stub
	}


	public NurseWard getWard() {
		return ward;
	}

	public void setWard(NurseWard ward) {
		this.ward = ward;
	}

	@Override
	public String toString() {
		return "Nurse [ward=" + ward + ", department=" + department + ", hireDate=" + hireDate + ", personId="
				+ personId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phone="
				+ phone + ", password=" + password + ", role=" + role + "]";
	}

}
