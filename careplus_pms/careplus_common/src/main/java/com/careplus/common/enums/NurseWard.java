package com.careplus.common.enums;

/*
 * Type of Nurse
 *
 * The ward a nurse is posted to. This is what scopes a nurse's case list so they
 * see the patients on their own ward rather than every patient in the hospital.
 *
 * GENERAL is the default posting for a nurse with no specialist ward.
 *
 * TODO: apply this scoping in the assigned-cases query once that service is
 * written.
 * */
public enum NurseWard {
	GENERAL,
	PEDIATRIC,
	ONCOLOGY,
	NEONATAL,
	EMERGENCY
}
