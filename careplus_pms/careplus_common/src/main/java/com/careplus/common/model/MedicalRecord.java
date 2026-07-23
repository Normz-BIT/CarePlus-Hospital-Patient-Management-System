package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

@Entity
@Table(name = "medical_record")
public class MedicalRecord implements Serializable {

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