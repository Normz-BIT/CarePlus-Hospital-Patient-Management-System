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
 * This one carries the whole triage workflow: a patient files it, a receptionist
 * assigns it, then a doctor or nurse answers it. The status field is how we
 * track where it's got to.
 *
 * Gets sent over the socket as well as saved, so it's Serializable and an entity
 * at the same time.
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
	 * Points back at another complaint, which is how we thread replies: a follow up
	 * is just another Complaint row pointing at the one it answers, instead of us
	 * building a separate reply class.
	 *
	 * Integer not int, because the column can be null and a primitive would default
	 * to 0. Zero isn't a complaint, so the foreign key would reject every single
	 * original complaint on insert. Null is what marks "this is the first one".
	 */
	@Column(name = "complaintParentId")
	private Integer complaintParentId;

	/*
	 * Who filed it, kept as the plain person_id String rather than a @ManyToOne to
	 * Patient. Same reasoning as Payment: this gets serialized over the socket and
	 * a lazy association could put a half-loaded proxy on the wire. The database
	 * still enforces the key through fk_complaint_patient.
	 */
	@Column(name = "patient_id", nullable = false)
	private String patientId;

	/*
	 * Both are staff IDs and both can be null, since a complaint nobody has picked
	 * up yet has neither. They're separate on purpose: respondedBy is whoever wrote
	 * the reply, assignedTo is whoever owns the case, and they're often different
	 * people. A receptionist can answer a patient while assigning it to a doctor.
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
	 * Worth knowing we've ended up with two ways of recording a reply: this pair of
	 * fields, and the parent ID threading above. Keep them in step if you touch
	 * either, because the patient's "view responses" screen reads the response and
	 * its date together.
	 */
	@Column(name = "response", columnDefinition = "TEXT")
	private String response;

	@Column(name = "response_date")
	private LocalDateTime responseDate;

	/*
	 * Goes SUBMITTED, ASSIGNED, IN_PROGRESS, RESOLVED, and REOPENED is available
	 * after that. Nothing stops an illegal jump like SUBMITTED straight to
	 * RESOLVED yet, since the receptionist picks the status off a combo. If we add
	 * that rule it belongs in ComplaintService, because only the server can see
	 * what state the complaint is actually in.
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
	 * Careful: this gives back null for an original complaint, not 0 like the old
	 * primitive version did. Check for null if you're testing whether it's a reply.
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
