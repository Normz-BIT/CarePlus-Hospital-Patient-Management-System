package com.careplus.server.dao;

import java.util.List;

import com.careplus.common.model.Appointment;

public interface AppointmentDAO extends GenericDAO<Appointment> {
	List<Appointment> forPatient(int patientId);

}
