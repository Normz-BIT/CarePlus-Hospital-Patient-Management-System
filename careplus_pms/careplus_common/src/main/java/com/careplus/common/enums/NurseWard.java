package com.careplus.common.enums;

/*
 * Type of Nurse
 *
 * Which ward a nurse is posted to. GENERAL is the default for anyone without a
 * specialist ward.
 *
 * Worth knowing the assigned-cases list doesn't actually use this. It goes by
 * who recorded the reading instead, because vitals carry a nurse_id and patients
 * have no ward on them. Doing it by ward would mean adding a ward column to
 * patient first.
 * */
public enum NurseWard {
	GENERAL,
	PEDIATRIC,
	ONCOLOGY,
	NEONATAL,
	EMERGENCY
}
