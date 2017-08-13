package br.org.knob.android.framework.service;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import br.org.knob.android.framework.R;
import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.settings.KafSettings;
import br.org.knob.android.framework.util.Util;

public class MapService extends GenericService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MapFragment";

    private Context context;

    private static GoogleApiClient mGoogleApiClient;
    private GoogleMap map;

    private LocationService locationService;

    public MapService(Context context) {
        this.context = context;

        // To retrieve and save our locations
        locationService = new LocationService(context);

        // Google Maps API
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Util.log(TAG, "Connected to Google Play Services");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Util.log(TAG, "Connection suspended: " + cause);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Util.log(TAG, "Connection failed: " + connectionResult.toString());
    }

    public boolean isMapReady() {
        return map != null;
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    public Location getLastKnowLocation() {
        Location location = null;

        if (mGoogleApiClient.isConnected()) {
            android.location.Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (lastKnownLocation != null) {
                Util.log(TAG, "Last know location: " + lastKnownLocation.toString());

                location = new Location(
                        new Date(),
                        String.valueOf(lastKnownLocation.getLatitude()),
                        String.valueOf(lastKnownLocation.getLongitude()),
                        null,
                        null);

                MapSnapshotService mapSnapshotService = new MapSnapshotService(context, map, location);
                mapSnapshotService.takeSnapshot(true);
            }
        }

        if (location == null) {
            // Couldn't get last know location from GPS, will try to get it from saved locations
            location = locationService.findLatest();
        }

        return location;
    }

    public void setLastKnownLocation() {
        // Last know location
        Location lastKnownLocation = getLastKnowLocation();

        // Save location
        locationService.save(lastKnownLocation);

        // Send map to that location
        setMapLocation(lastKnownLocation, true);
    }

    public void setMapLocation(Location location, boolean animateCamera) {
        if (location != null) {
            android.location.Location androidLocation = location.getAndroidLocation();

            if (map != null && androidLocation != null) {
                LatLng latLng = new LatLng(androidLocation.getLatitude(), androidLocation.getLongitude());

                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                if (animateCamera) {
                    map.animateCamera(update);
                } else {
                    map.moveCamera(update);
                }

                Util.log(TAG, "Set map location: " + androidLocation.toString());

                CircleOptions circle = new CircleOptions().center(latLng);
                circle.fillColor(Color.MAGENTA);
                circle.radius(25);
                map.clear();
                map.addCircle(circle);
            }
        }
    }
}
