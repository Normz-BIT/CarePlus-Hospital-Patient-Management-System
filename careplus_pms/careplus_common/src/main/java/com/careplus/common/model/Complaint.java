package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.ComplaintCategory;
import com.careplus.common.enums.ComplaintStatus;

import jakarta.persistence.*;

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
@Entity
@Table(name = "complaint")
public class Complaint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "complaint_id", nullable = false)
	private int complaintId;

	/*
	 * Self reference that makes a complaint thread: a reply is another Complaint
	 * row pointing at the one it answers, rather than a separate reply type.
	 *
	 * Boxed rather than a primitive int because the column is a nullable foreign
	 * key back to complaint.complaint_id. A primitive would default to zero, and
	 * zero is not a complaint, so every original complaint would be rejected by
	 * fk_complaint_parent on insert. Null is what marks an original complaint.
	 */
	@Column(name = "complaintParentId")
	private Integer complaintParentId;

	/*
	 * The patient who filed the complaint, held as the person_id String rather
	 * than a mapped association, for the same reason set out on Payment: these
	 * objects are serialized across the socket, and an association would risk
	 * dragging a lazy proxy onto the wire. The foreign key is enforced by the
	 * schema through fk_complaint_patient.
	 */
	@Column(name = "patient_id", nullable = false)
	private String patientId;

	/*
	 * Both are employee person_id values and both are nullable: a complaint that
	 * nobody has picked up yet has neither. respondedBy is who wrote the response
	 * text, assignedTo is who owns the case, and they are not always the same
	 * person, since a receptionist can reply while assigning it to a doctor.
	 */
	@Column(name = "responded_by")
	private String respondedBy;

	@Column(name = "assigned_to")
	private String assignedTo;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(name = "date_submitted", nullable = false)
	private LocalDateTime dateSubmitted = LocalDateTime.now();
	/*
	 * Duplicates what the parentId threading above already expresses, giving two
	 * competing ways to record a reply. Whichever is chosen, the pair should be
	 * consistent, since the patient's "view responses" screen reads the response
	 * and its date together.
	 */
	@Column(name = "response", columnDefinition = "TEXT")
	private String response;

	@Column(name = "response_date")
	private LocalDateTime responseDate;

	/*
	 * Lifecycle is SUBMITTED, ASSIGNED, IN_PROGRESS, RESOLVED, with REOPENED
	 * available afterwards. Nothing in the codebase enforces legal transitions
	 * today, so that rule has to live in ComplaintService once it is written.
	 */

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ComplaintStatus status = ComplaintStatus.SUBMITTED;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private ComplaintCategory category;

	public Complaint() {

	}

	public Complaint(int complaintId, String patientId, String description, LocalDateTime dateSubmitted, String response,
			LocalDateTime responseDate, ComplaintStatus status, ComplaintCategory category) {

		this.complaintId = complaintId;
		this.patientId = patientId;
		this.description = description;
		this.dateSubmitted = dateSubmitted;
		this.response = response;
		this.responseDate = responseDate;
		this.status = status;
		this.category = category;
	}

	public int getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(int complaintId) {
		this.complaintId = complaintId;
	}

	/*
	 * Returns null for an original complaint, rather than zero as the previous
	 * primitive did. Callers testing whether this is a reply must check for null.
	 */
	public Integer getComplaintParentId() {
		return complaintParentId;
	}

	public void setComplaintParentId(Integer complaintParentId) {
		this.complaintParentId = complaintParentId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getRespondedBy() {
		return respondedBy;
	}

	public void setRespondedBy(String respondedBy) {
		this.respondedBy = respondedBy;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
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

	public ComplaintStatus getStatus() {
		return status;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}

	public ComplaintCategory getCategory() {
		return category;
	}

	public void setCategory(ComplaintCategory category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Complaint [complaintId=" + complaintId + ", complaintParentId=" + complaintParentId + ", patientId="
				+ patientId + ", description=" + description + ", dateSubmitted=" + dateSubmitted + ", response="
				+ response + ", responseDate=" + responseDate + ", status=" + status + ", category=" + category
				+ ", respondedBy=" + respondedBy + ", assignedTo=" + assignedTo + "]";
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		Complaint other = (Complaint) obj;
		return complaintId == other.complaintId;
	}

}
