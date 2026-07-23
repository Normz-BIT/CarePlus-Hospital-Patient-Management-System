package com.careplus.common.enums;
/*
 * Status of Appointment
 *
 * Bookings start at SCHEDULED and end at either COMPLETED or CANCELLED, both of
 * which are intended to be terminal. A cancelled appointment is kept rather than
 * deleted so the patient's history stays intact and the slot's use is auditable.
 */
public enum AppointmentStatus {
	SCHEDULED,
	COMPLETED,
	CANCELLED
}
