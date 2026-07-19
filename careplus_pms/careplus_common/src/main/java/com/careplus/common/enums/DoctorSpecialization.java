package com.careplus.common.enums;
/*
 * Types of Doctors
 *
 * Doubles as the department list the patient picks from when booking, which is
 * why the appointment screen can populate its department combo from a doctor
 * lookup rather than from a separate department table.
 *
 * GENERAL is the fallback for a doctor with no declared specialism, so it should
 * stay present as a default.
 * */
public enum DoctorSpecialization {
	GENERAL,
	DERMATOLOGY,
	CARDIOLOGY,
	PSYCHIATRY,
	PEDIATRICS
}
