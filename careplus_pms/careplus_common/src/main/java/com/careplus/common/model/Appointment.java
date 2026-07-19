package com.careplus.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.careplus.common.enums.AppointmentStatus;


/*
 * Patient Books Appointments
 * Doctor Attends Appointments
 */
public class Appointment implements Serializable{
	private static final long serialVersionUID = 1L;

	private int appontmentId;
	private LocalDateTime appointmentDate;
	private String reason;
	private AppointmentStatus status;
	
	public Appointment() {
		
		
	}
	
	public Appointment(int appontmentId, LocalDateTime appointmentDate, String reason, AppointmentStatus status) {
		this.appontmentId = appontmentId;
		this.appointmentDate = appointmentDate;
		this.reason = reason;
		this.status = status;
	}
	public int getAppontmentId() {
		return appontmentId;
	}
	public void setAppontmentId(int appontmentId) {
		this.appontmentId = appontmentId;
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
	public int hashCode() {
		return Integer.hashCode(appontmentId);
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
		return appontmentId == other.appontmentId;
	}
	
	
	
}
