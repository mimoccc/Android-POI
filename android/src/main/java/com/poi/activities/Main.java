package com.poi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.poi.R;
import com.poi.dao.PoiDao;

public class Main extends Activity implements OnClickListener {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.view_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		
		((Button) findViewById(R.id.btnBares)).setOnClickListener(this);
		((Button) findViewById(R.id.btnBoates)).setOnClickListener(this);
		((Button) findViewById(R.id.btnShows)).setOnClickListener(this);
		((Button) findViewById(R.id.btnRestaurantes)).setOnClickListener(this);
    }
	
	public void onClick(View v) {
		
		int categoria = 0;
		
		switch (v.getId()) {
			case R.id.btnBares: categoria = PoiDao.VALUE_ID_CATEGORIA_BAR; break;
			case R.id.btnBoates: categoria = PoiDao.VALUE_ID_CATEGORIA_BOATE; break;
			case R.id.btnShows: categoria = PoiDao.VALUE_ID_CATEGORIA_SHOW; break;
			case R.id.btnRestaurantes: categoria = PoiDao.VALUE_ID_CATEGORIA_RESTAURANTE;
			default: throw new RuntimeException();
		}
		
		Intent intent = new Intent(this, PoiList.class);
		intent.putExtra(PoiDao.KEY_ID_CATEGORIA, categoria);
		startActivity(intent);
	}
}