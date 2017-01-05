package br.org.knob.android.framework.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.org.knob.android.framework.database.DatabaseHelper;
import br.org.knob.android.framework.model.GenericModel;
import br.org.knob.android.framework.util.Util;


public abstract class GenericDAO<T extends GenericModel> implements Serializable {
    private static final String TAG = "GenericDAO";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private Context context;
    private String dbName;
    private int dbVersion;

    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass, Context context, String dbName, int dbVersion) {
        this.entityClass = entityClass;

        this.context = context;
        this.dbName = dbName;
        this.dbVersion = dbVersion;

        dbHelper = new DatabaseHelper(context, dbName, dbVersion);
    }

    public abstract Context getContext();
    public abstract String getDbName();
    public abstract int getDbVersion();
    public abstract String getTableName();
    public abstract String getIdColumnName();

    private void dealWithException(Exception e) {
        //catch(SQLiteException e) {
        //    e.printStackTrace();
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        e.printStackTrace();
    }

    public List<T> toList(Cursor cursor) {
        List<T> list = new ArrayList<T>();

        if (cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);

                    switch (cursor.getType(i)) {
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
                            if(!cursor.isNull(i)) {
                                values.put(columnName, cursor.getBlob(i));
                            } else {
                                values.putNull(columnName);
                            }
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
            } while (cursor.moveToNext());
        }

        return list;
    }

    public SQLiteDatabase getDb() {
        if (db == null) {
            try {
                db = dbHelper.getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return db;
    }

    public void begin() {
        db = getDb();

        try {
            db.beginTransaction();
        } catch (Exception e) {
            dealWithException(e);
        }
    }

    public void commit() {
        db = getDb();

        try {
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            dealWithException(e);
        }
    }

    public void rollback() {
        db = getDb();

        try {
            db.endTransaction();
        } catch (Exception e) {
            dealWithException(e);
        }
    }

    public void close() {
        db = getDb();

        try {
            db.close();
        } catch (Exception e) {
            dealWithException(e);
        }
    }

    public boolean isPersisted(T entity) {
        if(entity.getId() != null) {
            return true;
        }

        return false;
    }

    public long save(T entity) {
        Long id = entity.getId();

        db = getDb();

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
                entity.setId(id);
                Util.log(TAG, "Inserted row with id=" + id + " in table " + getTableName());

                return id;
            }
        } catch (Exception e) {
            dealWithException(e);
        }

        return -1;
    }

    public int delete(T entity) {
        db = getDb();

        try {
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};
            int count = db.delete(getTableName(), "_id=?", whereArgs);

            Util.log(TAG, "Deleted " + count + " rows from table " + getTableName());

            return count;
        } catch (Exception e) {
            dealWithException(e);
        }

        return -1;
    }

    public int truncate() {
        db = getDb();

        try {
            int count = db.delete(getTableName(), "1", null);

            Util.log(TAG, "Deleted " + count + " rows from table " + getTableName());

            return count;

        } catch (Exception e) {
            dealWithException(e);
        }

        return -1;
    }

    public List<T> findAll() {
        db = getDb();

        try {
            Cursor cursor = db.query(getTableName(), null, null, null, null, null, null, null);

            Util.log(TAG, "Found " + cursor.getCount() + " rows in table " + getTableName());

            return toList(cursor);
        } catch (Exception e) {
            dealWithException(e);
        }

        return null;
    }

    public T findById(Long id) {
        db = getDb();

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
        } catch (Exception e) {
            dealWithException(e);
        }

        return null;
    }

    public T findFirst(String columnName) {
        String query = "SELECT * FROM " + getTableName() + " ORDER BY " + columnName + " ASC LIMIT 1";

        db = getDb();

        try {
            Cursor cursor = db.rawQuery(query, null);

            List<T> list = toList(cursor);

            T entity = null;
            if(!list.isEmpty()) {
                entity = (T) list.get(0);
                Util.log(TAG, "Row with id=" + entity.getId() + " is the fisrt one in in table " + getTableName() + " when order by " + columnName);
            } else {
                entity = null;
                Util.log(TAG, "Could not find first row of table " + getTableName());
            }

            return entity;

        } catch (Exception e) {
            dealWithException(e);
        }

        return null;
    }

    public T findLast(String columnName) {
        String query = "SELECT * FROM " + getTableName() + " ORDER BY " + columnName + " DESC LIMIT 1";

        db = getDb();

        try {
            Cursor cursor = db.rawQuery(query, null);

            List<T> list = toList(cursor);

            T entity = null;
            if(!list.isEmpty()) {
                entity = (T) list.get(0);
                Util.log(TAG, "Row with id=" + entity.getId() + " is the latest one in in table " + getTableName() + " when order by " + columnName);
            } else {
                entity = null;
                Util.log(TAG, "Could not find last row of table " + getTableName());
            }

            return entity;

        } catch (Exception e) {
            dealWithException(e);
        }

        return null;
    }

    public long count() {
        db = getDb();

        try {
            long count = DatabaseUtils.queryNumEntries(db, getTableName());
            Util.log(TAG, "Found " + count + " rows in table " + getTableName());

            return count;
        } catch (Exception e) {
            dealWithException(e);
        }

        return -1;
    }
}
