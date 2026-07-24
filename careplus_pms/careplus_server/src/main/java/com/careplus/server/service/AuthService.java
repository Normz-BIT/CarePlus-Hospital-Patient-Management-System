package com.careplus.server.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.model.Person;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * AuthService
 * Handles the one LOGIN request for both patients and staff.
 *
 * One method covers every kind of user because Person is the top of our joined
 * hierarchy: a single lookup comes back as a Patient, Doctor, Nurse or
 * Receptionist without this class caring which. The actual object goes back
 * inside the Response and the client reads its role to work out which menus to
 * show.
 *
 * The rest of the client leans on this: because the role arrives with the user,
 * no screen has to go back and ask the server what someone is allowed to do.
 */
public class AuthService extends BaseService {

	private static final Logger logger = LogManager.getLogger(AuthService.class);

	public Response login(Request request) {

		/*
		 * These two key names are the deal between LoginController and this method.
		 * The parameter map is untyped so nothing checks them at compile time, they
		 * just have to match on both sides. Don't rename one without the other.
		 */
		String id = (String) request.getParams().get("id");

		String password = (String) request.getParams().get("password");

		startSession();

		try {
			/*
			 * We uppercase the ID because that's how they're stored, so someone can type
			 * "pat0001" or "PAT0001" and still get in.
			 *
			 * Looking it up on Person rather than a specific subclass is what lets this
			 * one method log in patients and all three staff types: the joined mapping
			 * hands back whichever type the row actually is.
			 */
			Person person = (Person) session.find(Person.class, id.toUpperCase());

			/*
			 * An unknown ID comes back null here, so this line throws and the catch below
			 * reports it with the same message as a wrong password. Passwords are still
			 * plain text, so a straight equals is all we need.
			 */
			if (person.getPassword().equals(password)) {

				resp.setData(person);
				resp.setSuccess(true);

				resp.setMessage("Login Successful");

				logger.info("{} logged in as {}", person.getPersonId(), person.getRole());

			} else {

				/*
				 * Throwing here sends a wrong password down the same path as an unknown ID so
				 * both get an identical answer. That's on purpose: if we said which one was
				 * wrong, someone could sit there guessing IDs and work out which ones exist.
				 */
				throw new Exception("Invalid Login");
			}

		} catch (Exception e) {
			/*
			 * Login only reads so this rollback doesn't undo anything. We kept it so every
			 * service ends a failed request the exact same way.
			 */
			transaction.rollback();
			resp.setSuccess(false);

			/*
			 * Same message no matter what went wrong: unknown ID, wrong password, or the
			 * lookup itself failing. See the note on the throw above.
			 *
			 * The log doesn't say which part was wrong either, for the same reason: no
			 * point hiding it from the screen and then writing it into the log file.
			 */
			resp.setMessage("Login Unsuccessful: Incorrect Password or Username");

			logger.warn("Failed login attempt for ID {}", id);

		}

		finally {

			endSession();
		}

		return resp;

	}

}
