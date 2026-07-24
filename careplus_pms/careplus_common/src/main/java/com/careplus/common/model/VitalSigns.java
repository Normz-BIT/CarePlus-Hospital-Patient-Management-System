package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

/*
 * Patient has Vital Records
 * Nurse records Vital signs
 *
 * Every one of these is a single timestamped reading rather than the patient's
 * "current" values, so they pile up as a series. We did it that way because
 * vitals are read as a trend: what a doctor actually wants is how a temperature
 * or pulse moved across a shift, not just the latest number.
 */

@Entity
@Table(name = "vital_signs")
public class VitalSigns implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vital_id", nullable = false)
	private int vitalId;

	/*
	 * The patient the reading is for and the nurse who took it, as plain person_id
	 * Strings rather than @ManyToOne links, same reasoning as Payment. The database
	 * enforces both through fk_vitals_patient and fk_vitals_nurse.
	 *
	 * These were both broken before: patientId had @ManyToOne on it while typed as
	 * a String, which isn't legal since an association has to point at an entity.
	 * nurseId had a bare @JoinColumn with no association at all, so it went looking
	 * for a "nurseId" column that doesn't exist.
	 */
	@Column(name = "patient_id", nullable = false)
	private String patientId;

	@Column(name = "nurse_id", nullable = false)
	private String nurseId;
	/*
	 * We don't save a unit with this, so Celsius versus Fahrenheit is an unwritten
	 * agreement between whoever types it and whoever reads it. The screen label
	 * says Celsius, which is all that's holding that together.
	 *
	 * Double not double, because the column allows null and a primitive would turn
	 * a missing reading into 0.0. The chk_vitals_temp constraint rejects a stored
	 * zero, so that default would get written as if it were real and then bounced.
	 */
	@Column(name = "temperature")
	private Double temperature;
	/*
	 * A String because blood pressure is two numbers over each other, not one. The
	 * downside is you can't compare or average these without splitting the string
	 * first, and nothing forces the "120/80" format on the way in.
	 */
	@Column(name = "blood_pressure", length = 10)
	private String bloodPressure;

	@Column(name = "heart_rate")
	private Integer heartRate;

	@Column(name = "respiratory_rate")
	private Integer respiratoryRate;
	/*
	 * Two separate boxes on purpose: observations are what the nurse saw about the
	 * patient, nursing notes are the care they gave. Keeping them apart keeps that
	 * difference in the record instead of mushing it into one field.
	 */

	@Column(name = "observations", columnDefinition = "TEXT")
	private String observations;

	@Column(name = "nursing_notes", columnDefinition = "TEXT")
	private String nursingNotes;
	/*
	 * When the reading was taken, which is what puts the trend in order. Comes off
	 * the client's clock, so it's really the workstation's time, not the server's.
	 */
	@Column(name = "recorded_at", nullable = false)
	private LocalDateTime recordedAt = LocalDateTime.now();

	public VitalSigns() {

	}

	public VitalSigns(int vitalId, String patientId, String nurseId, Double temperature, String bloodPressure,
			Integer heartRate, Integer respiratoryRate, String observations, String nursingNotes,
			LocalDateTime recordedAt) {
		this.vitalId = vitalId;
		this.patientId = patientId;
		this.nurseId = nurseId;
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

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getNurseId() {
		return nurseId;
	}

	public void setNurseId(String nurseId) {
		this.nurseId = nurseId;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public String getBloodPressure() {
		return bloodPressure;
	}

	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	/*
	 * These give back Integer rather than int on purpose. Both columns allow null,
	 * so a reading that was never taken comes back as null, and the old
	 * "public int getHeartRate()" would have thrown a NullPointerException on that
	 * row instead of just telling us it wasn't there.
	 */
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
		return "VitalSigns [vitalId=" + vitalId + ", patientId=" + patientId + ", nurseId=" + nurseId + ", temperature="
				+ temperature + ", bloodPressure=" + bloodPressure + ", heartRate=" + heartRate + ", respiratoryRate="
				+ respiratoryRate + ", observations=" + observations + ", nursingNotes=" + nursingNotes
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		VitalSigns other = (VitalSigns) obj;
		return vitalId == other.vitalId;
	}

}
