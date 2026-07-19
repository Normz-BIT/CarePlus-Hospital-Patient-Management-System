package com.careplus.server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.cfg.Configuration;

import com.careplus.server.util.HibernateUtil;

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
	public int resetDatabase(Consumer<String> console) {

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

		int executed = 0;

		try (Connection connection = openServerConnection();
				Statement statement = connection.createStatement()) {

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

		Properties properties = new Configuration().configure().getProperties();

		String url = property(properties, "hibernate.connection.url", "connection.url");
		String username = property(properties, "hibernate.connection.username", "connection.username");
		String password = property(properties, "hibernate.connection.password", "connection.password");

		return DriverManager.getConnection(stripSchema(url), username, password);
	}

	private String property(Properties properties, String prefixedKey, String plainKey) {

		String value = properties.getProperty(prefixedKey);

		return value != null ? value : properties.getProperty(plainKey);
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
	 * Splits the script into executable statements.
	 *
	 * Splitting naively on ';' would corrupt the seed data - several inserts hold
	 * semicolons inside quoted text, for example 'Type 2 diabetes; penicillin
	 * allergy.' - so this tracks quoting and comments while scanning.
	 */
	List<String> readStatements() throws IOException {

		List<String> statements = new ArrayList<>();
		StringBuilder current = new StringBuilder();

		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;

		try (InputStream in = getClass().getClassLoader().getResourceAsStream(SCRIPT_NAME)) {

			if (in == null) {

				throw new IOException(SCRIPT_NAME + " was not found on the classpath");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

			String line;

			while ((line = reader.readLine()) != null) {

				for (int i = 0; i < line.length(); i++) {

					char c = line.charAt(i);

					// A -- comment runs to end of line, but only outside quotes.
					if (!inSingleQuote && !inDoubleQuote
							&& c == '-' && i + 1 < line.length() && line.charAt(i + 1) == '-') {

						break;
					}

					if (c == '\'' && !inDoubleQuote && !isEscaped(line, i)) {
						inSingleQuote = !inSingleQuote;
					} else if (c == '"' && !inSingleQuote && !isEscaped(line, i)) {
						inDoubleQuote = !inDoubleQuote;
					}

					if (c == ';' && !inSingleQuote && !inDoubleQuote) {

						add(statements, current);
						current.setLength(0);

						continue;
					}

					current.append(c);
				}

				// Preserve the line break so -- comments cannot swallow the next line.
				current.append('\n');
			}
		}

		add(statements, current);

		return statements;
	}

	/*
	 * True when the character at index is escaped by an odd run of backslashes.
	 */
	private boolean isEscaped(String line, int index) {

		int backslashes = 0;

		for (int i = index - 1; i >= 0 && line.charAt(i) == '\\'; i--)
			backslashes++;

		return backslashes % 2 == 1;
	}

	private void add(List<String> statements, StringBuilder current) {

		String sql = current.toString().trim();

		if (!sql.isEmpty()) {
			statements.add(sql);
		}
	}

	private void report(Consumer<String> console, String message) {

		if (console != null) {
			console.accept(message);
		}
	}
}
