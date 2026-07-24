package com.careplus.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.cfg.Configuration;

import com.careplus.server.util.HibernateUtil;
import com.careplus.server.util.ServerConsole;

/*
 * Database Reset Service
 * Drops careplus_db and builds it again by running careplus_create_database.sql
 *
 * The script starts with DROP DATABASE / CREATE DATABASE / USE, so this connects
 * to the MySQL server without picking a database first. If we connected to
 * careplus_db we'd be holding a connection open to the very thing the script is
 * about to drop.
 */
public class DatabaseResetService {

	private static final String SCRIPT_NAME = "careplus_create_database.sql";

	private static final Logger logger = LogManager.getLogger(DatabaseResetService.class);

	/*
	 * Runs the whole script. Progress goes back through the console callback so
	 * the caller can print it in the server window as it happens.
	 *
	 * Gives back how many statements ran, or -1 if the reset failed.
	 */
	public int resetDatabase(ServerConsole console) {

		List<String> statements;

		try {
			statements = readStatements();
		} catch (IOException e) {

			logger.error("The database script could not be read", e);
			report(console, "Unable to read " + SCRIPT_NAME + ": " + e.getMessage());

			return -1;
		}

		if (statements.isEmpty()) {

			logger.warn("The database script contained no statements");
			report(console, "No statements found in " + SCRIPT_NAME + ".");

			return -1;
		}

		report(console, "Read " + statements.size() + " statements from " + SCRIPT_NAME + ".");

		// Hibernate must let go of careplus_db before the script drops it.
		HibernateUtil.closeFactory();

		report(console, "Hibernate session factory closed.");

		int executed = executeStatements(statements, console);

		if (executed < 0) {

			// Bring Hibernate back up even on failure so the server stays usable.
			HibernateUtil.reconnect();

			return -1;
		}

		HibernateUtil.reconnect();
		report(console, "Hibernate session factory rebuilt.");

		return executed;
	}

	/*
	 * Builds the database from the script if it isn't there yet, and leaves it
	 * completely alone if it is. Returns true as long as the database exists by the
	 * time we're done, whether we found it or made it.
	 *
	 * This is what lets the server start against a MySQL install that has never had
	 * CarePlus on it. Hibernate can't build a session factory against a database
	 * that doesn't exist, so this has to run before HibernateUtil does.
	 *
	 * IMPORTANT: the existence check is a safety guard, not an optimisation.
	 * careplus_create_database.sql starts with DROP DATABASE, so running it on a
	 * database with real data in it would wipe every row. That's why we only ever
	 * create when it's genuinely missing and never touch an existing one, no matter
	 * how empty or broken it looks. Rebuilding an existing database is what the
	 * Reset button is for, and somebody has to press that on purpose.
	 */
	public boolean ensureDatabaseExists(ServerConsole console) {

		String schema;

		try {
			schema = schemaName(new Configuration().configure()
					.getProperties().getProperty("hibernate.connection.url"));

		} catch (RuntimeException e) {

			logger.error("The database configuration could not be read", e);
			report(console, "Unable to read the database configuration: " + e.getMessage());

			return false;
		}

		if (schema.isEmpty()) {

			logger.error("The JDBC url names no database, so there is nothing to create");
			report(console, "The JDBC url in hibernate.cfg.xml names no database.");

			return false;
		}

		try (Connection connection = openServerConnection()) {

			if (schemaExists(connection, schema)) {

				logger.info("Database {} already exists", schema);

				return true;
			}

		} catch (SQLException e) {

			/*
			 * Not being able to reach MySQL at all is a different problem from the
			 * database being missing, and only the second one is ours to fix. A refused
			 * connection means the server is off or the login is wrong, so say that
			 * instead of pretending the database is missing.
			 */
			logger.error("The MySQL server could not be reached", e);
			report(console, "Could not reach the MySQL server: " + e.getMessage());

			return false;
		}

		logger.info("Database {} was not found, creating it from {}", schema, SCRIPT_NAME);
		report(console, "Database " + schema + " was not found. Creating it from " + SCRIPT_NAME + "...");

		List<String> statements;

		try {
			statements = readStatements();

		} catch (IOException e) {

			logger.error("The database script could not be read", e);
			report(console, "Unable to read " + SCRIPT_NAME + ": " + e.getMessage());

			return false;
		}

		if (statements.isEmpty()) {

			logger.warn("The database script contained no statements");
			report(console, "No statements found in " + SCRIPT_NAME + ".");

			return false;
		}

		if (executeStatements(statements, console) < 0) {
			return false;
		}

		report(console, "Database " + schema + " created.");

		return true;
	}

