package com.careplus.common.enums;
/*
 * Status of Appointment
 *
 * Bookings start at SCHEDULED and end up either COMPLETED or CANCELLED, and
 * nothing should come back out of those two. We keep cancelled appointments
 * rather than deleting them so the patient's history stays complete and you can
 * still see the slot was used.
 */
public enum AppointmentStatus {
	SCHEDULED,
	COMPLETED,
	CANCELLED
}
