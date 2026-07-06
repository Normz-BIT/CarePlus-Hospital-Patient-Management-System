package com.careplus.server.dao;

import java.util.List;

import com.careplus.common.model.Payment;

public interface PaymentDAO extends GenericDAO<Payment> {
	List<Payment> forPatient(int patientId);

}
