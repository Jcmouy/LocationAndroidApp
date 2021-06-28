package com.ejemplo.testpedidosya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ejemplo.testpedidosya.Adapter.CustomAdapter;
import com.ejemplo.testpedidosya.Data.Remote.RemoteUtils;
import com.ejemplo.testpedidosya.Pojo.Restaurant;
import com.ejemplo.testpedidosya.Remote.AppTokenService;
import com.ejemplo.testpedidosya.Remote.RestaurantService;
import com.ejemplo.testpedidosya.Util.Constant;
import com.ejemplo.testpedidosya.Util.GpsLoc;
import com.ejemplo.testpedidosya.Util.Paginator;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MyActivity";

    private static final String clientId = "trivia_f";
    private static final String clientSecret = "PeY@@Tr1v1@943";

    ListView lv;
    Button nextBtn, prevBtn;
    ImageButton mapsBtn, ubicationBtn;
    Paginator p = new Paginator();
    private int totalPages = 0;
    private int currentPage = 0;
    CustomAdapter adapter;

    private String latitude, longitude;

    private String token;
    public static List<Restaurant> list_restaurants = new ArrayList<>();

    private AppTokenService mAppTokenService;
    private RestaurantService mRestaurantService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppTokenService = RemoteUtils.getAppTokenService();
        mRestaurantService = RemoteUtils.getRestaurantService();

        //Iniatilze view
        lv = (ListView) findViewById(R.id.listview);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        prevBtn = (Button) findViewById(R.id.prevBtn);
        mapsBtn = (ImageButton) findViewById(R.id.show_map);
        ubicationBtn = (ImageButton) findViewById(R.id.select_ubication);
        prevBtn.setEnabled(false);
        nextBtn.setEnabled(false);


        Intent intent = getIntent();
        if (intent.hasExtra("Latlng")){
            //Get new latlng values
            LatLng newLatLng = getIntent().getExtras().getParcelable("Latlng");
            latitude = String.valueOf(newLatLng.latitude);
            longitude = String.valueOf(newLatLng.longitude);

            list_restaurants = new ArrayList<>();
        }else{
            GpsLoc gpsLoc = new GpsLoc();
            gpsLoc.getDeviceLocation(MainActivity.this);
            latitude = gpsLoc.getLatitude();
            longitude = gpsLoc.getLongitude();
        }

        getToken(clientId, clientSecret);

        //NAVIGATE TO NEXT PAGE.
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage += 1;
                bindData(currentPage);
                toggleButtons();
            }
        });

        //NAVIGATE TO PREVIOUS PAGE
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage -= 1;
                bindData(currentPage);
                toggleButtons();
            }
        });

        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!list_restaurants.isEmpty()){
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("Arraylist_r", (Serializable) list_restaurants);
                    intent.putExtra("Bundle",args);
                    startActivity(intent);
                }else{
                    openDialog();
                }
            }
        });

        ubicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        Log.i(TAG, "Got token successfully");

    }

    /*
   TOGGLE NEXT AND PREVIOUS BUTTONS DEPENDING ON CURRENT PAGE
    */
    private void toggleButtons() {
        //SINGLE PAGE DATA
        if (totalPages <= 1) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(false);
        }
        //LAST PAGE
        else if (currentPage == totalPages) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        }
        //FIRST PAGE
        else if (currentPage == 0) {
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        }
        //SOMEWHERE IN BETWEEN
        else if (currentPage >= 1 && currentPage <= totalPages) {
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }
    }


    private void getToken(String clientId, String clientSecret) {
        mAppTokenService.getToken(clientId, clientSecret).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    JsonPrimitive json = response.body().getAsJsonPrimitive("access_token");

                    token = json.getAsString();

                    getRestaurants(token);

                    Log.i(TAG, "Got token successfully");
                } else{
                    // error
                    if (response.code() >= 500 & response.code() <= 599 ){
                        Toast.makeText(MainActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        switch (response.code()) {
                            case 401:
                            case 403:
                                Toast.makeText(MainActivity.this, R.string.user_n_permission, Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(MainActivity.this, R.string.user_acces_service, Toast.LENGTH_SHORT).show();
                                break;
                            case 408:
                                Toast.makeText(MainActivity.this, R.string.user_time_out, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(MainActivity.this, R.string.error_default, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network failure, if persist contact us", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRestaurants(String token) {

        mRestaurantService.getRestaurantsBySearch(latitude+","+longitude,"1", Constant.API_METHOD_NAME_FIELD+","+Constant.API_METHOD_COORDINATES_FIELD,token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonArray json = response.body().getAsJsonArray("data");

                    JsonObject list = new JsonObject();

                    if (json.size() == 0){
                        Toast.makeText(MainActivity.this, R.string.result_n, Toast.LENGTH_SHORT).show();
                    }else {
                        for (int i = 0; i < json.size(); i++){

                            list = (JsonObject) json.get(i);

                            //Get name & coordinates of restaurant
                            String name = list.get(Constant.API_METHOD_NAME_FIELD).getAsString();
                            String coordinates = list.get(Constant.API_METHOD_COORDINATES_FIELD).getAsString();

                            //Split coordinate in latitude & longitud
                            String[] coo = coordinates.split(",");
                            double latitude = Double.parseDouble(coo[0]);
                            double longitud = Double.parseDouble(coo[1]);

                            Restaurant res = new Restaurant(name,latitude,longitud);
                            list_restaurants.add(res);

                            if (list_restaurants.size() > Paginator.ITEMS_PER_PAGE){
                                nextBtn.setEnabled(true);
                            }

                        }

                    }
                    bindData(currentPage);

                    Log.i(TAG, "Got token successfully");

                } else {
                    // error
                    if (response.code() >= 500 & response.code() <= 599 ){
                        Toast.makeText(MainActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        switch (response.code()) {
                            case 401:
                            case 403:
                                Toast.makeText(MainActivity.this, R.string.user_n_permission, Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(MainActivity.this, R.string.user_acces_service, Toast.LENGTH_SHORT).show();
                                break;
                            case 408:
                                Toast.makeText(MainActivity.this, R.string.user_time_out, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(MainActivity.this, R.string.error_default, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network failure, if persist contact us", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void bindData(int page) {
        totalPages = p.getTotalPages();
        adapter=new CustomAdapter(this,p.generatePage(page));
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage(R.string.dialog_maps_availability)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
