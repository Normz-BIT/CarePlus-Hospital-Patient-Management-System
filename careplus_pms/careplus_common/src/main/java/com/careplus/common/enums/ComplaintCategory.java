package com.careplus.common.enums;

/*
 * Category of Complaint
 *
 * The three categories a patient chooses from when filing, and the axis the
 * receptionist dashboard groups its totals by.
 *
 * Note that the receptionist's filter control also offers an "All" option, which
 * is deliberately not a constant here: it is a UI concept meaning "no filter"
 * rather than a category a complaint can be filed under. That is why
 * ComplaintService.findByCategory takes a String rather than this enum.
 * **/
public enum ComplaintCategory {
	GENERAL_HEALTH,
	MEDICATION_CONCERN,
	APPOINTMENT_ISSUE

}
