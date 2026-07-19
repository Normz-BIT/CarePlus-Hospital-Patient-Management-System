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

/*
 * Employee Complaint Controller
 * The receptionist's complaint queue: triage, assign, respond and report
 *
 * Unlike the other controllers, this one keeps its own copy of the loaded
 * complaints so it can filter and summarise without going back to the server.
 * That makes category switching instant, at the cost of the counts reflecting the
 * last refresh rather than live data.
 *
 * GET_ALL_COMPLAINTS, ASSIGN_STAFF and RESPOND_TO_COMPLAINT are all unrouted on
 * the server, so the queue is currently empty and the summary reads zero.
 */
public class EmployeeComplaintController {

	/*
	 * Named constants because the cached rows are untyped Object arrays, so these
	 * positions are the only link between how refresh builds a row and how the
	 * filter and summary read it. Changing the column order means changing these
	 * two values, and nothing will fail to compile if that is forgotten.
	 */
	// Column indices in the complaint table (see EmployeeComplaint columns).
	private static final int CATEGORY_COL = 2;
	private static final int STATUS_COL = 7;

	private final EmployeeComplaintView view;
	/*
	 * The unfiltered result of the last refresh, held separately from the table
	 * model because the model only ever contains the currently visible subset.
	 * Filtering therefore rebuilds the table from this list rather than discarding
	 * rows it would need again.
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

			/*
			 * The receptionist picks any status from the full enum, so nothing prevents an
			 * illegal jump such as SUBMITTED straight to RESOLVED, or reopening a case that
			 * was never resolved. Enforcing the legal transitions belongs in
			 * ComplaintService, since only the server can see the complaint's current
			 * state.
			 */
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
					row.getDateSubmitted(),
					row.getResponse(),
					row.getResponseDate(),
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
	 * Rebuilds the category filter from the loaded complaints (keeping the
	 * selection).
	 *
	 * Options come from the data actually present rather than from the
	 * ComplaintCategory enum, so the receptionist is never offered a category with
	 * no complaints behind it. The side effect is that a category disappears from
	 * the filter once its last complaint is resolved away.
	 */
	private void populateFilter() {
		/*
		 * Captured before the combo is emptied, because removeAllItems clears the
		 * selection. Without this the receptionist's chosen filter would reset to "All"
		 * on every refresh, including after each assign or resolve.
		 */
		Object selected = view.getCboFilter().getSelectedItem();
		/*
		 * LinkedHashSet gives both properties needed here: it drops duplicate categories
		 * and preserves insertion order, so "All" stays pinned first and the rest follow
		 * the order they appear in the data rather than an arbitrary hash order.
		 */
		LinkedHashSet<String> categories = new LinkedHashSet<>();
		categories.add("All");
		for (Object[] row : allComplaints)
			categories.add(categoryOf(row));
		view.getCboFilter().removeAllItems();
		for (String category : categories)
			view.getCboFilter().addItem(category);
		/*
		 * Silently no ops if the previously selected category no longer exists in the
		 * data, leaving the combo on its first entry, which is "All".
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
	 * Counted from the cached list rather than queried, so these totals describe
	 * whatever the last refresh returned and not the database as it stands now.
	 * ComplaintService.dashboardStats exists to move this aggregation server side,
	 * which would also let it see complaints this client never received.
	 */
	private void updateSummary() {
		int total = allComplaints.size();
		int resolved = 0;
		/*
		 * LinkedHashMap so the category breakdown reads in a stable order rather than
		 * reshuffling between refreshes.
		 */
		Map<String, Integer> byCategory = new LinkedHashMap<>();
		for (Object[] row : allComplaints) {
			if (isResolved(statusOf(row)))
				resolved++;
			/*
			 * merge handles both the first sighting of a category and every subsequent one
			 * in a single call, avoiding a separate containsKey check.
			 */
			byCategory.merge(categoryOf(row), 1, Integer::sum);
		}
		/*
		 * Outstanding is derived rather than counted separately, which guarantees the
		 * two figures always sum to the total. Note this means REOPENED counts as
		 * outstanding, which is the intended reading: a reopened complaint is
		 * unfinished work.
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
	 * Defensive on all three counts, returning an empty String rather than throwing
	 * for a null row, a short row or a null cell. That matters because the summary
	 * runs over every loaded complaint: one malformed row would otherwise abort the
	 * whole count and blank the dashboard.
	 */
	private String cell(Object[] row, int index) {
		return (row != null && row.length > index && row[index] != null) ? String.valueOf(row[index]) : "";
	}

	/*
	 * Compares against text rather than the ComplaintStatus enum because the cached
	 * rows hold whatever the cell renders as. equalsIgnoreCase is what makes this
	 * match the enum's uppercase RESOLVED as well as any prettier casing.
	 *
	 * "Closed" is not a ComplaintStatus value, so that half of the test never
	 * matches anything today. It is harmless, but it should either be removed or
	 * promoted to a real status rather than left as an implied one.
	 */
	private boolean isResolved(String status) {
		return status.equalsIgnoreCase("Resolved") || status.equalsIgnoreCase("Closed");
	}
}
