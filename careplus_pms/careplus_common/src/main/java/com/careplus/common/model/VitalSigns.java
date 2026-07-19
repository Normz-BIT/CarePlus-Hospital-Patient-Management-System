package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Patient has Vital Records
 * Nurse records Vital signs
 *
 * Each instance is a timestamped observation rather than a set of current
 * values, so readings build up as a series. We modelled it this way because
 * vital signs are read as a trend: what matters clinically is how a temperature
 * or pulse moved across a shift, not only its latest figure.
 *
 * A wire type for now, gaining its JPA mapping alongside MedicalRecordService.
 *
 * TODO: add the JPA annotations and a patient reference, so a reading can be
 * stored against the patient it belongs to.
 */

public class VitalSigns implements Serializable {

	private static final long serialVersionUID = 1L;
	private int vitalId;
	/*
	 * No unit is recorded alongside the value, so Celsius against Fahrenheit is an
	 * unwritten convention between whoever enters the reading and whoever reads it.
	 * VitalsController only validates that the input parses as a number, so nothing
	 * currently rejects a physiologically impossible temperature.
	 */
	private double temperature;
	/*
	 * A String because blood pressure is a systolic over diastolic pair rather than
	 * one number. The tradeoff is that it cannot be compared or averaged without
	 * parsing, and no format is enforced on entry.
	 */
	private String bloodPressure;
	private int heartRate;
	private int respiratoryRate;
	/*
	 * Two separate free text fields by design: observations are what the nurse saw
	 * about the patient, nursing notes are the care given. Keeping them apart
	 * preserves that distinction in the clinical record.
	 */
	private String observations;
	private String nursingNotes;
	/*
	 * When the reading was taken, which is what orders the trend. Set from the
	 * client clock at entry time, so it reflects the workstation's time rather than
	 * the server's.
	 */
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
