package com.careplus.server.dao;

import java.util.List;

import com.careplus.common.model.Complaint;

public interface ComplaintDAO extends GenericDAO<Complaint> {
	
	List<Complaint> findByCategory(String category);

}
