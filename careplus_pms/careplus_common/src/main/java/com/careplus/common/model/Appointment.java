package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.AppointmentStatus;

import jakarta.persistence.*;

/*
 * Patient Books Appointments
 * Doctor Attends Appointments
 *
 * Appointment is one of the models we defined as a wire type first, so the
 * client screens could be built and demonstrated while the matching service was
 * still being written. It travels between client and server as a serialized
 * object and gains its JPA mapping when AppointmentService is completed.
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
	 * LocalDateTime carries no zone or offset, so every appointment is implicitly
	 * in the hospital's local time. Safe for a single site, but it would become
	 * ambiguous across time zones or over a daylight saving transition.
	 */

	@Column(name = "appointment_date", nullable = false)
	private LocalDateTime appointmentDate;

	@Column(name = "reason", length = 200)
	private String reason;
	/*
	 * Lifecycle is SCHEDULED, then either COMPLETED or CANCELLED. Bookings start at
	 * SCHEDULED, and the terminal states are intended to be final.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private AppointmentStatus status;

	/*
	 * Both foreign keys are held as person_id Strings with @Column rather than as
	 * mapped associations, following the same reasoning as Payment: an Appointment
	 * is serialized across the socket, and a lazy association would risk putting an
	 * uninitialised proxy on the wire. Referential integrity is enforced by the
	 * schema through fk_appt_patient and fk_appt_doctor.
	 *
	 * These carried @JoinColumn previously with no @ManyToOne to go with it. On its
	 * own that annotation declares no mapping, so both fields fell back to
	 * Hibernate's default naming and looked for columns "patientId" and "doctorId",
	 * neither of which exists in the appointment table.
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
