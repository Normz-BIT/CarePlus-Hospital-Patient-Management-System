package com.careplus.client.employee.controller;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.client.employee.view.EmployeeComplaintView;
import com.careplus.common.client.net.Client;
import com.careplus.common.client.view.MainDashboard;
import com.careplus.common.enums.ComplaintStatus;
import com.careplus.common.model.Complaint;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.common.util.DateDisplay;

/*
 * Employee Complaint Controller
 * The receptionist's complaint queue: triage, assign, respond and report
 *
 * Unlike the other controllers, this one keeps its own copy of the loaded
 * complaints so it can filter by category without going back to the server.
 * That makes category switching instant. 
 */
public class EmployeeComplaintController {

	/*
	 * Which column is which in the complaint table. They're constants because the
	 * cached rows are plain Object arrays, so these numbers are the only thing
	 * connecting how refresh builds a row to how the filter reads it.
	 */
	private static final int CATEGORY_COL = 2;
	private static final int STATUS_COL = 7;

	private final EmployeeComplaintView view;
	/*
	 * Everything the last refresh returned, kept separate from the table model
	 * because the model only ever holds the rows currently being shown. Filtering
	 * rebuilds the table from this list
	 */
	private final List<Object[]> allComplaints = new ArrayList<>();
	
	private static final Logger logger = LogManager.getLogger(EmployeeComplaintController.class);

	public EmployeeComplaintController(EmployeeComplaintView view) {
		this.view = view;
		loadCombos();
		refresh();
	}


	private void loadCombos() {
		fill(view.getCboStatus(), ComplaintStatus.values());
	}

	private void fill(JComboBox<String> box, Enum<?>[] values) {
		box.removeAllItems();

		for (Enum<?> value : values)
			box.addItem(value.name());
	}

	public void assign() {
		if (view.getTxtComplaintId().getText().trim().isEmpty()) {
			view.showMessage("Complaint ID is required.");
			logger.warn("Complaint assignment attempted without a complaint ID");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.ASSIGN_STAFF);
		req.putMap("complaintId", view.getTxtComplaintId().getText().trim());
		req.putMap("employeeId", MainDashboard.getCurrentUser().getPersonId());
		req.putMap("remarks", view.getTxtRemarks().getText().trim());

		logger.info("Assigning complaint ID: {}", view.getTxtComplaintId().getText().trim());

		submit(req);
	}

	public void resolve() {
		if (view.getTxtComplaintId().getText().trim().isEmpty()) {
			view.showMessage("Complaint ID is required.");
			logger.warn("Complaint response attempted without a complaint ID");

			return;
		}

		Request req = new Request();
		req.setType(RequestType.RESPOND_TO_COMPLAINT);

		Complaint complaint = new Complaint();

		try {

			complaint.setComplaintId(
					Integer.parseInt(view.getTxtComplaintId().getText().trim()));

			complaint.setResponse(
					view.getTxtRemarks().getText().trim());

			complaint.setResponseDate(LocalDateTime.now());

		
			complaint.setStatus(
					ComplaintStatus.valueOf(
							String.valueOf(view.getCboStatus().getSelectedItem())));

			logger.info("Complaint response created: {}", complaint.toString());

			req.putMap("complaint", complaint);
			req.putMap("employeeId", MainDashboard.getCurrentUser().getPersonId());

			submit(req);

		} catch (Exception e) {

			logger.error("An error occurred while responding to complaint", e);
			view.showMessage("Unable to respond to complaint: " + e.getMessage());
		}
	}

	/** Sends a change request, reports the result, and reloads the list. */
	private void submit(Request req) {
		Response res = Client.send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());

		if (res == null) {
			logger.error("No response received from server while updating complaint");
		} else {
			logger.info("Server complaint response: {}", res.getMessage());
		}

