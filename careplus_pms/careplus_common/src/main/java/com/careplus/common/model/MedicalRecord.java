package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Patient has Medical records
 * Doctor creates Medical records
 *
 * DTO ONLY: no JPA annotations, matching MedicalRecordService still being an
 * unimplemented stub.
 *
 * Records are append only by intent. A correction should add a superseding record
 * rather than overwrite an existing one, since the accumulated history is what
 * makes a patient's prior treatment visible to the next clinician.
 *
 * Carries neither a patient nor an authoring doctor reference, both of which are
 * needed before a record can be attributed or retrieved.
 */
public class MedicalRecord implements Serializable{
	private static final long serialVersionUID = 1L;

	private int recordId;
	private String diagnosis;
	private String treatmentNote;
	/*
	 * Nullable in effect: most records need no follow up, and null is what
	 * distinguishes those from a scheduled return visit.
	 *
	 * Note this is populated by DiagnosisController from a date-only input parsed
	 * with atStartOfDay, so the time component is always midnight and carries no
	 * clinical meaning. Do not read it as an appointment time.
	 */
	private LocalDateTime followUpDate;
	/*
	 * When the record was written, as distinct from followUpDate above. Set from the
	 * client clock at entry time rather than by the database.
	 */
	private LocalDateTime createdDate;
	
	public MedicalRecord() {
		
		
	}
	
	public MedicalRecord(int recordId, String diagnosis, String treatmentNote, LocalDateTime followUpDate,
			LocalDateTime createdDate) {
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
	public LocalDateTime getFollowUpDate() {
		return followUpDate;
	}
	public void setFollowUpDate(LocalDateTime followUpDate) {
		this.followUpDate = followUpDate;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "MedicalRecord [recordId=" + recordId + ", diagnosis=" + diagnosis + ", treatmentNote=" + treatmentNote
				+ ", followUpDate=" + followUpDate + ", createdDate=" + createdDate + "]";
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
