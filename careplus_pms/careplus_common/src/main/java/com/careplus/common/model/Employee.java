package com.careplus.common.model;

import java.util.Date;
import java.util.List;

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
<<<<<<< HEAD
=======
@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.JOINED)
/*
 * Ties the employee table's primary key back to person.person_id rather than
 * giving staff a separate identifier. One person, one ID, across all three
 * levels of the hierarchy.
 */
@PrimaryKeyJoinColumn(name = "person_id")
>>>>>>> stash
public abstract class Employee extends Person {

	private static final long serialVersionUID = 1L;
<<<<<<< HEAD
	protected String Department;

	protected Date hireDate;
=======
	
	@Column(name = "department", length = 80)
	protected String department;
	@Column(name = "hireDate")
	protected LocalDate hireDate;
>>>>>>> stash

	
	protected Employee() {
		super();
	}
	
	protected Employee(String personId, String firstName, String lastName, String email, String phone, String password,
			UserRole role, List<ChatMessages> complaint) {
		super(personId, firstName, lastName, email, phone, password, role, complaint);
		
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

<<<<<<< HEAD
	public Date getHireDate() {
=======
	public LocalDate getHireDate() {
>>>>>>> stash
		return hireDate;
	}

<<<<<<< HEAD
	public void setHireDate(Date hireDate) {
=======
	public void setHireDate(LocalDate hireDate) {
>>>>>>> stash
		this.hireDate = hireDate;
	}



}
