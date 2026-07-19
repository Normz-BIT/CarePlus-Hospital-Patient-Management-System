package com.careplus.common.enums;
/*
 * Status of Complaints
 *
 * The intended progression is SUBMITTED, ASSIGNED, IN_PROGRESS, RESOLVED, with
 * REOPENED available from RESOLVED to send a complaint back into the queue. Nothing
 * enforces these transitions: any code holding a Complaint can set any value, so
 * the rule lives in ComplaintService once that is written rather than in the type.
 *
 * The receptionist dashboard's "outstanding" count means everything that is not
 * RESOLVED, which is why REOPENED has to exist as a distinct value rather than
 * being modelled by clearing the status.
 *
 * Persisted by name via EnumType.STRING wherever it is mapped, so reordering these
 * constants is safe but renaming one orphans existing rows.
 * */
public enum ComplaintStatus {
	SUBMITTED,
	ASSIGNED,
	IN_PROGRESS,
	RESOLVED,
	REOPENED
}
