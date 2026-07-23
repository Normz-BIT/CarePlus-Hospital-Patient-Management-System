package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.ComplaintCategory;
import com.careplus.common.enums.ComplaintStatus;
<<<<<<< HEAD
/*
 * Patient files Complaints
 * Receptionist handles Complaints
 *
 * Complaint carries the whole triage workflow: a patient files one, a
 * receptionist assigns it, and a doctor or nurse responds. The status field is
 * what tracks its position in that sequence.
 *
 * Like Appointment, MedicalRecord, VitalSigns and ChatMessages, this is a wire
 * type for now and gains its JPA mapping when ComplaintService is completed.
 *
 * TODO: add the JPA annotations so complaints can be persisted.
 */
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "complaint")
public class Complaint implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

<<<<<<< HEAD
	/*
	 * Self reference that makes a complaint thread: a reply is another Complaint row
	 * pointing at the one it answers, rather than a separate reply type. Zero means
	 * this is an original complaint rather than a response, since the field is a
	 * primitive int and so cannot be null.
	 */
	private int complaintParentId;
=======
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id", nullable = false)
    private int complaintId;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	private String description;
	private LocalDateTime dateSubmitted;
	/*
	 * Duplicates what the parentId threading above already expresses, giving two
	 * competing ways to record a reply. Whichever is chosen, the pair should be
	 * consistent, since the patient's "view responses" screen reads the response
	 * and its date together.
	 */
	private String response;
	private LocalDateTime responseDate;
	/*
	 * Lifecycle is SUBMITTED, ASSIGNED, IN_PROGRESS, RESOLVED, with REOPENED
	 * available afterwards. Nothing in the codebase enforces legal transitions
	 * today, so that rule has to live in ComplaintService once it is written.
	 */
	private ComplaintStatus status;
	private ComplaintCategory category;
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaintParentId")
    private Complaint complaintParent;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ComplaintCategory category;

<<<<<<< HEAD
	public Complaint(int complaintId, String description, LocalDateTime dateSubmitted, String response,
			LocalDateTime responseDate, ComplaintStatus status, ComplaintCategory category) {
=======
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
		this.complaintId = complaintId;
		this.description = description;
		this.dateSubmitted = dateSubmitted;
		this.response = response;
		this.responseDate = responseDate;
		this.status = status;
		this.category = category;
	}
=======
    @Column(name = "date_submitted", nullable = false)
    private LocalDateTime dateSubmitted = LocalDateTime.now();
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ComplaintStatus status = ComplaintStatus.SUBMITTED;

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responded_by")
    private Employee respondedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private Employee assignedTo;

    public Complaint() {
    }

<<<<<<< HEAD
	public LocalDateTime getDateSubmitted() {
		return dateSubmitted;
	}
=======
    public Complaint(int complaintId, Patient patient, Complaint complaintParent,
                     ComplaintCategory category, String description,
                     LocalDateTime dateSubmitted, ComplaintStatus status,
                     String response, LocalDateTime responseDate,
                     Employee respondedBy, Employee assignedTo) {
        this.complaintId = complaintId;
        this.patient = patient;
        this.complaintParent = complaintParent;
        this.category = category;
        this.description = description;
        this.dateSubmitted = dateSubmitted;
        this.status = status;
        this.response = response;
        this.responseDate = responseDate;
        this.respondedBy = respondedBy;
        this.assignedTo = assignedTo;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	public void setDateSubmitted(LocalDateTime dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}
=======
    public int getComplaintId() {
        return complaintId;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	public String getResponse() {
		return response;
	}
=======
    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	public void setResponse(String response) {
		this.response = response;
	}
=======
    public Patient getPatient() {
        return patient;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	public LocalDateTime getResponseDate() {
		return responseDate;
	}
=======
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	public void setResponseDate(LocalDateTime responseDate) {
		this.responseDate = responseDate;
	}
=======
    public Complaint getComplaintParent() {
        return complaintParent;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

    public void setComplaintParent(Complaint complaintParent) {
        this.complaintParent = complaintParent;
    }

    public ComplaintCategory getCategory() {
        return category;
    }

    public void setCategory(ComplaintCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

<<<<<<< HEAD
	@Override
	public String toString() {
		return "Complaint [complaintId=" + complaintId + ", complaintParentId=" + complaintParentId + ", description="
				+ description + ", dateSubmitted=" + dateSubmitted + ", response=" + response + ", responseDate="
				+ responseDate + ", status=" + status + ", category=" + category + "]";
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(complaintId);
	}
=======
    public void setDescription(String description) {
        this.description = description;
    }
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

    public LocalDateTime getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(LocalDateTime dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public Employee getRespondedBy() {
        return respondedBy;
    }

    public void setRespondedBy(Employee respondedBy) {
        this.respondedBy = respondedBy;
    }

    public Employee getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public String toString() {
        return "Complaint [complaintId=" + complaintId
                + ", patient=" + (patient != null ? patient.getPersonId() : null)
                + ", complaintParent=" + (complaintParent != null ? complaintParent.getComplaintId() : null)
                + ", category=" + category
                + ", description=" + description
                + ", dateSubmitted=" + dateSubmitted
                + ", status=" + status
                + ", response=" + response
                + ", responseDate=" + responseDate
                + ", respondedBy=" + (respondedBy != null ? respondedBy.getPersonId() : null)
                + ", assignedTo=" + (assignedTo != null ? assignedTo.getPersonId() : null) + "]";
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(complaintId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Complaint)) {
            return false;
        }
        Complaint other = (Complaint) obj;
        return complaintId == other.complaintId;
    }
}