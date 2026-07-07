package com.careplus.server.service;

import java.util.List;
import java.util.Map;

import com.careplus.common.model.Complaint;
import com.careplus.common.net.Response;

public class ComplaintService {

	public Response submit(Complaint complaint) {
		return null;
		
	}
	public List<Complaint> findByCategory(String category) {
		return null;
		
	}
	public Response respond(int id, String text, int staffId) {
		return null;
		
	}
	public Response assignStaff(int id, int staffId) {
        return null;
    }
	public Map<String, Integer> dashboardStats() {
        return null;
    }
}
