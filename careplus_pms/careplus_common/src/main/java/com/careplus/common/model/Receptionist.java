package com.careplus.common.model;

import com.careplus.common.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "receptionist")
@PrimaryKeyJoinColumn(name = "person_id")
public class Receptionist extends Employee {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column(name = "desk_no", length = 20)
    private String deskNo;

    public Receptionist() {
        super();
        setRole(UserRole.RECEPTIONIST);
    }

    public Receptionist(String personId, String firstName, String lastName,
                        String email, String phone, String password) {
        super(personId, firstName, lastName, email, phone, password, UserRole.RECEPTIONIST);
    }

    public String getDeskNo() {
        return deskNo;
    }

    public void setDeskNo(String deskNo) {
        this.deskNo = deskNo;
    }

    @Override
    public String toString() {
        return "Receptionist [personId=" + personId
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", email=" + email
                + ", phone=" + phone
                + ", role=" + role
                + ", department=" + department
                + ", hireDate=" + hireDate
                + ", deskNo=" + deskNo + "]";
    }
}