package com.naufalrzld.belajarretrofit.services;

import com.naufalrzld.belajarretrofit.model.toko.TokoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Naufal on 26/01/2018.
 */

public interface TokoApiInterface {
    @GET("/toko/{username}")
    Call<TokoResponse> APIGetToko(@Path("username") String username);
}