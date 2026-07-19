package com.careplus.server.service;

import com.careplus.common.model.Person;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * AuthService
 * Handles the single LOGIN request for both patients and staff.
 *
 * Person is the joined inheritance root, so one lookup resolves a Patient, Doctor,
 * Nurse or Receptionist without the caller knowing which. The concrete subclass
 * comes back inside the Response, and the client reads its UserRole to decide
 * which dashboard features to expose.
 */
public class AuthService extends BaseService {

	public Response login(Request request) {

		/*
		 * Keys must match exactly what the client put in the Request map. These are an
		 * untyped convention, so a rename on either side fails at runtime as a null or
		 * a ClassCastException rather than at compile time.
		 */
		String id = (String) request.getParams().get("id");

		String password = (String) request.getParams().get("password");

		startSession();

		try {
			/*
			 * IDs are normalised to uppercase because they are stored that way, letting
			 * staff type their ID in any case. Note this dereferences id without a null
			 * check, so a malformed Request throws here rather than reporting a clean
			 * validation error.
			 */
			Person person = (Person) session.find(Person.class, id.toUpperCase());

			/*
			 * find() returns null for an unknown ID, so this line throws a
			 * NullPointerException that the catch below converts into the generic failure
			 * message. Unknown user is therefore handled by accident rather than by design,
			 * which is worth making explicit before someone "fixes" the catch to be
			 * narrower.
			 */
			// TODO change to log4j2
			System.out.println("Read : " + person.toString());

			/*
			 * Passwords are compared as plaintext, meaning they are stored unhashed in the
			 * database and travel unencrypted over the socket. Acceptable for a coursework
			 * prototype, but this is the first thing that would need to change for real
			 * patient data: store a salted hash and compare digests.
			 */
			if (person.getPassword().equals(password)) {

				/*
				 * This prints the stored password concatenated with the submitted one to
				 * stdout on every successful login, writing credentials into the server
				 * console and any captured log. It should be removed rather than migrated to
				 * log4j2.
				 */
				System.out.println(person.getPassword() + password);

				resp.setData(person);
				resp.setSuccess(true);

				// TODO add log4j2
				resp.setMessage("Login Sucessfull");

			} else {

				/*
				 * Exception as control flow: this exists purely to jump into the shared catch
				 * block so that a wrong password and an unknown ID produce byte identical
				 * responses. That uniformity is deliberate, since a distinct "no such user"
				 * message would let an attacker enumerate valid patient and staff IDs.
				 */
				throw new Exception("Invalid Login");
			}

		} catch (Exception e) {
			/*
			 * Rolling back a read only transaction is a no op against the data, but it does
			 * mark the transaction as inactive, which is what makes the commit inside
			 * endSession() throw. See the note in BaseService.endSession.
			 */
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
