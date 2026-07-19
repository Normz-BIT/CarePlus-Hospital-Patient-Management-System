package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

/*
 * Payment class
 * Patient has Payments
 */

/*
 * One of only four fully mapped entities, alongside Person, Employee and Patient.
 * It is both persisted by PaymentService and sent over the socket, so it must stay
 * Serializable as well as annotated.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;
	/*
	 * IDENTITY delegates key generation to MySQL's AUTO_INCREMENT, so the value is
	 * unknown until the row is actually inserted. That is why PaymentService uses
	 * persist for new payments rather than merge.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private int paymentId;
	/*
	 * double is a poor fit for currency: binary floating point cannot represent
	 * values like 0.10 exactly, so repeated arithmetic accumulates rounding error.
	 * BigDecimal, or storing cents as an integer, is the usual remedy for money.
	 */
	@Column(name = "amount_paid")
	private double amountPaid;
	/*
	 * Stored per payment as a snapshot of what was owed at that moment, rather than
	 * derived from the patient's account. It is therefore historical record, not a
	 * live figure, and the newest row is what reflects the current balance.
	 */
	@Column(name = "outstanding_balance")
	private double outstandingBalance;
	@Column(name = "description")
	private String description;
	@Column(name = "payment_date")
	private LocalDateTime paymentDate;
	/*
	 * A plain String copy of Person.personId rather than a mapped @ManyToOne
	 * relationship. That keeps the object graph flat, which matters because these
	 * objects are serialized over the socket and an association would risk dragging
	 * a lazy proxy across the wire. The cost is that no foreign key constraint is
	 * expressed here, so referential integrity depends on the schema alone. The HQL
	 * in PaymentService filters on this field.
	 */
	@Column(name = "patient_id")
	private String patientId;

	public Payment() {

	}

	public Payment(int paymentId, double amountPaid, double outstandingBalance, String description,
			LocalDateTime paymentDate, String patientId) {
		this.paymentId = paymentId;
		this.amountPaid = amountPaid;
		this.outstandingBalance = outstandingBalance;
		this.description = description;
		this.paymentDate = paymentDate;
		this.patientId = patientId;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public double getOutstandingBalance() {
		return outstandingBalance;
	}

	public void setOutstandingBalance(double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", amountPaid=" + amountPaid + ", outstandingBalance="
				+ outstandingBalance + ", description=" + description + ", paymentDate=" + paymentDate + ", patientId="
				+ patientId + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Payment)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Payment other = (Payment) obj;
		return paymentId == other.paymentId;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(paymentId);
	}

}
