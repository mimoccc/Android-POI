package com.poi.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.fedorvlasov.lazylist.LazyAdapter;
import com.github.droidfu.adapters.WebGalleryAdapter;
import com.poi.R;
import com.poi.dao.PoiDao;
import com.poi.dto.PoiDTO;
import com.poi.remote.PoiRPC;

public class PoiList extends Activity {

	private static final String TAG = "TicketList";

	private PoiDao dao;
	private ListView listView;
	private LazyAdapter adapter;
	private LinkedList<String> listGaleria;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.view_poi_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

		setAdapterGallery();
		setAdapterList();
	}

	private LinkedList<String> loadImagesGallery() {
		listGaleria = new LinkedList<String>();
		listGaleria.add("http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/egol5.jpg");
		listGaleria.add("http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/devassa-1.jpg");
		/*
		list.add("http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/2_Pinguim_2.jpg");
		list.add("http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/beer648x200.jpg");
		list.add("http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/2_deus-1.jpg");
		list.add("http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/aconchego_da_floresta.jpg");
		*/
		return listGaleria;
	}

	private void setAdapterGallery() {
		Gallery g = (Gallery) findViewById(R.id.destaques);

		WebGalleryAdapter webAdapter = new WebGalleryAdapter(this, loadImagesGallery());
		// webAdapter.setImageUrls(loadImages());
		g.setAdapter(webAdapter);

		// g.setAdapter(new ImageAdapter(this));
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Toast.makeText(PoiList.this, "" + position, Toast.LENGTH_SHORT).show();
			}
		});
		registerForContextMenu(g);
	}
	
	private void setAdapterList() {
		List<PoiDTO> listPoiDTO = PoiRPC.requestPois(this);
		
		listView = (ListView)findViewById(R.id.list);
		
		String[] mStrings = {
	    		"http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/egol5.jpg",
	    		"http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/devassa-1.jpg",
	    		"http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/2_Pinguim_2.jpg",
	    		"http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/beer648x200.jpg",
	    		"http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/2_deus-1.jpg",
	    		"http://www.soubh.com.br/plus/plusimage/estabelecimento/estab_destaque/aconchego_da_floresta.jpg",
	            "http://a3.twimg.com/profile_images/670625317/aam-logo-v3-twitter.png",
	            "http://a3.twimg.com/profile_images/740897825/AndroidCast-350_normal.png",
	            "http://a3.twimg.com/profile_images/121630227/Droid_normal.jpg",
	            "http://a1.twimg.com/profile_images/957149154/twitterhalf_normal.jpg",
	            "http://a1.twimg.com/profile_images/97470808/icon_normal.png",
	            "http://a1.twimg.com/profile_images/349012784/android_logo_small.jpg",
	            "http://a1.twimg.com/profile_images/841338368/ea-twitter-icon_normal.png",
	            "http://a3.twimg.com/profile_images/64827025/android-wallpaper6_2560x160_normal.png",
	            "http://a3.twimg.com/profile_images/77641093/AndroidPlanet_normal.png"
	    };
		
		adapter = new LazyAdapter(this, listPoiDTO);
		listView.setAdapter(adapter);
	}
	
	private void setAdapterList2() {
		dao = new PoiDao(this);

		int categoria = getIntent().getExtras().getInt(PoiDao.KEY_ID_CATEGORIA);

		//Cursor cursor = dao.buscar(PoiDao.KEY_ID_CATEGORIA + "=" + categoria, null, null, null, PoiDao.KEY_CRIACAO + " DESC");
		Cursor cursor = dao.buscarTodos();

		startManagingCursor(cursor);

		String[] from = new String[] { PoiDao.KEY_TITULO, PoiDao.KEY_DESCRICAO, PoiDao.KEY_DETALHES,
				PoiDao.KEY_LATITUDE, PoiDao.KEY_LONGITUDE };
		int[] to = new int[] { R.id.txtTitulo, R.id.txtDescricao, R.id.txtDetalhes, R.id.txtLatitude, R.id.txtLongitude };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.view_poi_list_item, cursor, from, to);

		//this.setListAdapter(adapter);
	}

	//@Override
	//protected void onListItemClick(ListView l, View v, int position, long id) {
		//SQLiteCursor cursor = (SQLiteCursor) l.getItemAtPosition(position);
		//Long idTicket = cursor.getLong(cursor.getColumnIndex(PoiDao.KEY_ID));
		//System.out.println(idTicket);
	//}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuAtualizarTickets:
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		//dao.close();
        adapter.imageLoader.stopThread();
        listView.setAdapter(null);
		super.onDestroy();
	}

}
