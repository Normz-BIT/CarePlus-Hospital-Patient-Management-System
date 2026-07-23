package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "amount_paid", nullable = false, precision = 10, scale = 2)
    private double amountPaid;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "outstanding_balance", nullable = false, precision = 10, scale = 2)
    private double outstandingBalance;

    @Column(name = "description", length = 200)
    private String description;

    public Payment() {
    }

    public Payment(Patient patient, double amountPaid, LocalDateTime paymentDate,
                   double outstandingBalance, String description) {
        this.patient = patient;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.outstandingBalance = outstandingBalance;
        this.description = description;
    }

    @PrePersist
    protected void onCreate() {
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
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
        return "Payment [paymentId=" + paymentId
                + ", patient=" + (patient != null ? patient.getPersonId() : null)
                + ", amountPaid=" + amountPaid
                + ", paymentDate=" + paymentDate
                + ", outstandingBalance=" + outstandingBalance
                + ", description=" + description + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Payment)) return false;
        if (getClass() != obj.getClass()) return false;
        Payment other = (Payment) obj;
        return paymentId != null && paymentId.equals(other.paymentId);
    }

    @Override
    public int hashCode() {
        return paymentId == null ? 0 : paymentId.hashCode();
    }
}