package com.careplus.common.net;

/*
 * The various request that can be sent from the client to the server
 */
public enum RequestType {

	LOGIN_PATIENT,
	LOGIN_EMPLOYEE,
	SUBMIT_COMPLAINT,
	GET_MY_COMPLAINTS,
	GET_MY_APPOINTMENTS,
	GET_MY_PAYMENTS,
	GET_ALL_COMPLAINTS,
	RESPOND_TO_COMPLAINT,
	ASSIGN_STAFF,
	ADD_DIAGNOSIS,
	RECORD_VITALS,
	CHAT_SEND,
	CHAT_POLL,
}
