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
	 * row pointing at the one it answers, rather than a separate reply type. Zero
	 * means this is an original complaint rather than a response, since the field
	 * is a primitive int and so cannot be null.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "complaintParentId")
	private int complaintParentId;

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

	public Complaint(int complaintId, String description, LocalDateTime dateSubmitted, String response,
			LocalDateTime responseDate, ComplaintStatus status, ComplaintCategory category) {

		this.complaintId = complaintId;
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

	public int getComplaintParentId() {
		return complaintParentId;
	}

	public void setComplaintParentId(int complaintParentId) {
		this.complaintParentId = complaintParentId;
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
		return "Complaint [complaintId=" + complaintId + ", complaintParentId=" + complaintParentId + ", description="
				+ description + ", dateSubmitted=" + dateSubmitted + ", response=" + response + ", responseDate="
				+ responseDate + ", status=" + status + ", category=" + category + "]";
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
