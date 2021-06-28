package com.ejemplo.testpedidosya.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class GpsLoc extends Activity implements LocationListener{

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private String latitude, longitude;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    protected LocationManager locationManager;

    public void getDeviceLocation(Context context) {

        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(GpsLoc.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        latitude = String.valueOf(mLastKnownLocation.getLatitude());
        longitude = String.valueOf(mLastKnownLocation.getLongitude());

    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location changed","Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
