package com.flame.bulgogi.cust.api.service;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Pranay on 24/4/17.
 */

public interface UserService {

    @GET("getCustomerLatLong.php")
    Observable<JsonObject> updatelocation(@Query("lat") String latitude, @Query("long") String longitude, @Query("heading") String heading, @Query("trip_id") String trip);

}
