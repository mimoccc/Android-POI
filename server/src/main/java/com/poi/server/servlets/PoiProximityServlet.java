package com.poi.server.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.poi.dto.PoiDTO;
import com.poi.dto.ProximityRequest;
import com.poi.dto.ProximityResponse;
import com.poi.server.dao.PoiDAO;
import com.poi.server.model.Poi;

@SuppressWarnings("serial")
public class PoiProximityServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		ProximityRequest proximityRequest = new ProximityRequest();
		ProtostuffIOUtil.mergeFrom(request.getInputStream(), proximityRequest, ProximityRequest.getSchema());

		ProximityResponse proximityResponse = new ProximityResponse();
		
		PoiDAO dao = new PoiDAO();
		List<Poi> pois = dao.proximitySearch(proximityRequest.getLatitude(), proximityRequest.getLongitude());
		PoiDTO poiDTO;
		for (Poi poi : pois) {
			poiDTO = new PoiDTO();
			poiDTO.setId(poi.getKey().getId());
			poiDTO.setLatitude(poi.getLatitude());
			poiDTO.setLongitude(poi.getLongitude());
			poiDTO.setTitulo(poi.getTitulo());
			poiDTO.setDescricao(poi.getDescricao());
			poiDTO.setDetalhes(poi.getDetalhes());
			proximityResponse.addPoiDTO(poiDTO);
		}
		
		ProtostuffIOUtil.writeTo(response.getOutputStream(), proximityResponse, ProximityResponse.getSchema(), LinkedBuffer.allocate(1024));
	}
	
}
