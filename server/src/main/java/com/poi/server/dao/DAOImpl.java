package com.poi.server.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.appengine.api.datastore.Key;
import com.poi.server.model.Poi;

public abstract class DAOImpl<T> implements DAO<T> {
	
	protected Class<T> entityClass;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DAOImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class) genericSuperclass.getActualTypeArguments()[0];
	}

	public void persist(T entity) {
		EntityManager em = EMF.get().createEntityManager();
		em.persist(entity);
		em.close();
	}

	public void remove(T entity) {
		EntityManager em = EMF.get().createEntityManager();
		em.remove(entity);
		em.close();
	}

	public T findById(Key id) {
		EntityManager em = EMF.get().createEntityManager();
		return (T) em.find(entityClass, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		EntityManager em = EMF.get().createEntityManager();
		Query q = em.createQuery("select from " + Poi.class.getName());
		return q.getResultList();
	}
}