		refresh();
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		Response res = Client.send(new Request(RequestType.GET_ALL_COMPLAINTS, "all", true));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess())) {

			logger.warn("Complaint records could not be retrieved");
			return;
		}

		allComplaints.clear();

		for (Complaint row : (List<Complaint>) res.getData()) {

			Object[] viewRow = new Object[] {
					row.getComplaintId(),
					row.getComplaintParentId(),
					row.getCategory(),
					row.getDescription(),
					DateDisplay.withTime(row.getDateSubmitted()),
					row.getResponse(),
					DateDisplay.withTime(row.getResponseDate()),
					row.getStatus()
			};

			allComplaints.add(viewRow);
		}

		/*
		 * Order matters: the filter options are rebuilt from the new data first, then
		 * the table is rendered through that filter, and only then are the totals
		 * computed. Summarising earlier would count against the previous load.
		 */
		populateFilter();
		applyFilter();
		updateSummary();

		logger.info("Complaint records refreshed successfully");
	}

	/**
	 * Rebuilds the category filter from whatever complaints we loaded, keeping
	 * whatever was selected.
	 *
	 * The options come from the complaints we actually have rather than from the
	 * ComplaintCategory enum, so the receptionist never gets offered a category
	 * with nothing behind it. 
	 */
	private void populateFilter() {
		/*
		 * Grab this before emptying the combo, because removeAllItems wipes the
		 * selection. Without it the receptionist's chosen filter would jump back to
		 * "All" on every refresh, including after every assign or resolve.
		 */
		Object selected = view.getCboFilter().getSelectedItem();
		/*
		 * LinkedHashSet does both things we need here: drops duplicate categories and
		 * keeps them in the order we added them, so "All" stays first and the rest
		 * follow the data instead of coming out in some random hash order.
		 */
		LinkedHashSet<String> categories = new LinkedHashSet<>();
		categories.add("All");
		for (Object[] row : allComplaints)
			categories.add(categoryOf(row));
		view.getCboFilter().removeAllItems();
		for (String category : categories)
			view.getCboFilter().addItem(category);
		/*
		 * Puts back whatever they had chosen before the refresh, so the filter doesn't
		 * reset every time the list reloads. If that category has gone the combo just
		 * falls back to its first entry, "All".
		 */
		if (selected != null)
			view.getCboFilter().setSelectedItem(selected);
	}

	/**
	 * Renders only the complaints matching the selected category ("All" = every
	 * row).
	 */
	public void applyFilter() {
		Object selected = view.getCboFilter().getSelectedItem();
		String filter = selected == null ? "All" : String.valueOf(selected);
		view.clearTable();
		for (Object[] row : allComplaints) {
			if ("All".equals(filter) || filter.equalsIgnoreCase(categoryOf(row))) {
				view.addComplaint(row);
			}
		}
	}

	/*
	 * Produces the dashboard figures the receptionist role requires: total requests,
	 * resolved, outstanding, and a breakdown by category.
	 *
	 * The numbers come from the server (ReportService runs one GROUP BY over the
	 * whole complaint table), so they describe the database as it stands rather
	 * than just the rows this screen happens to have loaded. If that request
	 * fails we fall back to counting the cached rows, which is better than a
	 * blank summary.
	 */
	private void updateSummary() {

		Response res = Client.send(new Request(RequestType.GET_DASHBOARD_STATS, "all", true));

		if (res == null || !Boolean.TRUE.equals(res.getSuccess()) || !(res.getData() instanceof List<?>)) {

			logger.warn("Dashboard stats could not be retrieved, counting the loaded rows instead");
			updateSummaryFromCache();
			return;
		}

		int total = 0;
		int resolved = 0;
		int outstanding = 0;

		List<String> categoryCounts = new ArrayList<>();

		// Each row is {category, total, resolved, outstanding}, one per category.
		for (Object row : (List<?>) res.getData()) {

			if (row instanceof Object[] stats && stats.length >= 4) {

				total += (Integer) stats[1];
				resolved += (Integer) stats[2];
				outstanding += (Integer) stats[3];

				categoryCounts.add(stats[0] + " (" + stats[1] + ")");
			}
		}

		view.setSummary("Total: " + total + " | Resolved: " + resolved + " | Outstanding: " + outstanding
				+ " | By category: " + String.join(", ", categoryCounts));
	}

	/*
	 * The old client-side count, kept as the fallback when the server can't give
	 * us the stats. These totals only describe whatever the last refresh
	 * returned, which is exactly why the server version above is preferred.
	 */
	private void updateSummaryFromCache() {
		int total = allComplaints.size();
		int resolved = 0;
		/*
		 * LinkedHashMap so the breakdown comes out in the same order every time instead
		 * of shuffling around between refreshes.
		 */
		Map<String, Integer> byCategory = new LinkedHashMap<>();
		for (Object[] row : allComplaints) {
			if (isResolved(statusOf(row)))
				resolved++;
			/*
			 * merge covers both the first time we see a category and every time after, so
			 * we don't need a separate containsKey check.
			 */
			byCategory.merge(categoryOf(row), 1, Integer::sum);
		}
		/*
		 * We work outstanding out by subtracting rather than counting it separately, so
		 * the two numbers always add up to the total. That does mean REOPENED counts as
		 * outstanding, which is what we want: a reopened complaint is still work to do.
		 */
		int outstanding = total - resolved;

		List<String> categoryCounts = new ArrayList<>();
		byCategory.forEach((category, count) -> categoryCounts.add(category + " (" + count + ")"));

		view.setSummary("Total: " + total + " | Resolved: " + resolved + " | Outstanding: " + outstanding
				+ " | By category: " + String.join(", ", categoryCounts));
	}

	private String categoryOf(Object[] row) {
		return cell(row, CATEGORY_COL);
	}

	private String statusOf(Object[] row) {
		return cell(row, STATUS_COL);
	}

	/*
	 * Guards against all three things that could go wrong (null row, row too short,
	 * null cell)
	 */
	private String cell(Object[] row, int index) {
		return (row != null && row.length > index && row[index] != null) ? String.valueOf(row[index]) : "";
	}

	/*
	 * Compares text rather than the ComplaintStatus enum, because the rows
	 * only hold whatever the cell displayed. equalsIgnoreCase so it matches the
	 * enum's uppercase RESOLVED as well as anything nicer looking.
	 *
	 */
	private boolean isResolved(String status) {
		return status.equalsIgnoreCase("Resolved");
	}
}
