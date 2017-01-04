package br.org.knob.android.framework.model;

import android.content.ContentValues;

public interface GenericModel {
    public static final String TAG = "GenericModel";

    public Long getId();
    public void setId(Long id);
    public ContentValues getValues();
    public void setValues(ContentValues values);
}
