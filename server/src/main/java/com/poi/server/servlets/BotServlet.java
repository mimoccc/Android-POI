package com.poi.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.poi.server.dao.PoiDAO;
import com.poi.server.model.Poi;

public class BotServlet extends HttpServlet {

	private static final long serialVersionUID = -5287141953144622696L;
	
	private static final String SELECTOR_SCRIPT = "script";
	private static final String PATTERN_PLUS_MAP_VIEW = "PlusMapView\\((.*)\\)"; // Ex: PlusMapView("2833",-19.935314,-43.936042,8);
	private static final String SELECTOR_TITULO_TIPO_1 = "div[class=agendaTituloBarTitulo]";
	private static final String SELECTOR_TITULO_TIPO_2 = "div[class=estabelecimentoTitulo] div[class=estabelecimentoTitulo]";
	private static final String SELECTOR_DESCRICAO = "meta[name=description]";
	private static final String SELECTOR_IMAGEM = "link[rel=image_src]";
	private static final String SELECTOR_LOCAL = "div[class=agendaInformacoesAdicionaisItem local] div[class=agendaInformacoesAdicionaisItemValor]";
	private static final String SELECTOR_ENDERECO = "div[class=agendaInformacoesAdicionaisItem endereco] div[class=agendaInformacoesAdicionaisItemValor]";
	private static final String SELECTOR_CIDADE = "div[class=agendaInformacoesAdicionaisItem cidade] div[class=agendaInformacoesAdicionaisItemValor]";
	private static final String SELECTOR_VENDA = "div[class=agendaInformacoesAdicionaisItem venda] div[class=agendaInformacoesAdicionaisItemValor]";
	private static final String SELECTOR_PRECO = "div[class=agendaInformacoesAdicionaisItem preco] div[class=agendaInformacoesAdicionaisItemValor]";
	private static final String ATTR_CONTENT = "content";
	private static final String ATTR_HREF = "href";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("url")) {
			
			long start = System.currentTimeMillis();
			
			String url = req.getParameter("url");
			Document doc = Jsoup.connect(url).timeout(20000).get();

			String id = "";
			Double lat = null;
			Double lon = null;
			String zoom = "";
			
			PrintWriter writer = resp.getWriter();
			
			try {
				Elements scripts = doc.select(SELECTOR_SCRIPT);
				Pattern pattern = Pattern.compile(PATTERN_PLUS_MAP_VIEW);
				for (Element element : scripts) {
					Matcher m = pattern.matcher(element.data());
					if (m.find()) {
						String s = m.group(1);
						String[] params = s.split(",");
						if (params.length == 4) {
							id = params[0].replaceAll("\"", "");
							lat = Double.valueOf(params[1]);
							lon = Double.valueOf(params[2]);
							zoom = params[3];
						}
						break;
					}
				}
			} catch (Exception e) {
				resp.getWriter().println("nao foi possivel pegar a lat/long");
				return;
			}
			
			String titulo = doc.select(SELECTOR_TITULO_TIPO_1).text();
			if ("".equals(titulo)) {
				titulo = doc.select(SELECTOR_TITULO_TIPO_2).text();
			}
			
			String descricao = doc.select(SELECTOR_DESCRICAO).attr(ATTR_CONTENT);
			
			writer.println("Url: " + url + "\n");
			writer.println("Página: " + doc.title() + "\n");
			writer.println("Título: " + titulo + "\n");
			writer.println("Descrição: " + descricao + "\n");
			writer.println("Imagem: " + doc.select(SELECTOR_IMAGEM).attr(ATTR_HREF) + "\n");
			writer.println("Local: " + doc.select(SELECTOR_LOCAL).text());
			writer.println("Endereço: " + doc.select(SELECTOR_ENDERECO).text());
			writer.println("Cidade: " + doc.select(SELECTOR_CIDADE).text());
			writer.println("Venda: " + doc.select(SELECTOR_VENDA).text());
			
			Elements elements = doc.select(SELECTOR_PRECO);
			if(elements.size() > 1) {
				writer.println("Local: " + elements.get(0).text());
				writer.println("Contato: " + elements.get(1).text());
			}
			
			writer.println();
			writer.println("id: " + id);
			writer.println("lat: " + lat);
			writer.println("lng: " + lon);
			writer.println("zoom: " + zoom);
			
	        Poi poi = new Poi(1, titulo, descricao.substring(0, 30)+"...", "descricao",  lat, lon, new Date());
			PoiDAO dao = new PoiDAO();
			dao.persist(poi);
			
			long end = System.currentTimeMillis();
			writer.println();
			writer.println("Tempo processamento da página: "+(end-start)+" ms.");
			
			// buscando o endereço apartir da latlong no ws do google maps
			
			/*
			start = System.currentTimeMillis();
			
			String route = "";
			String sublocality = "";
			String locality = "";
			String administrative_area_level_1 = "";
			String country = "";
			
			try {
				URL urlMaps = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=false");
				HttpURLConnection connection = (HttpURLConnection) urlMaps.openConnection();
				connection.connect();
			    String content = CharStreams.toString(new InputStreamReader((InputStream) connection.getContent()));
		    
				JSONObject locationInfo = new JSONObject(content);
				if ("OK".equals(locationInfo.getString("status"))) {
					JSONArray addressComponents = locationInfo.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
					for (int i = 0; i < addressComponents.length(); i++) {
						JSONObject adress = addressComponents.getJSONObject(i);
						if ("route".equals(adress.getJSONArray("types").getString(0))) {
							route = adress.getString("long_name");							// rua
						} else if ("sublocality".equals(adress.getJSONArray("types").getString(0))) {
							sublocality = adress.getString("long_name");					// bairro
						} else if ("locality".equals(adress.getJSONArray("types").getString(0))) {
							locality = adress.getString("long_name");						// cidade
						} else if ("administrative_area_level_1".equals(adress.getJSONArray("types").getString(0))) {
							administrative_area_level_1 = adress.getString("short_name");	// estado
						} else if ("country".equals(adress.getJSONArray("types").getString(0))) {
							country = adress.getString("short_name");						// pais
						}
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			writer.println();
			writer.println("route: " + route);
			writer.println("sublocality: " + sublocality);
			writer.println("locality: " + locality);
			writer.println("administrative_area_level_1: " + administrative_area_level_1);
			writer.println("country: " + country);
		    
			end = System.currentTimeMillis();
			writer.println();
			writer.println("Tempo pegando informações no google maps: "+(end-start)+" ms.");
			*/
			
		} else {
			resp.getWriter().println("entre com o parametro url");
		}
	}

}
