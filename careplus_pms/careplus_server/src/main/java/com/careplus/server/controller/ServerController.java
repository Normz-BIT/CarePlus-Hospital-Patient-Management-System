package com.careplus.server.controller;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.server.net.Server;
import com.careplus.server.service.DatabaseResetService;
import com.careplus.server.util.HibernateUtil;
import com.careplus.server.view.ServerView;

/*
 * Server Controller
 * Wires the server window to the socket server and the database reset service
 */
public class ServerController {

	private final ServerView view;
	private final Server server = new Server();
	private final DatabaseResetService databaseResetService = new DatabaseResetService();

	private static final Logger logger = LogManager.getLogger(ServerController.class);

	public ServerController(ServerView view) {

		this.view = view;

		server.setConsole(view);

		view.setRunning(false);
		view.println("CarePlus server console ready.");

		initializeDatabase();
	}

	/*
	 * Builds the Hibernate session factory off the event dispatch thread so the
	 * window paints immediately instead of freezing on the first connection.
	 */
	private void initializeDatabase() {

		view.setResetEnabled(false);
		view.println("Connecting to the database...");

		new Thread(() -> {

			/*
			 * Constructed purely for the side effect of populating the static factory.
			 * The instance itself is discarded, since every consumer reaches Hibernate
			 * through HibernateUtil's static methods.
			 */
			new HibernateUtil();

			boolean connected = HibernateUtil.sessionFactory != null;

			if (connected) {
				logger.info("Hibernate session factory created");
				/*
				 * Called straight from this worker thread rather than through invokeLater,
				 * which is safe only because ServerView.println marshals onto the EDT itself.
				 * See the threading contract on the ServerConsole interface.
				 */
				view.println("Database connection established.");
			} else {
				logger.error("The Hibernate session factory could not be created");
				/*
				 * A failed connection is deliberately not fatal. Reset stays reachable below
				 * so an operator can rebuild a missing or corrupt schema, which is the usual
				 * cause of this branch on a fresh checkout.
				 */
				view.println("Database connection failed - check hibernate.properties. "
						+ "You can still use Clear/Reset Database to rebuild the schema.");
			}

			/*
			 * Enabling a button mutates Swing state, so unlike println this genuinely has
			 * to be marshalled onto the EDT.
			 */
			SwingUtilities.invokeLater(() -> view.setResetEnabled(true));

		}, "careplus-db-init").start();
	}

	/*
	 * Start listening for clients
	 */
	public void start() {

		if (server.start()) {
			view.setRunning(true);
		} else {
			view.showMessage("The server could not be started. See the console for details.");
		}
	}

	/*
	 * Stop listening and disconnect clients
	 */
	public void stop() {

		if (server.stop()) {
			view.setRunning(false);
		}
	}

	/*
	 * Drop careplus_db and rebuild it from careplus_create_database.sql.
	 *
	 * This destroys all existing data, so it is confirmed first and the server is
	 * stopped for the duration - clients holding sessions against the old schema
	 * would fail once it is dropped.
	 */
	public void resetDatabase() {

		boolean confirmed = view.confirm(
				"This permanently deletes careplus_db and rebuilds it from "
						+ "careplus_create_database.sql.\n\nAll existing data will be lost. Continue?",
				"Clear/Reset Database");

		if (!confirmed) {

			view.println("Database reset cancelled.");
			return;
		}

		/*
		 * Captured before the stop below, so the worker thread can restore whatever
		 * state the operator was actually in rather than always leaving the server
		 * stopped. Being effectively final is also what lets the lambda close over it.
		 */
		boolean wasRunning = server.isRunning();

		if (wasRunning) {

			view.println("Stopping the server before resetting the database...");

			server.stop();
			view.setRunning(false);
		}

		view.setResetEnabled(false);

		view.println("Resetting the database...");

		new Thread(() -> {

			/*
			 * Returns a negative count as the failure sentinel rather than throwing, which
			 * is why the branch below tests for less than zero instead of catching.
			 */
			int executed = databaseResetService.resetDatabase(view);

			/*
			 * Everything after the reset is bundled into one invokeLater so the button
			 * state, the dialog and the restart are applied as a single unit on the EDT,
			 * rather than interleaving with other queued UI work.
			 */
			SwingUtilities.invokeLater(() -> {

				view.setResetEnabled(true);

				if (executed < 0) {
					view.showMessage("The database reset failed. See the console for details.");
				} else {
					view.showMessage("Database reset successfully (" + executed + " statements executed).");
				}

				if (wasRunning) {

					view.println("Restarting the server...");

					if (server.start()) {
						view.setRunning(true);
					}
				}
			});

		}, "careplus-db-reset").start();
	}

	/*
	 * Server side entry point. The whole bootstrap runs inside invokeLater because
	 * Swing components must be created and touched only on the Event Dispatch
	 * Thread, including construction. The controller constructor then offloads the
	 * slow Hibernate startup back onto a worker so the window appears immediately.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			ServerView view = new ServerView();
			ServerController controller = new ServerController(view);

			view.registerActionListener(controller);
		});
	}

}
