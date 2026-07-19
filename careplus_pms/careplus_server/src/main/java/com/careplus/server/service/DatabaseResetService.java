package com.careplus.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
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
 * Drops and recreates careplus_db by running careplus_create_database.sql
 *
 * The script itself begins with DROP DATABASE / CREATE DATABASE / USE, so this
 * service connects to the MySQL server without selecting a schema - connecting
 * to careplus_db would mean holding an open connection to the database the
 * script is about to drop.
 */
public class DatabaseResetService {

	private static final String SCRIPT_NAME = "careplus_create_database.sql";

	private static final Logger logger = LogManager.getLogger(DatabaseResetService.class);

	/*
	 * Runs the script end to end. Progress is reported through the console
	 * callback so the caller can mirror it into the server window.
	 *
	 * Returns the number of statements executed, or -1 if the reset failed.
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

		/*
		 * Declared outside the try so it survives into the catch, where it identifies
		 * which statement failed. A variable scoped to the block would be unreachable
		 * from the error path.
		 */
		int executed = 0;

		/*
		 * try-with-resources closes the statement and the connection in reverse order
		 * on every path, including the SQLException below, so a failed reset cannot
		 * strand an open connection against a half rebuilt schema.
		 */
		try (Connection connection = openServerConnection();
				Statement statement = connection.createStatement()) {

			/*
			 * Executed sequentially on one connection with autocommit left on, so each
			 * statement commits as it runs. There is deliberately no surrounding
			 * transaction: MySQL cannot roll back DDL such as DROP DATABASE and CREATE
			 * TABLE, so wrapping this would give a false impression of atomicity. The
			 * practical consequence is that a mid script failure leaves the schema
			 * partially built, and the fix is to run the reset again rather than to undo it.
			 */
			for (String sql : statements) {

				statement.execute(sql);
				executed++;
			}

			report(console, "Executed " + executed + " statements successfully.");
			logger.info("Database reset completed: {} statements executed", executed);

		} catch (SQLException e) {

			logger.error("The database reset failed at statement {}", executed + 1, e);
			report(console, "Reset failed at statement " + (executed + 1) + ": " + e.getMessage());

			// Bring Hibernate back up even on failure so the server stays usable.
			HibernateUtil.reconnect();

			return -1;
		}

		HibernateUtil.reconnect();
		report(console, "Hibernate session factory rebuilt.");

		return executed;
	}

	/*
	 * Opens a JDBC connection to the MySQL server with no schema selected.
	 */
	private Connection openServerConnection() throws SQLException {

		// Hibernate normalises cfg.xml keys to the hibernate.* prefix, and the
		// credentials come from hibernate.properties under the same prefix.
		Properties properties = new Configuration().configure().getProperties();

		return DriverManager.getConnection(
				stripSchema(properties.getProperty("hibernate.connection.url")),
				properties.getProperty("hibernate.connection.username"),
				properties.getProperty("hibernate.connection.password"));
	}

	/*
	 * Turns jdbc:mysql://host:3306/careplus_db?flags into
	 * jdbc:mysql://host:3306/?flags so the connection outlives the DROP DATABASE.
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
	 * Splits the script on ';'. Safe because no statement terminator is ambiguous:
	 * the script deliberately contains no semicolons inside string literals or
	 * comments (see the note above the sample data in the .sql file).
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
	 * A chunk carries the comments that preceded its statement, so skip any chunk
	 * that is blank or nothing but -- comment lines.
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
			console.println(message);
		}
	}
}
