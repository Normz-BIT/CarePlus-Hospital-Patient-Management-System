package com.careplus.common.net;

/**
 * The action codes of the client/server protocol. Every Request carries exactly
 * one of these, and ClientHandler switches on it to pick a service method.
 *
 * Using an enum rather than String action names means the compiler checks every
 * request type at both ends, and the switch in ClientHandler cannot be given a
 * value that does not exist.
 *
 * We declared the full set of actions up front, before the matching services
 * were written, so the client controllers could be built against the protocol we
 * had agreed as a group rather than waiting on the server side. Each value gains
 * its handler as the corresponding service is completed.
 *
 * Enums serialize by name rather than ordinal, so this list can safely be
 * reordered between client and server builds. Renaming a constant cannot: the
 * receiving side would no longer resolve the old name.
 */
public enum RequestType {
    LOGIN,
    GET_DOCTORS,
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
    ADD_DIAGNOSIS,
    UPDATE_DIAGNOSIS,
    GET_DIAGNOSIS_RECORDS,
    GET_ASSIGNED_PATIENTS,
    SCHEDULE_FOLLOWUP,
    GET_ASSIGNED_CASES,
    RECORD_VITALS,

    /*
     * Chat is polled rather than pushed: the server never initiates a write to an
     * idle client, so the client must call CHAT_POLL on a timer to discover new
     * messages. This keeps ClientHandler's read loop strictly request/response.
     */
    CHAT_SEND,
    CHAT_POLL
}
