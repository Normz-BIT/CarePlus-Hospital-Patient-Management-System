package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

/*
 * Payment class
 * Patient has Payments
 */

@Entity
@Table(name = "payment")
public class Payment implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private int paymentId;
	@Column(name = "amount_paid")
	private double amountPaid;
	@Column(name = "outstanding_balance")
	private double outstandingBalance;
	@Column(name = "description")
	private String description;
	@Column(name = "payment_date")
	private LocalDateTime paymentDate;
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
