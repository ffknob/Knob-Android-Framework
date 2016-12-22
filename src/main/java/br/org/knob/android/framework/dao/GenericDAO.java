package br.org.knob.android.framework.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.org.knob.android.framework.database.DatabaseHelper;
import br.org.knob.android.framework.model.GenericModel;
import br.org.knob.android.framework.util.Util;


public abstract class GenericDAO<T extends GenericModel> implements Serializable {
    private static final String TAG = "GenericDAO";

    protected DatabaseHelper dbHelper;
    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public abstract String getTableName();


    public List<T> toList(Cursor cursor) {
        List<T> list = new ArrayList<T>();

        if(cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                for(int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);

                    switch(cursor.getType(i)) {
                        case Cursor.FIELD_TYPE_NULL:
                            values.putNull(columnName);
                            break;

                        case Cursor.FIELD_TYPE_INTEGER:
                            values.put(columnName, cursor.getLong(i));
                            break;

                        case Cursor.FIELD_TYPE_FLOAT:
                            values.put(columnName, cursor.getFloat(i));
                            break;

                        case Cursor.FIELD_TYPE_STRING:
                            values.put(columnName, cursor.getString(i));
                            break;

                        case Cursor.FIELD_TYPE_BLOB:
                            // TODO: put BLOB
                            break;
                    }
                }

                T entity = null;
                try {
                    entity = entityClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                entity.setValues(values);
                list.add(entity);
            } while(cursor.moveToNext());
        }

        return list;
    };

    public long save(T entity) {
        Long id = entity.getId();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            if (id != null) {
                // Update
                String[] whereArgs = new String[]{String.valueOf(id)};
                int count = db.update(getTableName(), entity.getValues(), "_id=?", whereArgs);

                Util.log(TAG, "Updated " + count + " rows in table " + getTableName());

                return count;
            } else {
                // Insert
                id = db.insert(getTableName(), "", entity.getValues());

                Util.log(TAG, "Inserted row with id=" + id + " in table " + getTableName());

                return id;
            }
        } finally {
            db.close();
        }
    }

    public int delete(T entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};
            int count = db.delete(getTableName(), "_id=?", whereArgs);

            Util.log(TAG, "Deleted " + count + " rows from table " + getTableName());

            return count;
        } finally {
            db.close();
        }
    }

    public int truncate() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            int count = db.delete(getTableName(), "1", null);

            Util.log(TAG, "Deleted " + count + " rows from table " + getTableName());

            return count;

        } finally {
            db.close();
        }
    }

    public List<T> findAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<T> lista = new ArrayList<T>();

        try {
            Cursor cursor = db.query(getTableName(), null, null, null, null, null, null, null);

            Util.log(TAG, "Found " + cursor.getCount() + " rows in table " + getTableName());

            return toList(cursor);
        } finally {
            db.close();
        }
    }

    public T findById(Long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.query(getTableName(), null, "_id = " + id, null, null, null, null, null);

            if (cursor.getCount() == 1) {
                List<T> list = toList(cursor);
                T entity = (T) list.get(0);

                Util.log(TAG, "Found row with id=" + id + " in table " + getTableName());

                return entity;
            } else {
                Util.log(TAG, "Found more than one row with id=" + id + " in table " + getTableName());
                throw new SQLiteException();
            }
        } finally {
            db.close();
        }
    }

    public long count() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            long count  = DatabaseUtils.queryNumEntries(db, getTableName());
            Util.log(TAG, "Found " + count + " rows in table " + getTableName());

            return count;
        } finally {
            db.close();
        }
    }
}
