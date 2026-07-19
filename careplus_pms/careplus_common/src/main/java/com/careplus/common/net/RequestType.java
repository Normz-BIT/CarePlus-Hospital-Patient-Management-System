package com.careplus.common.net;

/**
 * The action codes of the client/server protocol. Every Request carries exactly
 * one of these, and ClientHandler switches on it to pick a service method.
 *
 * Two things to be aware of before adding or reordering values:
 *
 * 1. Enums serialize by name, not by ordinal, so reordering this list is safe
 *    across client and server builds. Renaming a constant is not: the receiving
 *    side throws InvalidObjectException when it cannot resolve the name.
 *
 * 2. Declaring a constant here does not make it work. The dispatch switch in
 *    ClientHandler currently implements only LOGIN, MAKE_PAYMENT and
 *    GET_MY_PAYMENTS. Every other value below falls through to the default
 *    branch and returns an all null Response, because the corresponding services
 *    (Appointment, Chat, Complaint, MedicalRecord) are still unimplemented
 *    stubs. The constants are declared ahead of the services so that client side
 *    controllers can be written against the final protocol, but a controller
 *    sending any of them today gets silence rather than data.
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
