package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.UserRole;

import jakarta.persistence.*;

/**
 * Root of the person inheritance hierarchy
 *
 * JOINED inheritance gives each subclass its own table holding only its extra
 * columns, linked back to person by shared primary key. That keeps the schema
 * normalised to 3NF, at the cost of a join per subclass read. The alternative,
 * SINGLE_TABLE, would be faster but would force every doctor-only and
 * patient-only column to be nullable in one wide table.
 *
 * The practical payoff is in AuthService: one lookup against Person resolves a
 * patient or any staff member without the caller knowing which, and the
 * concrete subclass comes back with its role attached.
 *
 * This is also a Serializable object sent over the socket, so it is both a JPA
 * entity and a wire type. That dual role is why serialVersionUID below is
 * marked @Transient.
 */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Serializable {
	/*
	 * @Transient keeps Hibernate from trying to map this serialization bookkeeping
	 * field to a column. Without it, Hibernate would look for a matching column and
	 * fail at startup.
	 */
	@Transient
	private static final long serialVersionUID = 1L;
	/*
	 * A human readable business key, such as a staff or patient ID, rather than a
	 * generated surrogate. It is assigned by the hospital and typed in at login,
	 * which is why AuthService normalises it to uppercase before lookup: the value
	 * stored here is the canonical uppercase form.
	 */
	@Id
	@Column(name = "person_id")
	protected String personId;
	@Column(name = "first_name", nullable = false)
	protected String firstName;
	@Column(name = "last_name", nullable = false)
	protected String lastName;
	/*
	 * Email is unique because it is how a person is identified when registering, so
	 * the constraint lives in the database where it holds regardless of which part
	 * of the application does the insert.
	 *
	 * TODO: check for an existing email in the service before persisting, so a
	 * duplicate registration produces a readable message rather than a constraint
	 * violation on commit.
	 */
	@Column(name = "email", nullable = false, unique = true)
	protected String email;
	@Column(name = "phone", length = 20)
	protected String phone;
	/*
	 * Stored as plaintext, which is what lets AuthService compare with equals. This
	 * is the field that would need to hold a salted hash before the system could
	 * handle real patient data.
	 */
	@Column(name = "password", length = 255, nullable = false)
	protected String password;

	/*
	 * We use EnumType.STRING rather than the ORDINAL default so the database holds
	 * "DOCTOR" instead of a positional number. With ORDINAL, reordering the
	 * UserRole constants would change what every existing row means, and the stored
	 * data would no longer say what it did when it was written. Storing the name
	 * also makes the table readable when marking or debugging.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	protected UserRole role;

	@Column(name = "created_at", nullable = false, updatable = false)
	protected LocalDateTime createdAt;

	/*
	 * Required by both Hibernate and Java serialization to instantiate before
	 * populating fields. Protected rather than public so application code is pushed
	 * towards the argument taking constructors, which cannot leave an entity half
	 * built.
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
	 * The column is mapped updatable = false, so a value set here only reaches the
	 * database on the initial insert. Changing it on an already persisted Person
	 * has no effect once the row exists.
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Person [personId=" + personId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", phone=" + phone + ", password=" + password + ", role=" + role + ", createdAt=" + createdAt
				+ "]";
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