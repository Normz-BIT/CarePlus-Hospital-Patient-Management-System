package com.careplus.common.enums;
/*
 * Status of Complaints
 *
 * Meant to go SUBMITTED, ASSIGNED, IN_PROGRESS, RESOLVED, with REOPENED
 * available from RESOLVED to push one back into the queue.
 *
 * The dashboard's "outstanding" count is everything that isn't RESOLVED, which
 * is why REOPENED needs to be its own value rather than us just clearing the
 * status.
 *
 * */
public enum ComplaintStatus {
	SUBMITTED,
	ASSIGNED,
	IN_PROGRESS,
	RESOLVED,
	REOPENED
}
