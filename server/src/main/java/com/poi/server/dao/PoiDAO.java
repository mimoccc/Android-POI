package com.poi.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.poi.server.model.Poi;

public class PoiDAO extends DAOImpl<Poi> {

	private static final int BOUND = 10000;
	
	public List<Poi> proximitySearch(double lat, double lon) {

		EntityManager em = EMF.get().createEntityManager();
		
		Point center = new Point(lat, lon);

		//List<Object> params = new ArrayList<Object>();
		//params.add("John");
		// GeocellQuery baseQuery = new
		// GeocellQuery("lastName == lastNameParam", "String lastNameParam",
		// params);
		GeocellQuery baseQuery = new GeocellQuery("select o from " + Poi.class.getName() + " o");

		List<Poi> objects = null;
		int maxResults = 40;
		int maxDistance = 1000;
		
		long start = System.currentTimeMillis();
		objects = GeocellManager.proximitySearch(center, maxResults, maxDistance, Poi.class, baseQuery, em);
		long end = System.currentTimeMillis();
		
		System.out.println("Tempo proximitySearch: " + (end - start) + " ms.");
		System.out.println("Qtde pontos encontrados: " + objects.size());

		return objects;
	}
	
	@SuppressWarnings("unchecked")
	public List<Poi> findTicketsAtTheBounds(int lat, int lng) {
		
		String sql = 
			"select from " + Poi.class.getName() +
			" where latitude  > " + (lat - BOUND) +
			"   and latitude  < " + (lat + BOUND) +
			"   and longitude > " + (lat - BOUND) +
			"   and longitude < " + (lat + BOUND);
		
		EntityManager em = EMF.get().createEntityManager();
		Query q = em.createQuery(sql);
		
		return q.getResultList();
	}
}