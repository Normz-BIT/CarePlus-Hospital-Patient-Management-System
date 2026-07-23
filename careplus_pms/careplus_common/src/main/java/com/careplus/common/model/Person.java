package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.UserRole;

import jakarta.persistence.*;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "person_id", length = 10, nullable = false)
    protected String personId;

    @Column(name = "first_name", length = 50, nullable = false)
    protected String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    protected String lastName;

    @Column(name = "email", length = 120, nullable = false, unique = true)
    protected String email;

    @Column(name = "phone", length = 20)
    protected String phone;

    @Column(name = "password", length = 255, nullable = false)
    protected String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    protected UserRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    protected Person() {
    }

    protected Person(String personId, String firstName, String lastName,
                     String email, String phone, String password, UserRole role) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Person [personId=" + personId
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", email=" + email
                + ", phone=" + phone
                + ", role=" + role
                + ", createdAt=" + createdAt + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Person)) return false;
        if (getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        return personId != null && personId.equals(other.personId);
    }

    @Override
    public int hashCode() {
        return personId == null ? 0 : personId.hashCode();
    }
}