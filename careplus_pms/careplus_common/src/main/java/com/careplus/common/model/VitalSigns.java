package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Patient has Vital Records
 * Nurse records Vital signs
 */

public class VitalSigns implements Serializable {

	private static final long serialVersionUID = 1L;
	private int vitalId;
	private double temperature;
	private String bloodPressure;
	private int heartRate;
	private int respiratoryRate;
	private String observations;
	private String nursingNotes;
	private LocalDateTime recordedAt;

	public VitalSigns() {

	}

	public VitalSigns(int vitalId, double temperature, String bloodPressure, int heartRate, int respiratoryRate,
			String observations, String nursingNotes, LocalDateTime recordedAt) {
		this.vitalId = vitalId;
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

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public String getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public int getRespiratoryRate() {
		return respiratoryRate;
	}

	public void setRespiratoryRate(int respiratoryRate) {
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
		return "VitalSigns [vitalId=" + vitalId + ", temperature=" + temperature + ", bloodPressure=" + bloodPressure
				+ ", heartRate=" + heartRate + ", respiratoryRate=" + respiratoryRate + ", observations=" + observations
				+ ", nursingNotes=" + nursingNotes + ", recordedAt=" + recordedAt + "]";
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		VitalSigns other = (VitalSigns) obj;
		return vitalId == other.vitalId;
	}

	
	
}
