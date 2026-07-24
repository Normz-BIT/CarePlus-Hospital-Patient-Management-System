package com.careplus.common.enums;
/*
 * Status of Complaints
 *
 * Meant to go SUBMITTED, ASSIGNED, IN_PROGRESS, RESOLVED, with REOPENED
 * available from RESOLVED to push one back into the queue. Nothing actually
 * enforces that order though: anything holding a Complaint can set any value it
 * likes, so if we want the rule it has to go in ComplaintService where the
 * server can see the current state.
 *
 * The dashboard's "outstanding" count is everything that isn't RESOLVED, which
 * is why REOPENED needs to be its own value rather than us just clearing the
 * status.
 *
 * Saved by name with EnumType.STRING, so reordering these is safe but renaming
 * one leaves existing rows pointing at nothing.
 * */
public enum ComplaintStatus {
	SUBMITTED,
	ASSIGNED,
	IN_PROGRESS,
	RESOLVED,
	REOPENED
}
