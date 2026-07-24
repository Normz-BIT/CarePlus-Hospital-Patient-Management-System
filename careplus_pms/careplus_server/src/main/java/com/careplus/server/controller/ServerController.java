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
		view.showln("CarePlus server console ready.");

		initializeDatabase();
	}

	/*
	 * Builds the Hibernate session factory on a background thread so the window
	 * actually paints instead of sitting frozen while the first connection is made.
	 */
	private void initializeDatabase() {

		view.setResetEnabled(false);
		view.showln("Connecting to the database...");

		new Thread(() -> {

			/*
			 * Builds careplus_db from the script the first time the server runs against a
			 * MySQL that's never had it. Hibernate can't build a factory against a
			 * database that isn't there, so this has to go first.
			 *
			 * If the database already exists this does nothing at all, so it's a no-op on
			 * every run after the first. We ignore the return value on purpose: any
			 * failure is already printed to the console, and the check below handles a
			 * missing connection by leaving the Reset button available.
			 */
			databaseResetService.ensureDatabaseExists(view);

			/*
			 * We build one of these purely for the side effect of filling in the static
			 * factory. The object itself gets thrown away, because everything else reaches
			 * Hibernate through HibernateUtil's static methods anyway.
			 */
			new HibernateUtil();

			boolean connected = HibernateUtil.sessionFactory != null;

			if (connected) {
				logger.info("Hibernate session factory created");
				/*
				 * Called straight from this worker thread instead of through invokeLater.
				 * That's only safe because ServerView.showln puts itself on the Swing thread
				 * internally. See the note on the ServerConsole interface.
				 */
				view.showln("Database connection established.");
			} else {
				logger.error("The Hibernate session factory could not be created");
				/*
				 * A failed connection isn't fatal on purpose. We leave the Reset button
				 * enabled below so somebody can rebuild a missing or broken database, which
				 * is usually why you end up here on a fresh checkout.
				 */
				view.showln("Database connection failed - check hibernate.properties. "
						+ "You can still use Clear/Reset Database to rebuild the schema.");
			}

			/*
			 * Enabling a button actually changes Swing state, so unlike showln above this
			 * one really does have to go through invokeLater.
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
	 * This destroys all existing data,
	 */
	public void resetDatabase() {

		boolean confirmed = view.confirm(
				"This permanently deletes careplus_db and rebuilds it from "
						+ "careplus_create_database.sql.\n\nAll"
						+ " existing data will be lost. Continue?",
				"Clear/Reset Database");

		if (!confirmed) {
			view.showln("Database reset cancelled.");
			return;
		}

		/*
		 * Grabbed before the stop below, so afterwards we can put the server back the
		 * way we found it instead of always leaving it stopped. It also has to be
		 * effectively final for the lambda further down to use it.
		 */
		boolean wasRunning = server.isRunning();

		if (wasRunning) {

			view.showln("Stopping the server before resetting the database...");

			server.stop();
			view.setRunning(false);
		}

		view.setResetEnabled(false);

		view.showln("Resetting the database...");

		new Thread(() -> {

			/*
			 * This gives back a negative number to mean failure rather than throwing,
			 * which is why we test for less than zero below instead of catching anything.
			 */
			int executed = databaseResetService.resetDatabase(view);

			/*
			 * Everything after the reset goes in one invokeLater so the button, the dialog
			 * and the restart all happen together as one lot, instead of getting mixed in
			 * with whatever else Swing has queued up.
			 */
			SwingUtilities.invokeLater(() -> {

				view.setResetEnabled(true);

				if (executed < 0) {
					view.showMessage("The database reset failed. See the console for details.");
				} else {
					view.showMessage("Database reset successfully (" + executed + " statements executed).");
				}

				if (wasRunning) {

					view.showln("Restarting the server...");

					if (server.start()) {
						view.setRunning(true);
					}
				}
			});

		}, "careplus-db-reset").start();
	}

	/*
	 * Where the server starts. The whole thing goes inside invokeLater because Swing
	 * components have to be created and touched only on the Swing thread, building
	 * them included. The controller constructor then pushes the slow Hibernate
	 * startup back onto a worker thread so the window shows up straight away.
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			ServerView view = new ServerView();
			ServerController controller = new ServerController(view);

			view.registerActionListener(controller);
		});
	}

}
