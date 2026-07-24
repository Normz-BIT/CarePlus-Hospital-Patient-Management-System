package com.careplus.common.enums;

/*
 * Category of Complaint
 *
 * The three the brief asks for. A patient picks one when filing, and it's what
 * the receptionist dashboard groups its totals by.
 *
 * The receptionist's filter also has an "All" option, which is deliberately not
 * in here: that's a screen thing meaning "don't filter", not something a
 * complaint can actually be filed under.
 * **/
public enum ComplaintCategory {
	GENERAL_HEALTH,
	MEDICATION_CONCERN,
	APPOINTMENT_ISSUE

}
