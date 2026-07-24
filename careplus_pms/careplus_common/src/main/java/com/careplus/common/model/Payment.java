package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

/*
 * Payment class
 * Patient has Payments
 */

/*
 * This was the first thing we got working end to end, so the rest of the
 * services copied its shape. It's saved by PaymentService and sent over the
 * socket, so it has to stay Serializable as well as mapped.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;
	/*
	 * IDENTITY lets MySQL's AUTO_INCREMENT make the ID, so we don't know the value
	 * until the row actually goes in. That's why PaymentService uses persist for
	 * new payments instead of merge.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "payment_id", nullable = false)
	private int paymentId;
	/*
	 * Fair warning, double is the wrong type for money. It can't hold values like
	 * 0.10 exactly, so the rounding errors add up the more arithmetic you do. The
	 * proper fix is BigDecimal, or storing everything as whole cents in an int.
	 */
	@Column(name = "amount_paid", nullable = false)
	private double amountPaid;
	/*
	 * Saved on each payment as a snapshot of what was owed at that moment, rather
	 * than worked out from the account. So it's history, not a live number, and the
	 * newest row is the one showing the current balance.
	 */
	@Column(name = "outstanding_balance", nullable = false)
	private double outstandingBalance;
	/*
	 * Allowed to be null, matching the schema. A payment can be recorded without
	 * anyone writing a note against it.
	 */
	@Column(name = "description", length = 200)
	private String description;
	@Column(name = "payment_date", nullable = false)
	private LocalDateTime paymentDate;
	/*
	 * Just a copy of the person_id String instead of a @ManyToOne to Patient. This
	 * is the decision the other models copied: keeping the object flat matters
	 * because these go over the socket, and an association risks dragging a lazy
	 * proxy across with it. The tradeoff is the foreign key isn't expressed in Java
	 * at all, so we're relying on the database for that. PaymentService filters on
	 * this field in its HQL.
	 */
	@Column(name = "patient_id", nullable = false)
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