	/*
	 * Runs the script and says how many statements went through, or -1 if one of
	 * them broke.
	 *
	 * Both the reset and the first-time creation above use this. Neither one messes
	 * with the session factory in here: reset closes and rebuilds it around this
	 * call, and creation runs before the factory even exists.
	 */
	private int executeStatements(List<String> statements, ServerConsole console) {

		/*
		 * Declared out here rather than inside the try so the catch can still see it
		 * and tell us which statement fell over. Declared inside, it'd be out of scope
		 * by the time we needed it.
		 */
		int executed = 0;

		/*
		 * try-with-resources closes the statement and the connection on every path out
		 * of here, including the SQLException below. That way a failed run can't leave
		 * a connection hanging open against a half-built database.
		 */
		try (Connection connection = openServerConnection();
				Statement statement = connection.createStatement()) {

			/*
			 * One after another on the same connection with autocommit left on, so each
			 * statement saves as it goes. We deliberately didn't wrap this in a
			 * transaction: MySQL can't roll back DDL like DROP DATABASE and CREATE TABLE
			 * anyway, so it would only look safer than it is. If it dies halfway the
			 * database is half built, and the fix is to run it again rather than undo it.
			 */
			for (String sql : statements) {

				statement.execute(sql);
				executed++;
			}

			report(console, "Executed " + executed + " statements successfully.");
			logger.info("Database script completed: {} statements executed", executed);

			return executed;

		} catch (SQLException e) {

			logger.error("The database script failed at statement {}", executed + 1, e);
			report(console, "Failed at statement " + (executed + 1) + ": " + e.getMessage());

			return -1;
		}
	}

	/*
	 * Asks MySQL's own catalogue whether the database is there, instead of trying
	 * to connect to it and treating a failure as "missing". A connection can fail
	 * for all sorts of reasons and only one of them is the database not existing.
	 */
	private boolean schemaExists(Connection connection, String schema) throws SQLException {

		try (PreparedStatement statement = connection
				.prepareStatement("SELECT 1 FROM information_schema.schemata WHERE schema_name = ?")) {

			statement.setString(1, schema);

			try (ResultSet results = statement.executeQuery()) {

				return results.next();
			}
		}
	}

	/*
	 * Opens a JDBC connection to the MySQL server with no schema selected.
	 */
	private Connection openServerConnection() throws SQLException {

		// Hibernate puts a "hibernate." prefix on the cfg.xml keys, and the login
		// details come out of hibernate.properties under the same prefix.
		Properties properties = new Configuration().configure().getProperties();

		return DriverManager.getConnection(
				stripSchema(properties.getProperty("hibernate.connection.url")),
				properties.getProperty("hibernate.connection.username"),
				properties.getProperty("hibernate.connection.password"));
	}

	/*
	 * Turns jdbc:mysql://host:3306/careplus_db?flags into
	 * jdbc:mysql://host:3306/?flags, so the connection survives the DROP DATABASE
	 * instead of being killed along with the database it's attached to.
	 */
	String stripSchema(String url) {

		if (url == null) {

			throw new IllegalStateException("No JDBC url is configured in hibernate.cfg.xml");
		}

		int query = url.indexOf('?');

		String base = query < 0 ? url : url.substring(0, query);
		String flags = query < 0 ? "" : url.substring(query);

		int lastSlash = base.lastIndexOf('/');

		// Keep the trailing slash; everything after it is the schema name.
		return lastSlash < 0 ? base + flags : base.substring(0, lastSlash + 1) + flags;
	}

	/*
	 * The opposite of stripSchema: gives back just the database name out of the
	 * url, or an empty String if the url doesn't name one.
	 *
	 * That check on the character before the slash is the important bit. In
	 * jdbc:mysql://localhost:3306 the last slash is the second one of the "//"
	 * before the host, so the text after it is "localhost:3306" and not a database
	 * at all. Without the check we'd try to create a database called
	 * "localhost:3306".
	 */
	String schemaName(String url) {

		if (url == null) {

			throw new IllegalStateException("No JDBC url is configured in hibernate.cfg.xml");
		}

		int query = url.indexOf('?');

		String base = (query < 0 ? url : url.substring(0, query)).trim();

		int lastSlash = base.lastIndexOf('/');

		if (lastSlash < 0 || (lastSlash > 0 && base.charAt(lastSlash - 1) == '/')) {
			return "";
		}

		return base.substring(lastSlash + 1);
	}

	/*
	 * Chops the script up on semicolons. This works because the script deliberately
	 * has no semicolons anywhere except at the end of statements, so don't put one
	 * inside a comment or a string in the .sql file or this will split in the wrong
	 * place. (Already caught us out once.)
	 */
	List<String> readStatements() throws IOException {

		List<String> statements = new ArrayList<>();

		for (String chunk : readScript().split(";")) {

			String sql = chunk.trim();

			if (containsSql(sql)) {
				statements.add(sql);
			}
		}

		return statements;
	}

	/*
	 * Reads the whole script from the classpath.
	 */
	private String readScript() throws IOException {

		try (InputStream in = getClass().getClassLoader().getResourceAsStream(SCRIPT_NAME)) {

			if (in == null) {

				throw new IOException(SCRIPT_NAME + " was not found on the classpath");
			}

			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	/*
	 * Each chunk drags along whatever comments came before its statement, so skip
	 * anything that's blank or nothing but -- comment lines.
	 */
	private boolean containsSql(String chunk) {

		for (String line : chunk.split("\n")) {

			String trimmed = line.trim();

			if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
				return true;
			}
		}

		return false;
	}

	private void report(ServerConsole console, String message) {

		if (console != null) {
			console.showln(message);
		}
	}
}
