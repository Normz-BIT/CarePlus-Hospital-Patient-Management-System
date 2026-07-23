package com.careplus.common.model;

<<<<<<< HEAD
import java.util.Date;
import java.util.List;
=======
import java.time.LocalDate;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

import com.careplus.common.enums.UserRole;

<<<<<<< HEAD
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
=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
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
<<<<<<< HEAD

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
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	
	protected Employee() {
		super();
	}
	
	protected Employee(String personId, String firstName, String lastName, String email, String phone, String password,
			UserRole role, List<ChatMessages> complaint) {
		super(personId, firstName, lastName, email, phone, password, role, complaint);
		
	}
=======
    @Transient
    private static final long serialVersionUID = 1L;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	public String getDepartment() {
		return department;
	}
=======
    @Column(name = "department", length = 80)
    protected String department;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	public void setDepartment(String department) {
		this.department = department;
	}
=======
    @Column(name = "hire_date")
    protected LocalDate hireDate;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
<<<<<<< HEAD
	public Date getHireDate() {
=======
	public LocalDate getHireDate() {
>>>>>>> stash
		return hireDate;
	}
=======
    protected Employee() {
        super();
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
<<<<<<< HEAD
	public void setHireDate(Date hireDate) {
=======
	public void setHireDate(LocalDate hireDate) {
>>>>>>> stash
		this.hireDate = hireDate;
	}
=======
    protected Employee(String personId, String firstName, String lastName,
                       String email, String phone, String password, UserRole role) {
        super(personId, firstName, lastName, email, phone, password, role);
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD


}
=======
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

    @Override
    public String toString() {
        return "Employee [personId=" + personId
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", email=" + email
                + ", phone=" + phone
                + ", role=" + role
                + ", department=" + department
                + ", hireDate=" + hireDate + "]";
    }
}
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
