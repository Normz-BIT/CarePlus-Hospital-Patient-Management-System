package com.careplus.common.model;

import java.io.Serializable;

/*
 * Payment class
 * Patient has Payments
 */
<<<<<<< HEAD
=======

/*
 * One of only four fully mapped entities, alongside Person, Employee and Patient.
 * It is both persisted by PaymentService and sent over the socket, so it must stay
 * Serializable as well as annotated.
 */
@Entity
@Table(name = "payment")
>>>>>>> stash
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;
<<<<<<< HEAD
=======
	/*
	 * IDENTITY delegates key generation to MySQL's AUTO_INCREMENT, so the value is
	 * unknown until the row is actually inserted. That is why PaymentService uses
	 * persist for new payments rather than merge.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
>>>>>>> stash
	private int paymentId;
<<<<<<< HEAD
=======
	/*
	 * double is a poor fit for currency: binary floating point cannot represent
	 * values like 0.10 exactly, so repeated arithmetic accumulates rounding error.
	 * BigDecimal, or storing cents as an integer, is the usual remedy for money.
	 */
	@Column(name = "amount_paid")
>>>>>>> stash
	private double amountPaid;
<<<<<<< HEAD
=======
	/*
	 * Stored per payment as a snapshot of what was owed at that moment, rather than
	 * derived from the patient's account. It is therefore historical record, not a
	 * live figure, and the newest row is what reflects the current balance.
	 */
	@Column(name = "outstanding_balance")
>>>>>>> stash
	private double outstandingBalance;
	private String description;
<<<<<<< HEAD
=======
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
>>>>>>> stash

	public Payment() {

	}

	public Payment(int paymentId, double amountPaid, double outstandingBalance, String description) {
		this.paymentId = paymentId;
		this.amountPaid = amountPaid;
		this.outstandingBalance = outstandingBalance;
		this.description = description;
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
