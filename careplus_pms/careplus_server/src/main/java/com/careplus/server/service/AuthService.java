package com.careplus.server.service;

import com.careplus.common.model.Person;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

public class AuthService extends BaseService {

	public Response login(Request request) {

		String id = (String) request.getParams().get("id");

		String password = (String) request.getParams().get("password");

		startSession();

		try {
			Person person = (Person) session.find(Person.class, id.toUpperCase());

			// TODO change to log4j2
			System.out.println("Read : " + person.toString());

			if (person.getPassword().equals(password)) {

				System.out.println(person.getPassword() + password);

				resp.setData(person);
				resp.setSuccess(true);

				// TODO add log4j2
				resp.setMessage("Login Sucessfull");

			} else {

				throw new Exception("Invalid Login");
			}
			
		} catch (Exception e) {
			transaction.rollback();
			resp.setSuccess(false);
			resp.setMessage("Login Unsucessfull: Incorrect Password or Username");
			// TODO add log4j2

		}

		finally {

			endSession();
		}

		return resp;

	}

}
