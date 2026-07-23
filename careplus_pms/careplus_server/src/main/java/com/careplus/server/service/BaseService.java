package com.careplus.server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careplus.common.net.Response;
import com.careplus.server.util.HibernateUtil;

/*
 * BaseService holds the Hibernate session and transaction handling that every
 * service needs, so the individual services are left to express only their own
 * query logic. Pulling this up into a parent class keeps the open, commit and
 * close sequence identical everywhere and means a change to how we manage
 * transactions is made in one place.
 *
 * The three fields below are ordinary instance state, so each service instance
 * belongs to a single request at a time. This is why ClientHandler creates its
 * own services per connection rather than sharing them: it gives each client
 * thread its own session without any locking.
 */
public abstract class BaseService {

	Transaction transaction;
	Session session;
	Response resp;

	/*
	 * Opens a fresh session and transaction per request. Sessions are deliberately
	 * short lived rather than held open for the life of the connection, so a client
	 * sitting idle does not pin a database connection from the pool.
	 */
	protected void startSession() {

		session = HibernateUtil.getSession();

		transaction = session.beginTransaction();

		resp = new Response();

	}

	protected void endSession() {

		try {
			/*
			 * Every service calls this from a finally block so the session is always tidied
			 * up, including on paths where the catch has already rolled back. Committing an
			 * transaction that was rolled back throws, which is why the catch below is
			 * broad: the rollback has already decided the outcome and the data is correct
			 * either way.
			 */
			transaction.commit();

			/*
			 * TODO: move this close into its own finally block so the session and its
			 * database connection are released even when the commit above throws.
			 */
			session.close();
		} catch (Exception e) {
			// TODO add log4j2
			e.printStackTrace();
		}

	}
}
