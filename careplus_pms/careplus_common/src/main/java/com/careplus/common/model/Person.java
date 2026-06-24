package com.careplus.common.model;

import java.io.Serializable;
import java.util.List;

import com.careplus.common.enums.UserRole;


/**
 * Root of the person inheritance hierarchy
 */

public abstract class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String personId;

	protected String firstName;

	protected String lastName;

	protected String email;

	protected String phone;

	protected String password;

	protected UserRole role;

	protected List<ChatMessages> complaint;

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

	public List<ChatMessages> getComplaint() {
		return complaint;
	}

	public void setComplaint(List<ChatMessages> complaint) {
		this.complaint = complaint;
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