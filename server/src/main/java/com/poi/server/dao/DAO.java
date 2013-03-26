package com.poi.server.dao;

import com.google.appengine.api.datastore.Key;

public interface DAO<T> {
	
	void persist(T entity);

	void remove(T entity);

	T findById(Key id);
}
