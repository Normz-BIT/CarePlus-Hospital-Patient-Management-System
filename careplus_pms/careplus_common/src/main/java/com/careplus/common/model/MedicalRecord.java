package com.careplus.common.model;

import java.io.Serializable;
import java.util.Date;


public class MedicalRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int recordId;
	private String diagnosis;
	private String treatmentNote;
	private Date followUpDate;
	private Date createdDate;
	
	public MedicalRecord() {
		
		
	}
	
	public MedicalRecord(int recordId, String diagnosis, String treatmentNote, Date followUpDate, Date createdDate) {
		this.recordId = recordId;
		this.diagnosis = diagnosis;
		this.treatmentNote = treatmentNote;
		this.followUpDate = followUpDate;
		this.createdDate = createdDate;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getTreatmentNote() {
		return treatmentNote;
	}
	public void setTreatmentNote(String treatmentNote) {
		this.treatmentNote = treatmentNote;
	}
	public Date getFollowUpDate() {
		return followUpDate;
	}
	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(recordId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MedicalRecord)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MedicalRecord other = (MedicalRecord) obj;
		return recordId == other.recordId;
	}
	
}
