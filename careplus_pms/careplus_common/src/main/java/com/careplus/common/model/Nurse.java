package com.careplus.common.model;

import java.time.LocalDateTime;

import com.careplus.common.enums.NurseWard;
import com.careplus.common.enums.UserRole;

import jakarta.persistence.*;

/*
 * The staff type that handles the bedside record: nurses take vital signs and
 * write the nursing notes that go with them.
 */

@Entity
@Table(name = "nurse")
@PrimaryKeyJoinColumn(name = "person_id")
public class Nurse extends Employee {

	@Transient
	private static final long serialVersionUID = 1L;
	/*
	 * Which ward the nurse is posted to. An enum rather than free text so two
	 * nurses on the same ward always match exactly, which typed-in ward names
	 * would never guarantee.
	 *
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "ward", nullable = false)
	private NurseWard ward;

	/*
	 * Same as Doctor: role is set here rather than passed in, so a Nurse can't be
	 * built with the wrong one. The client reads it to pick the dashboard menus.
	 */
	public Nurse() {
		super();
		setRole(UserRole.NURSE);
	}

	
	public Nurse(String personId, String firstName, String lastName, String email, String phone, String password, LocalDateTime createdAt) {
		super(personId, firstName, lastName, email, phone, password, UserRole.NURSE, createdAt);
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
