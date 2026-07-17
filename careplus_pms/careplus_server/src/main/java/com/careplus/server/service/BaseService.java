package com.careplus.server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careplus.common.net.Response;
import com.careplus.server.util.HibernateUtil;

public abstract class BaseService {

	Transaction transaction;
	Session session;
	Response resp;

	protected void startSession() {

		session = HibernateUtil.getSession();

		transaction = session.beginTransaction();
		
		resp = new Response();

	}
	
	protected void endSession() {
		
		try {
			transaction.commit();
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
