package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.AppointmentStatus;

import jakarta.persistence.*;

/*
 * Patient Books Appointments
 * Doctor Attends Appointments
 *
 * We wrote this one as a plain serializable class first so the booking screens
 * could be built and shown off while the server side was still being written,
 * then added the mapping once AppointmentService was done. It still travels
 * between client and server as a serialized object.
 */
@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "appointment_id", nullable = false)
	private int appointmentId;
	/*
	 * LocalDateTime has no time zone on it, so every appointment is just assumed to
	 * be hospital local time. Fine for one hospital, but it would get confusing
	 * across time zones or over a daylight saving change.
	 */

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

	/*
	 * Both of these are plain person_id Strings rather than @ManyToOne links, same
	 * reasoning as Payment: an Appointment gets serialized over the socket, and a
	 * lazy association could put a half-loaded proxy on the wire. The database
	 * still enforces both keys through fk_appt_patient and fk_appt_doctor.
	 *
	 * These used to have @JoinColumn on them with no @ManyToOne, which does nothing
	 * on its own. Both fields fell back to Hibernate's default naming and went
	 * looking for "patientId" and "doctorId" columns that don't exist.
	 */
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
