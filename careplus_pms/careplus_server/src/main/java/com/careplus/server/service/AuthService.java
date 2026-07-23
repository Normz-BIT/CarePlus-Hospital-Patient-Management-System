package com.careplus.server.service;

import com.careplus.common.model.Person;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * AuthService
 * Handles the single LOGIN request for both patients and staff.
 *
 * One method serves every kind of user because Person is the root of our joined
 * inheritance hierarchy: a single lookup resolves a Patient, Doctor, Nurse or
 * Receptionist without this class needing to know which it will get. The
 * concrete object travels back inside the Response, and the client reads its
 * UserRole to decide which dashboard features to open.
 *
 * This is the design decision the rest of the client depends on. Because the
 * role arrives with the signed in user, no screen has to ask the server what a
 * user is allowed to do.
 */
public class AuthService extends BaseService {

	public Response login(Request request) {

		/*
		 * These two keys are the contract between LoginController and this method. The
		 * parameter map is untyped, so the names have to match on both sides, which is
		 * why they are kept short and identical to the field names on the login form.
		 */
		String id = (String) request.getParams().get("id");

		String password = (String) request.getParams().get("password");

		startSession();

		try {
			/*
			 * IDs are normalised to uppercase because that is how they are stored, so a
			 * member of staff can type their ID in any case and still sign in.
			 *
			 * Looking the user up on Person rather than a specific subclass is what lets
			 * one method log in patients and all three staff types: the joined inheritance
			 * mapping returns whichever concrete type the row belongs to.
			 */
			Person person = (Person) session.find(Person.class, id.toUpperCase());

			/*
			 * An unknown ID gives back null here, and the resulting failure is caught below
			 * and reported with the same message as a wrong password.
			 
			 * Passwords are compared as plaintext
			 */
			if (person.getPassword().equals(password)) {

				resp.setData(person);
				resp.setSuccess(true);

				// TODO add log4j2
				resp.setMessage("Login Successfull");

			} else {

				/*
				 * Throwing here sends a wrong password down the same path as an unknown ID, so
				 * both produce an identical response. That is deliberate: telling the user
				 * which of the two was wrong would let someone work out which patient and staff
				 * IDs exist by trying them one at a time.
				 */
				throw new Exception("Invalid Login");
			}

		} catch (Exception e) {
			/*
			 * Login only reads, so the rollback changes no data. It is kept so every
			 * service ends a failed request the same way, which keeps the pattern
			 * consistent as the remaining services are written.
			 */
			transaction.rollback();
			resp.setSuccess(false);

			/*
			 * One message for every failure, whether the ID was unknown, the password was
			 * wrong or the lookup itself failed. See the note on the throw above.
			 */
			resp.setMessage("Login Unsuccessfull: Incorrect Password or Username");
			

		}

		finally {

			endSession();
		}

		return resp;

	}

}
