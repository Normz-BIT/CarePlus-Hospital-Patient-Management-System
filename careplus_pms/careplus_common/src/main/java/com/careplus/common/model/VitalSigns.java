package com.careplus.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "vital_signs")
public class VitalSigns implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vital_id", nullable = false)
    private int vitalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", nullable = false)
    private Nurse nurse;

    @Column(name = "temperature", precision = 4, scale = 1)
    private BigDecimal temperature;

    @Column(name = "blood_pressure", length = 10)
    private String bloodPressure;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "nursing_notes", columnDefinition = "TEXT")
    private String nursingNotes;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    public VitalSigns() {
    }

    public VitalSigns(int vitalId, Patient patient, Nurse nurse, BigDecimal temperature,
                      String bloodPressure, Integer heartRate, Integer respiratoryRate,
                      String observations, String nursingNotes, LocalDateTime recordedAt) {
        this.vitalId = vitalId;
        this.patient = patient;
        this.nurse = nurse;
        this.temperature = temperature;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.respiratoryRate = respiratoryRate;
        this.observations = observations;
        this.nursingNotes = nursingNotes;
        this.recordedAt = recordedAt;
    }

    public int getVitalId() {
        return vitalId;
    }

    public void setVitalId(int vitalId) {
        this.vitalId = vitalId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getNursingNotes() {
        return nursingNotes;
    }

    public void setNursingNotes(String nursingNotes) {
        this.nursingNotes = nursingNotes;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public String toString() {
        return "VitalSigns [vitalId=" + vitalId
                + ", patient=" + (patient != null ? patient.getPersonId() : null)
                + ", nurse=" + (nurse != null ? nurse.getPersonId() : null)
                + ", temperature=" + temperature
                + ", bloodPressure=" + bloodPressure
                + ", heartRate=" + heartRate
                + ", respiratoryRate=" + respiratoryRate
                + ", observations=" + observations
                + ", nursingNotes=" + nursingNotes
                + ", recordedAt=" + recordedAt + "]";
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(vitalId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VitalSigns)) {
            return false;
        }
        VitalSigns other = (VitalSigns) obj;
        return vitalId == other.vitalId;
    }
}