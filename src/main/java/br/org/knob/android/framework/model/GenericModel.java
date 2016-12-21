package br.gov.rs.tce.inventario.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;

public interface GenericModel {
    public Long getId();
    public ContentValues getValues();
    public void setValues(ContentValues values);
}
