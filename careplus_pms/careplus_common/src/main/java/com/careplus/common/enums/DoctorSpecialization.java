package com.careplus.common.enums;
/*
 * Types of Doctors
 *
 * What a doctor specialises in. Not to be confused with Department, which is a
 * separate enum.
 *
 * GENERAL is the fallback for a doctor with no particular specialism, so leave
 * it in as the default.
 * */
public enum DoctorSpecialization {
	GENERAL,
	DERMATOLOGY,
	CARDIOLOGY,
	PSYCHIATRY,
	PEDIATRICS
}
