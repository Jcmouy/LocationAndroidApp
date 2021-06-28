package com.ejemplo.testpedidosya.Remote;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by JC on 6/12/2017.
 */

public interface AppTokenService {
    /*@Headers("Content-Type: application/json")*/

    @GET("tokens")
    Call<JsonObject> getToken(@Query("clientId") String clientId,
                              @Query("clientSecret") String clientSecret);

}
