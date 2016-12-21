package br.org.knob.android.framework.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.org.knob.android.framework.model.GenericModel;
import br.org.knob.android.framework.util.Util;


public abstract class GenericDAO<T extends GenericModel> implements Serializable {
    private static final String TAG = "GenericDAO";

    SQLiteOpenHelper dbOpenHelper;
    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public abstract String getTableName();

    public abstract SQLiteOpenHelper getDbOpenHelper();

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
        long id = entity.getId();

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        try {
            if (id != 0) {
                // Update
                String[] whereArgs = new String[]{String.valueOf(id)};
                int count = db.update(getTableName(), entity.getValues(), "id=?", whereArgs);

                Util.log(TAG, "Atualizados " + count + " registros na tabela " + getTableName());

                return count;
            } else {
                // Insert
                id = db.insert(getTableName(), "", entity.getValues());

                Util.log(TAG, "Inserido registro id=" + id + " na tabela " + getTableName());

                return id;
            }
        } finally {
            db.close();
        }
    }

    public int delete(T entity) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        try {
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};
            int count = db.delete(getTableName(), "id=?", whereArgs);

            Util.log(TAG, "Excluídos " + count + " registros da tabela " + getTableName());

            return count;
        } finally {
            db.close();
        }
    }

    public int truncate() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        try {
            int count = db.delete(getTableName(), "1", null);

            Util.log(TAG, "Excluídos " + count + " registros da tabela " + getTableName());

            return count;

        } finally {
            db.close();
        }
    }

    public List<T> findAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        List<T> lista = new ArrayList<T>();

        try {
            Cursor cursor = db.query(getTableName(), null, null, null, null, null, null, null);

            Util.log(TAG, "Encontrados " + cursor.getCount() + " registros na tabela " + getTableName());

            return toList(cursor);
        } finally {
            db.close();
        }
    }

    public T findById(Long id) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        try {
            Cursor cursor = db.query(getTableName(), null, "id = " + id, null, null, null, null, null);

            if (cursor.getCount() == 1) {
                List<T> list = toList(cursor);
                T entity = (T) list.get(0);

                Util.log(TAG, "Encontrado registro com id=" + id + " na tabela " + getTableName());

                return entity;
            } else {
                Util.log(TAG, "Encontrados mais de um registro com id=" + id + " na tabela " + getTableName());
                throw new SQLiteException();
            }
        } finally {
            db.close();
        }
    }
}
