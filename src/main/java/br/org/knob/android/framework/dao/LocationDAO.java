package br.org.knob.android.framework.dao;

import android.content.Context;

import br.org.knob.android.framework.model.Location;

public class LocationDAO extends GenericDAO<Location> {
    public static final String TAG = "LocationDAO";

    private static final String TABLE_NAME = "locations";
    private static final String ID_COLUMN_NAME = "_id";

    private Context context;
    private String databaseName;
    private Integer databaseVersion;

    public LocationDAO(Context context, String databaseName, Integer databaseVersion) {
        super(Location.class, context, databaseName, databaseVersion);

        this.context = context;
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public String getDbName() {
        return databaseName;
    }

    @Override
    public int getDbVersion() {
        return databaseVersion;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnName() { return ID_COLUMN_NAME; }
}
