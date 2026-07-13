package com.careplus.common.model;

import java.io.Serializable;
import java.util.List;

import com.careplus.common.enums.UserRole;

import jakarta.persistence.*;

/**
 * Root of the person inheritance hierarchy
 */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "person_id", length = 10)
	protected String personId;
	@Column(name = "first_name", nullable = false, length = 50)
	protected String firstName;
	@Column(name = "last_name", nullable = false, length = 50)
	protected String lastName;
	@Column(name = "email", nullable = false, unique = true, length = 120)
	protected String email;
	@Column(name = "phone", length = 20)
	protected String phone;
	@Column(name = "password", nullable = false, length = 255)
	protected String password;

	
	transient List<ChatMessages> complaint;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	protected UserRole role;


	protected Person() {

	}

	protected Person(String personId, String firstName, String lastName, String email, String phone, String password,
			UserRole role, List<ChatMessages> complaint) {
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = role;
		this.complaint = complaint;
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

	@Override
	public boolean equals(Object obj) {// allows us to compare object instances
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

	@Override
	public int hashCode() {// use string id hash code
		return (personId == null) ? 0 : personId.hashCode();
	}
}