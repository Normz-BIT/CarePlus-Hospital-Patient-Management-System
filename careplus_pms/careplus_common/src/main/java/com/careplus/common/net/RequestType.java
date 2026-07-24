package com.careplus.common.net;

/**
 * All the actions our client/server protocol supports. Every Request carries one
 * of these and ClientHandler switches on it to pick which service to call.
 *
 * An enum rather than String action names means the compiler checks these at
 * both ends, and nobody can hand the switch in ClientHandler a value that
 * doesn't exist.
 *
 * We wrote the whole list out up front, before most of the services existed, so
 * everyone could build their client screens against the protocol we'd agreed as
 * a group instead of waiting on the server side to catch up.
 *
 */
public enum RequestType {
    LOGIN,
    GET_DOCTORS,

    /*
     * "PAT0001 - Andre Campbell" text for the staff screens that need to pick a
     * patient (staff chat and vitals), so nobody has to type an ID by hand and
     * end up filing something against the wrong patient.
     */
    GET_PATIENTS,

    /*
     * The mirror of GET_PATIENTS: "STF0001 - Karen Reid (DOCTOR)" text for the
     * patient's chat screen, so they pick an actual person to message rather than
     * a job title and hope it reaches the right one.
     */
    GET_STAFF,
    GET_DEPARTMENTS,
    SCHEDULE_APPOINTMENT,  
    UPDATE_APPOINTMENT,
    CANCEL_APPOINTMENT,
    GET_MY_APPOINTMENTS,
    SUBMIT_COMPLAINT,
    UPDATE_COMPLAINT,
    DELETE_COMPLAINT,
    GET_MY_COMPLAINTS,
    GET_MY_PAYMENTS,
    MAKE_PAYMENT,
    GET_MEDICAL_RECORDS,
    GET_ALL_COMPLAINTS,
    RESPOND_TO_COMPLAINT,
    ASSIGN_STAFF,
    GET_STAFF_ASSIGNMENTS,

    /*
     * The receptionist dashboard numbers (total, resolved and outstanding per
     * category). ReportService does this as a SQL GROUP BY so the counts cover
     * every complaint, not just the rows the screen happens to have loaded.
     */
    GET_DASHBOARD_STATS,
    ADD_DIAGNOSIS,
    UPDATE_DIAGNOSIS,
    GET_DIAGNOSIS_RECORDS,
    GET_ASSIGNED_PATIENTS,
    SCHEDULE_FOLLOWUP,
    GET_ASSIGNED_CASES,
    RECORD_VITALS,

    /*
     * Chat is polled, not pushed. The server never writes to a client out of the
     * blue, so the client has to keep calling CHAT_POLL to find new messages.
     * Keeps ClientHandler's loop strictly one request, one response.
     */
    CHAT_SEND,
    CHAT_POLL
}
