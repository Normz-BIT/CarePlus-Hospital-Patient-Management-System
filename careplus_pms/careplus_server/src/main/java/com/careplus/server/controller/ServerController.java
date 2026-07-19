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

			new HibernateUtil();

			boolean connected = HibernateUtil.sessionFactory != null;

			if (connected) {
				logger.info("Hibernate session factory created");
				view.println("Database connection established.");
			} else {
				logger.error("The Hibernate session factory could not be created");
				view.println("Database connection failed - check hibernate.properties. "
						+ "You can still use Clear/Reset Database to rebuild the schema.");
			}

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

		boolean wasRunning = server.isRunning();

		if (wasRunning) {

			view.println("Stopping the server before resetting the database...");

			server.stop();
			view.setRunning(false);
		}

		view.setResetEnabled(false);

		view.println("Resetting the database...");

		new Thread(() -> {

			int executed = databaseResetService.resetDatabase(view);

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

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			ServerView view = new ServerView();
			ServerController controller = new ServerController(view);

			view.registerActionListener(controller);
		});
	}

}
