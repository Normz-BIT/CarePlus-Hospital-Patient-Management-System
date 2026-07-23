package com.careplus.common.model;

import java.time.LocalDateTime;

import com.careplus.common.enums.UserRole;
import jakarta.persistence.*;
/*
 * Receptionist is the front desk staff type and the triage point of our
 * complaint workflow. Receptionists are the only role that assigns a complaint
 * to a doctor or nurse, which is the step that moves it from SUBMITTED to
 * ASSIGNED. Keeping that permission to one role is what stops two members of
 * staff picking up the same complaint.
 */

@Entity
@Table(name = "receptionist")
@PrimaryKeyJoinColumn(name = "person_id")
public class Receptionist extends Employee {
	@Transient
	private static final long serialVersionUID = 1L;

	/*
	 * Desk number is a String rather than an int because desks are labelled rather
	 * than numbered in sequence, so values like "A2" need to be valid.
	 */

	@Column(name = "desk_no", length = 20)
	private String deskNo;

	/*
	 * As with the other staff types, the role is fixed here so a Receptionist
	 * cannot be built carrying the wrong UserRole.
	 */
	public Receptionist() {
		super();
		setRole(UserRole.RECEPTIONIST);
	}

	
	
	public Receptionist(String personId, String firstName, String lastName, String email, String phone, String password, LocalDateTime createdAt) {
		super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST, createdAt);
		// TODO Auto-generated constructor stub
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
