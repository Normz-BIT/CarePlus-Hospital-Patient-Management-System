package com.careplus.common.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import com.careplus.common.enums.Department;
import com.careplus.common.enums.UserRole;

/*
 * Employee Abstract Class
 *
 * The staff side of the Person hierarchy, and itself the parent of another
 * JOINED level, so the full chain is person then employee then doctor/nurse/
 * receptionist. Abstract because nobody is hired as just an "employee": every
 * staff member has to actually be one of the three.
 *
 * The primary key points back at person.person_id instead of staff getting
 * their own separate ID. One person, one ID, all the way down.
 */

@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "person_id")
public abstract class Employee extends Person {
	@Transient
	private static final long serialVersionUID = 1L;

	/*
	 * Saved by name, same reason as role on Person: reordering the Department
	 * constants shouldn't change what's already saved.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "department")
	protected Department department;
	/*
	 * hire_date is a DATE column so the time half never gets saved. MySQL drops it
	 * on write and reads come back at midnight. Only the date part counts.
	 */
	@Column(name = "hire_date")
	protected LocalDateTime hireDate;

	protected Employee() {
		super();
	}

	public Employee(String personId, String firstName, String lastName, String email, String phone, String password,
			UserRole role, LocalDateTime createdAt) {
		super(personId, firstName, lastName, email, phone, password, role, createdAt);

	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public LocalDateTime getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDateTime hireDate) {
		this.hireDate = hireDate;
	}

}
