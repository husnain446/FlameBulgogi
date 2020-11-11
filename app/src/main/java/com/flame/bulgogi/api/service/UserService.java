package com.flame.bulgogi.api.service;

import com.flame.bulgogi.api.request.LoginRequest;
import com.flame.bulgogi.api.response.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Pranay on 24/4/17.
 */

public interface UserService {

    @GET("getLatLong.php")
        Observable<String> updatelocation(@Query("lat") String latitude, @Query("long") String longitude, @Query("heading") String heading, @Query("trip") String trip);

}
