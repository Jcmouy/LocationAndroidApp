package com.ejemplo.testpedidosya.Data.Remote;

import com.ejemplo.testpedidosya.BuildConfig;
import com.ejemplo.testpedidosya.Remote.AppTokenService;
import com.ejemplo.testpedidosya.Remote.RestaurantService;
import com.ejemplo.testpedidosya.Remote.RetrofitClient;

/**
 * Created by JC on 9/12/2017.
 */

public class RemoteUtils {

    private RemoteUtils(){}

    public static AppTokenService getAppTokenService(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(AppTokenService.class);
    }

    public static RestaurantService getRestaurantService(){
        return RetrofitClient.getClient(BuildConfig.BASE_URL).create(RestaurantService.class);
    }

}
