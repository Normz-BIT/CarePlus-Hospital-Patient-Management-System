package com.careplus.server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careplus.common.net.Response;
import com.careplus.server.util.HibernateUtil;

/*
 * BaseService
 * Shared Hibernate session and transaction handling for the server's services.
 *
 * These three fields are plain instance state with no synchronization, which makes
 * every subclass single threaded by contract. That holds today only because
 * ClientHandler constructs its own service instances per connection. Making any
 * service static or sharing one instance between client threads would let two
 * requests overwrite each other's session and commit each other's work.
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
			 * Called unconditionally from every subclass finally block, including on paths
			 * where the catch block already called rollback(). Committing an
			 * already rolled back transaction throws IllegalStateException, which is then
			 * swallowed below. The rollback still wins, so the data stays correct, but the
			 * error path is noisier than it looks.
			 */
			transaction.commit();
			/*
			 * Reached only if commit() succeeded. A commit failure skips this close and
			 * leaks the session along with its database connection, since no finally block
			 * covers it. Moving the close into its own finally would make cleanup
			 * unconditional.
			 */
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
