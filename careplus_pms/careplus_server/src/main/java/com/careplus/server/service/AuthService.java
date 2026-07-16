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

		Response resp = new Response();

		if (person != null) {
			// TODO change to log4j2
			System.out.println("Read : " + person.toString());

			if (person.getPassword().equals(password)) {
				
				System.out.println(person.getPassword() + password);
				
				resp.setData(person);
				resp.setSuccess(true);
				resp.setMessage("Login Sucessfull");
				// TODO add log4j2
			}

		} else {

			resp.setSuccess(false);
			resp.setMessage("Login Unsucessfull: Incorrect Password or Username");
			// TODO add log4j2

		}

		transaction.commit();
		session.close();

		return resp;

	}
}
