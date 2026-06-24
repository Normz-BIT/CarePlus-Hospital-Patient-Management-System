package com.careplus.common.model;

import java.io.Serializable;

/*
 * Payment class
 * Patient has Payments
 */
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;
	private int paymentId;
	private double amountPaid;
	private double outstandingBalance;
	private String description;

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
