package com.poi.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class Dao {

    private SQLiteOpenHelper mDbHelper;
    protected SQLiteDatabase mDb;
    
    protected String table;
    protected String[] columns;
    protected String keyId;
    
    public Dao(SQLiteOpenHelper mDbHelper, String table, String keyId, String[] columns) {
    	this.mDbHelper = mDbHelper;
        this.table = table;
        this.columns = columns;
        this.keyId = keyId;
        open();
    }

    public Dao open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public boolean delete(long rowId) {
        return mDb.delete(table, keyId + "=" + rowId, null) > 0;
    }
    
    public boolean deleteAll() {
    	return mDb.delete(table, null, null) > 0;
    }
    
    protected long insert(ContentValues initialValues) {
    	return mDb.insert(table, null, initialValues);
    }

    protected boolean update(long rowId, ContentValues args) {
        return mDb.update(table, args, keyId + "=" + rowId, null) > 0;
    }
    
    public Cursor buscarTodos() {
        return mDb.query(table, columns, null, null, null, null, null); 
    }

    public Cursor buscar(long rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, table, columns, keyId + "=" + rowId, null, null, null, null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor buscar(String selection, String selectionArgs[], String groupBy, String having, String orderBy) {
    	return mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy); 
    }

}
