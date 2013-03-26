package com.poi.dao;

import android.content.ContentValues;
import android.content.Context;

public class PoiDao extends Dao {
	
	public static final String TABLE = "poi";
	public static final String KEY_ID = "_id";
	public static final String KEY_ID_CATEGORIA = "id_categoria";
	public static final String KEY_TITULO = "titulo";
	public static final String KEY_DESCRICAO = "descricao";
	public static final String KEY_DETALHES = "detalhes";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_CRIACAO = "criacao";
	
	public static final int VALUE_ID_CATEGORIA_BAR = 1;
	public static final int VALUE_ID_CATEGORIA_BOATE = 2;
	public static final int VALUE_ID_CATEGORIA_SHOW  = 3;
	public static final int VALUE_ID_CATEGORIA_RESTAURANTE = 4;
	
	private static final String[] COLUMNS = {
		KEY_ID, KEY_ID_CATEGORIA, KEY_TITULO, KEY_DESCRICAO, KEY_DETALHES, KEY_LATITUDE, KEY_LONGITUDE, KEY_CRIACAO};
	
    public static final String CREATE = "create table " + TABLE + " ("
			+ KEY_ID + " integer primary key autoincrement, "
			+ KEY_ID_CATEGORIA + " integer, "
			+ KEY_TITULO + " text not null,"
			+ KEY_DESCRICAO + " text not null,"
    		+ KEY_DETALHES + " text not null,"
    		+ KEY_LATITUDE + " integer, "
    		+ KEY_LONGITUDE + " integer, "
    		+ KEY_CRIACAO + " date default CURRENT_TIMESTAMP);";

	public PoiDao(Context context) {
		super(PoiDBHelper.getInstance(context), TABLE, KEY_ID, COLUMNS);
	}

    public boolean update(long rowId, String titulo, String descricao, String detalhes, double latitude, double longitude) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITULO, titulo);
        args.put(KEY_DESCRICAO, descricao);
        args.put(KEY_DETALHES, detalhes);
        args.put(KEY_LATITUDE, latitude);
        args.put(KEY_LONGITUDE, longitude);
        return super.update(rowId, args);
    }

    public long insert(String titulo, String descricao, String detalhes, double latitude, double longitude) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITULO, titulo);
        initialValues.put(KEY_DESCRICAO, descricao);
        initialValues.put(KEY_DETALHES, detalhes);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);
        return super.insert(initialValues);
    }

}
