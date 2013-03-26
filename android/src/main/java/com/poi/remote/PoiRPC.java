package com.poi.remote;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.poi.dto.PoiDTO;
import com.poi.dto.ProximityRequest;
import com.poi.dto.ProximityResponse;
import com.poi.util.Util;

public class PoiRPC {
	
    private static final String TAG = "PoiRPC";

	public static List<PoiDTO> requestPois(Context context) {
		try {
			long start = System.currentTimeMillis();

			ProximityRequest proximityRequest = new ProximityRequest(-19.932200, -43.938000);

			byte[] data = ProtostuffIOUtil.toByteArray(proximityRequest,
					ProximityRequest.getSchema(),
					LinkedBuffer.allocate(1024));

			RestClient client = new RestClient("http://10.0.2.2:8888/poiproximity");
			//RestClient client = new RestClient("http://test.localizerserver.appspot.com/poiproximity");
			client.setBinaryData(data);
			InputStream inputStream = client.execute(RequestMethod.POST);

			ProximityResponse proximityResponse = new ProximityResponse();
			ProtostuffIOUtil.mergeFrom(inputStream, proximityResponse, ProximityResponse.getSchema());

			long end = System.currentTimeMillis();

			String tempo = "Tempo processo: " + (end - start) + " ms.";
			Toast.makeText(context, tempo, Toast.LENGTH_SHORT).show();
			
			Util.log(TAG, tempo);

			int qtde = proximityResponse.getPoiDTOCount();

			Util.log(TAG, "Qtde pontos encontrados: " + qtde);

			/*
			PoiDao dao = new PoiDao(context);
			dao.deleteAll();
			for (PoiDTO poiDTO : proximityResponse.getPoiDTOList()) {
				dao.insert(poiDTO.getTitulo(),
						poiDTO.getDescricao(),
						poiDTO.getDetalhes(),
						poiDTO.getLatitude(),
						poiDTO.getLongitude());
			}
			*/
			
			return proximityResponse.getPoiDTOList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<PoiDTO>();
    }

}
