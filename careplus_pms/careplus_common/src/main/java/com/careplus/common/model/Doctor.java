package com.careplus.common.model;

import com.careplus.common.enums.DoctorSpecialization;
import com.careplus.common.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "doctor")
@PrimaryKeyJoinColumn(name = "person_id")
public class Doctor extends Employee {

    @Transient
    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private DoctorSpecialization specialization;

    @Column(name = "license_no", length = 40, unique = true)
    private String licenseNo;

    public Doctor() {
        super();
        setRole(UserRole.DOCTOR);
    }

    public Doctor(String personId, String firstName, String lastName,
                  String email, String phone, String password) {
        super(personId, firstName, lastName, email, phone, password, UserRole.DOCTOR);
    }

    public DoctorSpecialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(DoctorSpecialization specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    @Override
    public String toString() {
        return "Doctor [personId=" + personId
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", email=" + email
                + ", phone=" + phone
                + ", role=" + role
                + ", department=" + department
                + ", hireDate=" + hireDate
                + ", specialization=" + specialization
                + ", licenseNo=" + licenseNo + "]";
    }
}