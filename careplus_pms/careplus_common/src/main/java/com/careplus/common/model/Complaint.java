package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.ComplaintCategory;
import com.careplus.common.enums.ComplaintStatus;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id", nullable = false)
    private int complaintId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaintParentId")
    private Complaint complaintParent;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ComplaintCategory category;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_submitted", nullable = false)
    private LocalDateTime dateSubmitted = LocalDateTime.now();

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

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Complaint getComplaintParent() {
        return complaintParent;
    }

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

    public void setDescription(String description) {
        this.description = description;
    }

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