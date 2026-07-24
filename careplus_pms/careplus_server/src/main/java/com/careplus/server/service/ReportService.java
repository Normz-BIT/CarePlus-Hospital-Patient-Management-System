package com.careplus.server.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.cfg.Configuration;

import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

/*
 * ReportService
 * The plain JDBC part of the server: the staff assignment screen and the
 * receptionist's dashboard numbers.
 *
 * We kept these two on JDBC on purpose. The brief wants JDBC as well as
 * Hibernate, and these were the natural fit: the assignment table is just rows
 * of columns rather than objects, and the dashboard is a GROUP BY that reads
 * better as SQL than it would as entities. Everything else goes through
 * Hibernate.
 *
 * Doesn't extend BaseService because there's no Hibernate session in here. Each
 * method opens its own JDBC connection and try-with-resources closes it.
 */
public class ReportService {

	private static final Logger logger = LogManager.getLogger(ReportService.class);

	/*
	 * Rows for the staff assignment table: every complaint that has somebody
	 * assigned to it, with that employee's department. Comes back as Object[] rows
	 * because the table on screen just draws columns, it doesn't need objects.
	 */
	public Response getStaffAssignments(Request request) {

		Response resp = new Response();

		String sql = "SELECT c.complaint_id, c.assigned_to, e.department, c.status "
				+ "FROM complaint c JOIN employee e ON e.person_id = c.assigned_to "
				+ "ORDER BY c.complaint_id";

		try (Connection connection = openConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet results = statement.executeQuery()) {

			/*
			 * Has to be an ArrayList specifically, not just any List: whatever we put in a
			 * Response gets serialized over the socket, and ArrayList is Serializable.
			 */
			ArrayList<Object[]> rows = new ArrayList<>();

			while (results.next()) {

				rows.add(new Object[] {
						results.getInt("complaint_id"),
						results.getString("assigned_to"),
						results.getString("department"),
						results.getString("status")
				});
			}

			resp.setSuccess(true);
			resp.setMessage("Assignments found");
			resp.setData(rows);

		} catch (Exception e) {

			resp.setSuccess(false);
			resp.setMessage("Failed to get staff assignments");

			logger.error("Could not load the staff assignment list", e);
		}

		return resp;
	}

	/*
	 * Assigning a complaint to a staff member, done as a JDBC UPDATE. Two checks
	 * worth noting: we make sure the employee exists first so a typo'd staff ID
	 * comes back as a message instead of a foreign key error, and afterwards we
	 * check the update actually changed a row, otherwise a wrong complaint ID would
	 * quietly report success having done nothing.
	 */
	public Response assignStaff(Request request) {

		Response resp = new Response();

		String employeeId = String.valueOf(request.getParams().get("employeeId")).trim().toUpperCase();
		String rawComplaintId = String.valueOf(request.getParams().get("complaintId")).trim();

		try (Connection connection = openConnection()) {

			int complaintId = Integer.parseInt(rawComplaintId);

			try (PreparedStatement check = connection
					.prepareStatement("SELECT 1 FROM employee WHERE person_id = ?")) {

				check.setString(1, employeeId);

				try (ResultSet found = check.executeQuery()) {

					if (!found.next()) {
						resp.setSuccess(false);
						resp.setMessage("No employee with ID " + employeeId);
						return resp;
					}
				}
			}

			try (PreparedStatement update = connection.prepareStatement(
					"UPDATE complaint SET assigned_to = ?, status = 'ASSIGNED' WHERE complaint_id = ?")) {

				update.setString(1, employeeId);
				update.setInt(2, complaintId);

				int changed = update.executeUpdate();

				if (changed == 0) {
					resp.setSuccess(false);
					resp.setMessage("No complaint with ID " + complaintId);
					return resp;
				}

				resp.setSuccess(true);
				resp.setMessage("Complaint " + complaintId + " assigned to " + employeeId);

				logger.info("Complaint {} assigned to {}", complaintId, employeeId);
			}

		} catch (NumberFormatException e) {

			resp.setSuccess(false);
			resp.setMessage("Complaint ID must be a number");

			logger.warn("Staff assignment rejected, complaint id was '{}'", rawComplaintId);

		} catch (Exception e) {

			resp.setSuccess(false);
			resp.setMessage("Failed to assign staff");

			logger.error("Could not assign complaint to staff", e);
		}

		return resp;
	}

	/*
	 * The receptionist dashboard numbers the brief asks for: per category, how many
	 * complaints in total, how many resolved, how many still outstanding. One GROUP
	 * BY does the lot. The client used to count these itself, but that only ever
	 * counted the rows it happened to have loaded.
	 */
	public Response getDashboardStats(Request request) {

		Response resp = new Response();

		String sql = "SELECT category, COUNT(*) AS total, "
				+ "SUM(CASE WHEN status = 'RESOLVED' THEN 1 ELSE 0 END) AS resolved, "
				+ "SUM(CASE WHEN status <> 'RESOLVED' THEN 1 ELSE 0 END) AS outstanding "
				+ "FROM complaint GROUP BY category ORDER BY category";

		try (Connection connection = openConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet results = statement.executeQuery()) {

			ArrayList<Object[]> rows = new ArrayList<>();

			while (results.next()) {

				rows.add(new Object[] {
						results.getString("category"),
						results.getInt("total"),
						results.getInt("resolved"),
						results.getInt("outstanding")
				});
			}

			resp.setSuccess(true);
			resp.setMessage("Dashboard stats found");
			resp.setData(rows);

		} catch (Exception e) {

			resp.setSuccess(false);
			resp.setMessage("Failed to get dashboard stats");

			logger.error("Could not load the dashboard stats", e);
		}

		return resp;
	}

	/*
	 * Reads the same config as Hibernate does (hibernate.cfg.xml plus the login
	 * details in hibernate.properties).
	 * we leave the database name in the url, since these queries actually run
	 * against careplus_db rather than the bare server.
	 */
	private Connection openConnection() throws Exception {

		Properties properties = new Configuration().configure().getProperties();

		return DriverManager.getConnection(
				properties.getProperty("hibernate.connection.url"),
				properties.getProperty("hibernate.connection.username"),
				properties.getProperty("hibernate.connection.password"));
	}
}
