package com.careplus.common.model;

import java.io.Serializable;

<<<<<<< HEAD
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
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
@Entity
@Table(name = "payment")
>>>>>>> stash
public class Payment implements Serializable {
<<<<<<< HEAD
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
=======

    @Transient
    private static final long serialVersionUID = 1L;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

<<<<<<< HEAD
	public Payment(int paymentId, double amountPaid, double outstandingBalance, String description) {
		this.paymentId = paymentId;
		this.amountPaid = amountPaid;
		this.outstandingBalance = outstandingBalance;
		this.description = description;
	}
=======
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
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

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

<<<<<<< HEAD
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
=======
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

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