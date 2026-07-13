package com.careplus.server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careplus.common.model.Person;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;
import com.careplus.server.util.HibernateUtil;

public class AuthService {

	public Response login(Request request) {

		String id = (String) request.getParams().get("id");
		
		
		String password = (String) request.getParams().get("password");

		Session session = HibernateUtil.getSession();

		Transaction transaction = session.beginTransaction();

		Person person = (Person) session.find(Person.class, id);

		// check to make sure passwords match

		//
		
		System.out.println("Read Person: "+person.getFirstName());
		
		

		transaction.commit();
		session.close();

		Response resp = new Response();
		resp.setData(person);
		resp.setSuccess(true);

		return resp;

		// TODO
		// read the mysql database for this person using the id and compare the password

	}
}
