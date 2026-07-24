package com.careplus.common.model;

import java.io.Serializable;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/*
 * Patient has Medical records
 * Doctor creates Medical records
 *
 * These are append only on purpose. If a doctor needs to correct something they
 * add another record rather than editing the old one, because the build-up of
 * history is what shows the next doctor how the patient was treated before.
 * Overwriting would lose the reasoning behind the earlier decision.
 */
 
 
@Entity
@Table(name = "medical_record")
public class MedicalRecord implements Serializable{

    @Transient
	private static final long serialVersionUID = 1L;

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
	private int recordId;
	@Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment_notes", columnDefinition = "TEXT")
    private String treatmentNote;

   
	/*
	 * Left null when there's no follow up needed, which is most of the time. Null
	 * versus a date is how we tell those apart from a real return visit.
	 *
	 * The column is a DATE so the time half never gets saved anyway. The diagnosis
	 * screen pads the typed date to midnight with atStartOfDay, so don't read the
	 * time on this as an appointment time, it doesn't mean anything.
	 */
	 @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;
	/*
	 * When the record was written, which is a different thing from followUpDate
	 * above. Comes off the client's clock when it's entered, not the database.
	 */
	  @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();



	/*
	 * Who the record is for and who wrote it, as plain person_id Strings rather
	 * than @ManyToOne links. Same reasoning as Payment, plus one extra: loading a
	 * whole Patient in here would drag their password across the socket with it.
	 * The database enforces both keys through fk_record_patient and
	 * fk_record_doctor.
	 */
	@Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

	public MedicalRecord() {


	}

	public MedicalRecord(int recordId, String patientId, String doctorId, String diagnosis, String treatmentNote,
			LocalDateTime followUpDate, LocalDateTime createdDate) {
		this.recordId = recordId;
		this.patientId = patientId;
		this.doctorId = doctorId;
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
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "MedicalRecord [recordId=" + recordId + ", patientId=" + patientId + ", doctorId=" + doctorId
				+ ", diagnosis=" + diagnosis + ", treatmentNote=" + treatmentNote + ", followUpDate=" + followUpDate
				+ ", createdDate=" + createdDate + "]";
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
