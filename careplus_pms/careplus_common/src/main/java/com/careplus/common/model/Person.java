package com.careplus.common.model;

import java.io.Serializable;

/**
 * Root of the person inheritance hierarchy
 */

public abstract class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	private String personId;

	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private String password;

	protected Person() {

	}

	protected Person(String personId, String firstName, String lastName, String email, String phone, String password) {
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.password = password;
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
	public boolean equals(Object o) {// allows us to compare object instances
		if (this == o) {
			return true;
		}
		if (!(o instanceof Person)) {
			return false;
		}
		Person other = (Person) o;
		return personId != null && personId.equals(other.personId);
	}

	@Override
	public int hashCode() {// use id hash code
		return personId == null ? 0 : personId.hashCode();
	}
}