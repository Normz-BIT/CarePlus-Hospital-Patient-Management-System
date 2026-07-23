package com.careplus.common.model;

import java.io.Serializable;
<<<<<<< HEAD
=======
import java.time.LocalDate;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
import java.time.LocalDateTime;

<<<<<<< HEAD
/*
 * Patient has Medical records
 * Doctor creates Medical records
 *
 * Medical records are append only by design. A correction is made by adding a
 * further record rather than editing an existing one, because the accumulated
 * history is what shows the next clinician how a patient was treated before.
 * Overwriting would lose the reasoning behind an earlier decision.
 *
 * A wire type for now, gaining its JPA mapping when MedicalRecordService is
 * completed.
 *
 * TODO: add the JPA annotations, plus the patient and authoring doctor
 * references a record needs to be attributed and retrieved.
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
=======
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
	@Override
	public String toString() {
		return "MedicalRecord [recordId=" + recordId + ", diagnosis=" + diagnosis + ", treatmentNote=" + treatmentNote
				+ ", followUpDate=" + followUpDate + ", createdDate=" + createdDate + "]";
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(recordId);
	}
=======
@Entity
@Table(name = "medical_record")
public class MedicalRecord implements Serializable {
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
    private int recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment_notes", columnDefinition = "TEXT")
    private String treatmentNote;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    public MedicalRecord() {
    }

    public MedicalRecord(int recordId, Patient patient, Doctor doctor,
                         String diagnosis, String treatmentNote,
                         LocalDate followUpDate, LocalDateTime createdDate) {
        this.recordId = recordId;
        this.patient = patient;
        this.doctor = doctor;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
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
        return "MedicalRecord [recordId=" + recordId
                + ", patient=" + (patient != null ? patient.getPersonId() : null)
                + ", doctor=" + (doctor != null ? doctor.getPersonId() : null)
                + ", diagnosis=" + diagnosis
                + ", treatmentNote=" + treatmentNote
                + ", followUpDate=" + followUpDate
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
        MedicalRecord other = (MedicalRecord) obj;
        return recordId == other.recordId;
    }
}