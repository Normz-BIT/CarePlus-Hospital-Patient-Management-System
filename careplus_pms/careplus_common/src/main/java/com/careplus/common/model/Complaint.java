package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.ComplaintCategory;
import com.careplus.common.enums.ComplaintStatus;
/*
 * Patient files Complaints
 * Receptionist handles Complaints
 */

public class Complaint implements Serializable {
	private static final long serialVersionUID = 1L;

	private int complaintId;

	private int complaintParentId;

	private String description;
	private LocalDateTime dateSubmitted;
	private String response;
	private LocalDateTime responseDate;
	private ComplaintStatus status;
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
