package com.careplus.common.model;

import java.time.LocalDateTime;

import com.careplus.common.enums.UserRole;
import jakarta.persistence.*;
/*
 * Front desk staff, and the triage point of the whole complaint workflow.
 * Receptionists are the only ones who assign a complaint to a doctor or nurse,
 * which is the step that moves it from SUBMITTED to ASSIGNED. Keeping that to
 * one role is what stops two people picking up the same complaint.
 */

@Entity
@Table(name = "receptionist")
@PrimaryKeyJoinColumn(name = "person_id")
public class Receptionist extends Employee {
	@Transient
	private static final long serialVersionUID = 1L;

	/*
	 * A String, not an int, because desks are labelled rather than numbered in
	 * order, so things like "A2" and "D-01" have to work.
	 */
	@Column(name = "desk_no", length = 20)
	private String deskNo;

	/*
	 * Same as the other two staff types: role is set here so a Receptionist can't
	 * end up carrying the wrong one.
	 */
	public Receptionist() {
		super();
		setRole(UserRole.RECEPTIONIST);
	}

	
	
	public Receptionist(String personId, String firstName, String lastName, String email, String phone, String password, LocalDateTime createdAt) {
		super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST, createdAt);
	}


	@Override
	public String toString() {
		return "Receptionist [deskNo=" + deskNo + ", department=" + department + ", hireDate=" + hireDate
				+ ", personId=" + personId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + ", password=" + password + ", role=" + role + ", createdAt=" + createdAt + "]";
	}

	public String getDeskNo() {
		return deskNo;
	}

	public void setDeskNo(String deskNo) {
		this.deskNo = deskNo;
	}

}
