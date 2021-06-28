package com.ejemplo.testpedidosya.Remote;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by JC on 6/12/2017.
 */

public interface RestaurantService {
    //@Headers("Content-Type: application/json")

    @GET("search/restaurants")
    Call<JsonObject> getRestaurantsBySearch(@Query("point") String point,
                                                  @Query("country") String country,
                                                  @Query("fields") String fields,
                                                  @Header("Authorization") String authorization);


}
