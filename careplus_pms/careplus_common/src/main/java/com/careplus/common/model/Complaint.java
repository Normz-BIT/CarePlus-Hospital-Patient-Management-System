package com.careplus.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.careplus.common.enums.ComplaintCategory;
import com.careplus.common.enums.ComplaintStatus;

public class Complaint implements Serializable {
	private static final long serialVersionUID = 1L;

	private int complaintId;
	private String description;
	private Date dateSubmitteDate;
	private String Response;
	private Date responseDate;
	private ComplaintStatus status;
	private ComplaintCategory category;

	
	public Complaint() {
	
	}


	public Complaint(int complaintId, String description, Date dateSubmitteDate, String response, Date responseDate,
			ComplaintStatus status, ComplaintCategory category) {

		this.complaintId = complaintId;
		this.description = description;
		this.dateSubmitteDate = dateSubmitteDate;
		Response = response;
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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getDateSubmitteDate() {
		return dateSubmitteDate;
	}


	public void setDateSubmitteDate(Date dateSubmitteDate) {
		this.dateSubmitteDate = dateSubmitteDate;
	}


	public String getResponse() {
		return Response;
	}


	public void setResponse(String response) {
		Response = response;
	}


	public Date getResponseDate() {
		return responseDate;
	}


	public void setResponseDate(Date responseDate) {
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
	public int hashCode() {
		return Objects.hash(complaintId);
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
