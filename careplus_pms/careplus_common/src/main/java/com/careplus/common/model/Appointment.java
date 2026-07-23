package com.careplus.common.model;

import java.io.Serializable;
<<<<<<< HEAD
import java.util.Date;
=======
import java.time.LocalDateTime;

>>>>>>> stash
import com.careplus.common.enums.AppointmentStatus;


/*
 * Patient Books Appointments
 * Doctor Attends Appointments
 *
 * Appointment is one of the models we defined as a wire type first, so the
 * client screens could be built and demonstrated while the matching service was
 * still being written. It travels between client and server as a serialized
 * object and gains its JPA mapping when AppointmentService is completed.
 *
 * TODO: add the JPA annotations, plus the patient and doctor references the
 * booking screens need in order to show who an appointment is with.
 */
public class Appointment implements Serializable{
	private static final long serialVersionUID = 1L;

<<<<<<< HEAD
	private int appontmentId;
	private Date appointmentDate;
=======
	private int appointmentId;
	/*
	 * LocalDateTime carries no zone or offset, so every appointment is implicitly in
	 * the hospital's local time. Safe for a single site, but it would become
	 * ambiguous across time zones or over a daylight saving transition.
	 */
	private LocalDateTime appointmentDate;
>>>>>>> stash
	private String reason;
	/*
	 * Lifecycle is SCHEDULED, then either COMPLETED or CANCELLED. Bookings start at
	 * SCHEDULED, and the terminal states are intended to be final.
	 */
	private AppointmentStatus status;
	
	public Appointment() {
		
		
	}
	
<<<<<<< HEAD
	public Appointment(int appontmentId, Date appointmentDate, String reason, AppointmentStatus status) {
		this.appontmentId = appontmentId;
=======
	public Appointment(int appointmentId, LocalDateTime appointmentDate, String reason, AppointmentStatus status) {
		this.appointmentId = appointmentId;
>>>>>>> stash
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
<<<<<<< HEAD
	public Date getAppointmentDate() {
=======
	public LocalDateTime getAppointmentDate() {
>>>>>>> stash
		return appointmentDate;
	}
<<<<<<< HEAD
	public void setAppointmentDate(Date appointmentDate) {
=======
	public void setAppointmentDate(LocalDateTime appointmentDate) {
>>>>>>> stash
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
