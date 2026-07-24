package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.AppointmentStatus;

import jakarta.persistence.*;

/*
 * Patient Books Appointments
 * Doctor Attends Appointments
 *
 */
@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "appointment_id", nullable = false)
	private int appointmentId;

	@Column(name = "appointment_date", nullable = false)
	private LocalDateTime appointmentDate;

	@Column(name = "reason", length = 200)
	private String reason;
	/*
	 * Starts at SCHEDULED, then ends up either COMPLETED or CANCELLED. Those last
	 * two are meant to be the end of it, nothing moves back out of them.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private AppointmentStatus status;

	@Column(name = "patient_id", nullable = false)
	private String patientId;

	@Column(name = "doctor_id", nullable = false)
	private String doctorId;

	public Appointment() {

	}

	public Appointment(int appointmentId, LocalDateTime appointmentDate, String reason, AppointmentStatus status,
			String patientId, String doctorId) {
		this.appointmentId = appointmentId;
		this.appointmentDate = appointmentDate;
		this.reason = reason;
		this.status = status;
		this.patientId = patientId;
		this.doctorId = doctorId;
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

	public int getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}

	public LocalDateTime getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDateTime appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Appointment [appointmentId=" + appointmentId + ", patientId=" + patientId + ", doctorId=" + doctorId
				+ ", appointmentDate=" + appointmentDate + ", reason=" + reason + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(appointmentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Appointment)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Appointment other = (Appointment) obj;
		return appointmentId == other.appointmentId;
	}

}
