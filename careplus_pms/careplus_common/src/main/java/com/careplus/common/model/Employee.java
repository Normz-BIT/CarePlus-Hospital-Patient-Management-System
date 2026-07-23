package com.careplus.common.model;

import java.time.LocalDate;

import com.careplus.common.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "person_id")
public abstract class Employee extends Person {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column(name = "department", length = 80)
    protected String department;

    @Column(name = "hire_date")
    protected LocalDate hireDate;

    protected Employee() {
        super();
    }

    protected Employee(String personId, String firstName, String lastName,
                       String email, String phone, String password, UserRole role) {
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