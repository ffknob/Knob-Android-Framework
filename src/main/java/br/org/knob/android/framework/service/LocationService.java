package br.org.knob.android.framework.service;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import br.org.knob.android.framework.dao.LocationDAO;
import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.settings.KafSettings;

public class LocationService extends GenericService {
    public static final String TAG = "LocationService";

    private Context context;
    private List<Location> locations;
    private LocationDAO locationDAO;
    private String databaseName;
    private Integer databaseVersion;

    // Use framework's database
    public LocationService(Context context) {
        this.context = context;
        this.databaseName = KafSettings.DATABASE_NAME;
        this.databaseVersion = KafSettings.DATABASE_VERSION;

        locationDAO = new LocationDAO(context, databaseName, databaseVersion);
    }

    // Use own database
    public LocationService(Context context, String databaseName, Integer databaseVersion) {
        this.context = context;
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;

        locationDAO = new LocationDAO(context, databaseName, databaseVersion);
    }

    public long count() {
        return locationDAO.count();
    }

    public boolean isPersisted(Location location) { return locationDAO.isPersisted(location); }

    public void save(Location location) {
        try {
            locationDAO.begin();
            locationDAO.save(location);
            locationDAO.commit();
        } catch(Exception e) {
            locationDAO.rollback();
        }
    }

    public List<Location> findAllOrderedByDate() {
        // TODO: Limit somehow... we don't want to retrieve all saved locations
        locations = locationDAO.findAll();

        if(locations != null) {
            Collections.sort(locations, new Location.DateDescComparator());
        }

        return locations;
    }

    public Location findLatest() {
        return locationDAO.findLast(locationDAO.getIdColumnName());
    }

    public Location findById(Long id) {
        return locationDAO.findById(id);
    }
}
