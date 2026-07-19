package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.AppointmentStatus;


/*
 * Patient Books Appointments
 * Doctor Attends Appointments
 *
 * DTO ONLY: no JPA annotations, so this cannot be persisted as it stands, which
 * matches AppointmentService still being an unimplemented stub.
 *
 * Note the model carries no patient or doctor reference, even though the patient
 * facing screen has to show which doctor is assigned. Those fields are missing
 * rather than deliberately omitted, and will be needed before the service can be
 * written.
 */
public class Appointment implements Serializable{
	private static final long serialVersionUID = 1L;

	private int appointmentId;
	/*
	 * LocalDateTime carries no zone or offset, so every appointment is implicitly in
	 * the hospital's local time. Safe for a single site, but it would become
	 * ambiguous across time zones or over a daylight saving transition.
	 */
	private LocalDateTime appointmentDate;
	private String reason;
	/*
	 * Lifecycle is SCHEDULED, then either COMPLETED or CANCELLED. Bookings start at
	 * SCHEDULED, and the terminal states are intended to be final.
	 */
	private AppointmentStatus status;
	
	public Appointment() {
		
		
	}
	
	public Appointment(int appointmentId, LocalDateTime appointmentDate, String reason, AppointmentStatus status) {
		this.appointmentId = appointmentId;
		this.appointmentDate = appointmentDate;
		this.reason = reason;
		this.status = status;
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
		return "Appointment [appointmentId=" + appointmentId + ", appointmentDate=" + appointmentDate + ", reason="
				+ reason + ", status=" + status + "]";
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
