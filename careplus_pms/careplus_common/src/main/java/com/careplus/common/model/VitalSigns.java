package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

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
	 * The patient observed and the nurse who took the reading, held as person_id
	 * Strings with @Column rather than as mapped associations, for the reason set
	 * out on Payment: a VitalSigns row is serialized across the socket, and a lazy
	 * association would risk putting an uninitialised proxy on the wire. The schema
	 * enforces both keys through fk_vitals_patient and fk_vitals_nurse.
	 *
	 * patientId previously carried @ManyToOne while typed as a String, which is not
	 * a legal mapping: an association target has to be an entity. nurseId carried a
	 * bare @JoinColumn with no association at all, so it fell back to Hibernate's
	 * default naming and looked for a "nurseId" column that does not exist.
	 */
	@Column(name = "patient_id", nullable = false)
	private String patientId;

	@Column(name = "nurse_id", nullable = false)
	private String nurseId;
	/*
	 * No unit is recorded alongside the value, so Celsius against Fahrenheit is an
	 * unwritten convention between whoever enters the reading and whoever reads it.
	 * VitalsController only validates that the input parses as a number, so nothing
	 * currently rejects a physiologically impossible temperature.
	 *
	 * Boxed, because the column is nullable and a primitive would report an absent
	 * reading as 0.0. The chk_vitals_temp constraint rejects a stored zero, so that
	 * default would have been written as a real value and then refused.
	 */
	@Column(name = "temperature")
	private Double temperature;
	/*
	 * A String because blood pressure is a systolic over diastolic pair rather than
	 * one number. The tradeoff is that it cannot be compared or averaged without
	 * parsing, and no format is enforced on entry.
	 */
	@Column(name = "blood_pressure", length = 10)
	private String bloodPressure;

	@Column(name = "heart_rate")
	private Integer heartRate;

	@Column(name = "respiratory_rate")
	private Integer respiratoryRate;
	/*
	 * Two separate free text fields by design: observations are what the nurse saw
	 * about the patient, nursing notes are the care given. Keeping them apart
	 * preserves that distinction in the clinical record.
	 */

	@Column(name = "observations", columnDefinition = "TEXT")
	private String observations;

	@Column(name = "nursing_notes", columnDefinition = "TEXT")
	private String nursingNotes;
	/*
	 * When the reading was taken, which is what orders the trend. Set from the
	 * client clock at entry time, so it reflects the workstation's time rather than
	 * the server's.
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
	 * These four accessors return the boxed type rather than unboxing to a
	 * primitive. Both columns are nullable, so a reading that was never taken comes
	 * back as null, and the previous "public int getHeartRate()" would have thrown
	 * a NullPointerException on that row rather than reporting the absence.
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
