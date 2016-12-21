package br.org.knob.android.framework.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHelper";

    public String databaseName;
    public Integer databaseVersion;

    public DatabaseHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);

        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Integer getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(Integer databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

}
