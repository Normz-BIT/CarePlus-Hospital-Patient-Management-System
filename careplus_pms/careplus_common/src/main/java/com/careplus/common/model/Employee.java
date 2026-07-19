package com.careplus.common.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import com.careplus.common.enums.UserRole;

/*
 * Employee Abstract Class
 *
 * The staff branch of the Person hierarchy and itself the root of a second JOINED
 * level, so the full chain is person then employee then the concrete role table.
 * Abstract because no one is employed as a generic "employee": every staff row
 * must resolve to a Doctor, Nurse or Receptionist.
 *
 * IMPORTANT: that resolution does not currently work. Doctor, Nurse and
 * Receptionist carry no @Entity annotation, so Hibernate does not know they
 * exist and cannot instantiate this abstract class to satisfy a staff row.
 * Annotating those three subclasses is what completes the mapping declared here.
 * */
@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.JOINED)
/*
 * Ties the employee table's primary key back to person.person_id rather than
 * giving staff a separate identifier. One person, one ID, across all three
 * levels of the hierarchy.
 */
@PrimaryKeyJoinColumn(name = "person_id")
public abstract class Employee extends Person {
	@Transient 
	private static final long serialVersionUID = 1L;
	
	@Column(name = "department", length = 80)
	protected String department;
	@Column(name = "hireDate")
	protected LocalDate hireDate;

	
	protected Employee() {
		super();
	}
	
	protected Employee(String personId, String firstName, String lastName, String email, String phone, String password,
			UserRole role) {
		super(personId, firstName, lastName, email, phone, password, role);
		
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}



}
