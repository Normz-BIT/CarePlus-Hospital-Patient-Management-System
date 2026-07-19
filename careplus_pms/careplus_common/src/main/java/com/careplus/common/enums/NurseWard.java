package com.careplus.common.enums;

/*
 * Type of Nurse
 *
 * The ward a nurse is posted to, which is what should scope their assigned case
 * list: a nurse sees the patients on their own ward rather than the whole
 * hospital. That scoping is not implemented yet, since MedicalRecordService and
 * the assigned-cases query are still stubs.
 *
 * GENERAL is the fallback posting for a nurse with no specialist ward.
 * */
public enum NurseWard {
	GENERAL,
	PEDIATRIC,
	ONCOLOGY,
	NEONATAL,
	EMERGENCY
}
