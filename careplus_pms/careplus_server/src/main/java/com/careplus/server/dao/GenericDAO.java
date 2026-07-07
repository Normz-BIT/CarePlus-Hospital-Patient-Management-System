package com.careplus.server.dao;

import java.util.List;

public interface GenericDAO<T> {
	
	void save(T entity);
	
	T findById(int id);
	
	List<T> findAll();
	
	void update(T entity);
	
	void delete (T entity);
	

}
