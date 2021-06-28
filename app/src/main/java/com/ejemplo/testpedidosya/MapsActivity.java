package com.ejemplo.testpedidosya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ejemplo.testpedidosya.Pojo.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MyActivity";
    private GoogleMap mMap;

    private int option = 0;

    private CameraPosition mCameraPosition;
    LatLng location;
    private static final int DEFAULT_ZOOM = 12;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);

    private ArrayList<Restaurant> list_r = new ArrayList<>();
    private Marker r_marker;
    private ArrayList<Marker> list_markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("Bundle");
        if (args != null){
            list_r = (ArrayList<Restaurant>) args.getSerializable("Arraylist_r");
            option = 1;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        createMarkers();

        if (option == 0){
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    confirmDialog(latLng);
                }
            });
        }
    }

    private void confirmDialog(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage(R.string.dialog_title_confirmation)
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.dialog_yes,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                        intent.putExtra("Latlng",latLng);
                        startActivity(intent);
                    }
                })
                .show();
    }

    public void createMarkers(){

        Log.i(TAG, "create marker");

        if (!list_r.isEmpty()){
            for (int i = 0; i < list_r.size(); i++){

                String name = list_r.get(i).getName();
                double latitude = list_r.get(i).getLatitude();
                double longitud = list_r.get(i).getLongitud();

                location = new LatLng(latitude,longitud);

                r_marker = mMap.addMarker(new MarkerOptions()
                        .title(name)
                        .position(location)
                        //.flat(true)
                        .alpha(0.8f));

                list_markers.add(r_marker);

            }
            updateLocation();
        }
    }

    public void updateLocation(){
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (location != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.latitude,
                            location.longitude), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
}
