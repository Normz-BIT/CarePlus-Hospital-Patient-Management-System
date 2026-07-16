package com.careplus.client.employee.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.careplus.client.employee.view.EmployeeComplaint;
import com.careplus.common.client.net.Client;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;

public class EmployeeComplaintController {

	// Column indices in the complaint table (see EmployeeComplaint columns).
	private static final int CATEGORY_COL = 1;
	private static final int STATUS_COL = 3;

	private final EmployeeComplaint view;
	private final List<Object[]> allComplaints = new ArrayList<>();

	public EmployeeComplaintController(EmployeeComplaint view) {
		this.view = view;
		init();
		loadCombos();
		refresh();
	}

	private void init() {
		view.getBtnAssign().addActionListener(e -> assign());
		view.getBtnResolve().addActionListener(e -> resolve());
		view.getBtnRefresh().addActionListener(e -> refresh());
		view.getBtnClear().addActionListener(e -> view.clearFields());
		view.getBtnSearch().addActionListener(e -> applyFilter());
	}

	private void loadCombos() {
		add(view.getCboCategory(), "Medical", "Billing", "Appointment", "Staff", "Other");
		add(view.getCboPriority(), "Low", "Medium", "High");
		add(view.getCboStatus(), "Pending", "Assigned", "In Progress", "Resolved", "Closed");
	}

	private void add(javax.swing.JComboBox<String> box, String... items) {
		box.removeAllItems();
		for (String item : items)
			box.addItem(item);
	}

	private void assign() {
		Request req = new Request();
		req.setType(RequestType.ASSIGN_STAFF);
		req.putMap("complaintId", view.getTxtComplaintId().getText().trim());
		req.putMap("remarks", view.getTxtRemarks().getText().trim());
		submit(req);
	}

	private void resolve() {
		Request req = new Request();
		req.setType(RequestType.RESPOND_TO_COMPLAINT);
		req.putMap("complaintId", view.getTxtComplaintId().getText().trim());
		req.putMap("status", String.valueOf(view.getCboStatus().getSelectedItem()));
		req.putMap("response", view.getTxtRemarks().getText().trim());
		submit(req);
	}

	/** Sends a change request, reports the result, and reloads the list. */
	private void submit(Request req) {
		Response res = Client.send(req);
		view.showMessage(res == null ? "No response from server." : res.getMessage());
		refresh();
	}

	@SuppressWarnings("unchecked")
	private void refresh() {
		Response res = Client.send(new Request(RequestType.GET_ALL_COMPLAINTS, "all", true));
		if (res == null || !Boolean.TRUE.equals(res.getSuccess()))
			return;
		allComplaints.clear();
		if (res.getData() instanceof List<?>) {
			for (Object row : (List<Object>) res.getData()) {
				if (row instanceof Object[])
					allComplaints.add((Object[]) row);
			}
		}
		populateFilter();
		applyFilter();
		updateSummary();
	}

	/**
	 * Rebuilds the category filter from the loaded complaints (keeping the
	 * selection).
	 */
	private void populateFilter() {
		Object selected = view.getCboFilter().getSelectedItem();
		LinkedHashSet<String> categories = new LinkedHashSet<>();
		categories.add("All");
		for (Object[] row : allComplaints)
			categories.add(categoryOf(row));
		view.getCboFilter().removeAllItems();
		for (String category : categories)
			view.getCboFilter().addItem(category);
		if (selected != null)
			view.getCboFilter().setSelectedItem(selected);
	}

	/**
	 * Renders only the complaints matching the selected category ("All" = every
	 * row).
	 */
	private void applyFilter() {
		Object selected = view.getCboFilter().getSelectedItem();
		String filter = selected == null ? "All" : String.valueOf(selected);
		view.clearTable();
		for (Object[] row : allComplaints) {
			if ("All".equals(filter) || filter.equalsIgnoreCase(categoryOf(row))) {
				view.addComplaint(row);
			}
		}
	}

	private void updateSummary() {
		int total = allComplaints.size();
		int resolved = 0;
		Map<String, Integer> byCategory = new LinkedHashMap<>();
		for (Object[] row : allComplaints) {
			if (isResolved(statusOf(row)))
				resolved++;
			byCategory.merge(categoryOf(row), 1, Integer::sum);
		}
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

	private String cell(Object[] row, int index) {
		return (row != null && row.length > index && row[index] != null) ? String.valueOf(row[index]) : "";
	}

	private boolean isResolved(String status) {
		return status.equalsIgnoreCase("Resolved") || status.equalsIgnoreCase("Closed");
	}
}
