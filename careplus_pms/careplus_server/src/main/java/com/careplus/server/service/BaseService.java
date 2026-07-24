package com.careplus.server.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.careplus.common.net.Response;
import com.careplus.server.util.HibernateUtil;

/*
 * Holds the Hibernate session and transaction handling that every service needs,
 * so each service only has to write its own queries. Putting it in one parent
 * class keeps the open/commit/close the same everywhere, and if we ever change
 * how transactions work we change it here once.
 *
 * The three fields below are normal instance fields with no locking on them, so
 * one service object can only handle one request at a time. That's why
 * ClientHandler builds its own set of services per connection instead of sharing
 * them: each client thread gets its own session and they can't tread on each
 * other.
 */
public abstract class BaseService {

	/*
	 * This logs under BaseService, so we stick the subclass name in the message to
	 * show which service the problem actually came from.
	 */
	private static final Logger logger = LogManager.getLogger(BaseService.class);

	Transaction transaction;
	Session session;
	Response resp;

	/*
	 * New session and transaction for every request. We keep them short on purpose
	 * rather than holding one open for the whole connection, otherwise a client
	 * sitting there doing nothing would tie up a database connection from the pool.
	 */
	protected void startSession() {

		session = HibernateUtil.getSession();

		transaction = session.beginTransaction();

		resp = new Response();

	}

	/*
	 * Every service calls this from a finally block, so it runs whether the work
	 * went through or the catch already rolled us back.
	 *
	 * We only commit if the transaction is still going. When a service catches an
	 * error it rolls back itself, and there's nothing left to commit after that.
	 * Asking Hibernate to commit anyway just throws, and we'd end up logging a
	 * "commit failed" error on every request that already failed for its own
	 * reason, which just buries the real problem.
	 */
	protected void endSession() {

		try {

			if (transaction.isActive()) {
				transaction.commit();
			}

		} catch (Exception e) {

			logger.error("Could not commit the transaction in {}", getClass().getSimpleName(), e);

		} finally {
			/*
			 * The close gets its own finally so the session always goes back to the pool,
			 * even when the commit above throws. It used to sit right after the commit,
			 * which meant a failed commit skipped it and leaked the connection. Do that
			 * enough times and the pool runs dry and the server stops answering anyone.
			 */
			session.close();
		}

	}
}
