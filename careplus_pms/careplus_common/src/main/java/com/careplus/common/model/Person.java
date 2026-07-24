package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.UserRole;

import jakarta.persistence.*;

/**
 * The top of our person hierarchy. Everyone in the system is a Person: patients
 * and all three staff types.
 *
 * We went with JOINED so each subclass gets its own table with just its extra
 * columns, joined back here on the same ID. That keeps us at 3NF like the brief
 * asks. SINGLE_TABLE would've been quicker to read but then every
 * doctor-only and patient-only column has to allow nulls in one huge table,
 * which felt worse.
 *
 * Where this really pays off is login: AuthService does one lookup on Person and
 * gets back a Patient, Doctor, Nurse or Receptionist without having to know
 * which it's looking for, and the role comes with it.
 *
 * This also gets sent over the socket, so it's both a database entity and a
 * thing we serialize. That's why serialVersionUID below needs @Transient.
 */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Serializable {
	/*
	 * @Transient so Hibernate doesn't try to find a column for this. It's just
	 * serialization bookkeeping, and without the annotation Hibernate goes looking
	 * for a matching column and falls over at startup.
	 */
	@Transient
	private static final long serialVersionUID = 1L;
	/*
	 * The actual hospital ID (PAT0001, STF0001) rather than a generated number,
	 * since that's what people type at login. We store it uppercase and AuthService
	 * uppercases whatever's typed before looking it up, so case doesn't matter to
	 * whoever's signing in.
	 */
	@Id
	@Column(name = "person_id")
	protected String personId;
	@Column(name = "first_name", nullable = false)
	protected String firstName;
	@Column(name = "last_name", nullable = false)
	protected String lastName;
	/*
	 * Email has to be unique since that's how someone is identified when they
	 * register. We put the rule in the database rather than in Java so it holds no
	 * matter which part of the app does the insert.
	 */
	@Column(name = "email", nullable = false, unique = true)
	protected String email;
	@Column(name = "phone", length = 20)
	protected String phone;
	/*
	 * Plain text for now, which is why AuthService can just use equals to check it.
	 * Obviously this would need hashing before anything real went in the database,
	 * but the seed logins have to stay readable for the demo.
	 */
	@Column(name = "password", length = 255, nullable = false)
	protected String password;

	/*
	 * EnumType.STRING, not the default ORDINAL, so the database holds "DOCTOR"
	 * instead of a number. With ORDINAL, if anyone reordered the UserRole constants
	 * every row already saved would suddenly mean something else. Storing the name
	 * also makes the table readable when we're debugging or showing it to someone.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	protected UserRole role;

	@Column(name = "created_at", nullable = false, updatable = false)
	protected LocalDateTime createdAt;

	/*
	 * Hibernate and Java serialization both need a no-arg constructor to build the
	 * object before filling in the fields. Protected rather than public so the rest
	 * of our code uses the full constructors instead, which can't leave a half
	 * built object lying around.
	 */
	protected Person() {

	}

	public Person(String personId, String firstName, String lastName, String email, String phone, String password,
			UserRole role, LocalDateTime createdAt) {
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = role;
		this.createdAt = createdAt;
	}

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}

	public UserRole getRole() {
		return this.role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getFullName() {
		return firstName + " " + lastName;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/*
	 * Heads up: the column is updatable = false, so this only actually saves on the
	 * first insert. Calling it on someone already in the database does nothing.
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Person [personId=" + personId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", phone=" + phone + ", role=" + role + ", createdAt=" + createdAt
				+ "]";
	}

	/*
	 * Two people are the same person if they have the same ID. Everything else
	 * about them can change, the ID can't.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Person)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Person other = (Person) obj;
		return personId != null && personId.equals(other.personId);
	}

	// Matches equals above: hash off the ID and nothing else.
	@Override
	public int hashCode() {
		return (personId == null) ? 0 : personId.hashCode();
	}
}