package com.careplus.common.model;

import com.careplus.common.enums.NurseWard;
import com.careplus.common.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "nurse")
@PrimaryKeyJoinColumn(name = "person_id")
public class Nurse extends Employee {

    @Transient
    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "ward", nullable = false)
    private NurseWard ward;

    public Nurse() {
        super();
        setRole(UserRole.NURSE);
    }

    public Nurse(String personId, String firstName, String lastName,
                 String email, String phone, String password) {
        super(personId, firstName, lastName, email, phone, password, UserRole.NURSE);
    }

    public NurseWard getWard() {
        return ward;
    }

    public void setWard(NurseWard ward) {
        this.ward = ward;
    }

    @Override
    public String toString() {
        return "Nurse [personId=" + personId
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", email=" + email
                + ", phone=" + phone
                + ", role=" + role
                + ", department=" + department
                + ", hireDate=" + hireDate
                + ", ward=" + ward + "]";
    }
}