package com.careplus.common.enums;

/*
 * The hospital's departments, defined once here for everything: the column on
 * employee, the assignment screen's combo and the booking screen's combo all
 * read from this now.
 *
 * This used to be a plain String column plus a hardcoded list over in
 * StaffAssignmentController, and the two didn't match each other or the seed
 * data. Making it an enum means the compiler keeps them in step for us.
 *
 * These names have to match what the seed script inserts, so if you rename one
 * change careplus_create_database.sql at the same time.
 */
public enum Department {
	INTERNAL_MEDICINE,
	CARDIOLOGY,
	GENERAL_WARD,
	EMERGENCY,
	FRONT_DESK
}
